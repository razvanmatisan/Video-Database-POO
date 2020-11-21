package user;

import entertainment.Video;
import entertainment.VideoDB;

import java.util.*;

public class User {
    private final String username;
    private final String subscriptionType;
    private final Map<String, Integer> history;
    private final ArrayList<String> favoriteVideos;

    private Map<String, ArrayList<Integer>> videosWithRating;
    private Integer numberGivenRatings;

    public User(final String username, final String subscriptionType,
                         final Map<String, Integer> history,
                         final ArrayList<String> favoriteVideos) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.favoriteVideos = favoriteVideos;
        this.history = history;
        this.videosWithRating = new HashMap<>();
        this.numberGivenRatings = 0;
    }

    public String getUsername() {
        return username;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public ArrayList<String> getFavoriteVideos() {
        return favoriteVideos;
    }

    public Integer getNumberGivenRatings() {
        return numberGivenRatings;
    }

    /* ////////////////////// Commands ////////////////////// */
    public String favorite(String title) {
        if (favoriteVideos.contains(title)) {
            return "Already in Favorites!";
        } else if (history.containsKey(title)) {
            favoriteVideos.add(title);
            return "Added to Favorites!";
        }
        return "Haven't been seen yet!";
    }

    public void view(String title) {
        if (history.containsKey(title)) {
            history.put(title, history.get(title) + 1);
        } else {
            history.put(title, 1);
        }
    }

    public String ratingVideo(Video video, final double rating, int numberSeason) {
        String title = video.getTitle();
        if (videosWithRating.containsKey(title)) {
            ArrayList<Integer> seasons = videosWithRating.get(title);
            if (seasons.contains(numberSeason)) {
                return "Already rated!";
            } else {
                video.giveRating(rating, numberSeason);
                seasons.add(numberSeason);
                numberGivenRatings += 1;
                return "Added rating!";
            }
        } else if (history.containsKey(title)) {
            video.giveRating(rating, numberSeason);

            ArrayList<Integer> seasons = new ArrayList<>();
            seasons.add(numberSeason);
            videosWithRating.put(title, seasons);

            numberGivenRatings += 1;

            return "Added rating!";
        }

        return "Not seen!";
    }

    /* ////////////////////// Recommendations ////////////////////// */

    private String firstUnseenVideo(List<Video> videos) {
        for (Video video : videos) {
            String title = video.getTitle();
            if (!history.containsKey(title)) {
                return title;
            }
        }
        return "";
    }

    /* /////////// 1. Standard /////////// */
    public String standard(VideoDB videoDB) {
        List<Video> movies = videoDB.getMovies();
        List<Video> serials = videoDB.getSerials();

        String title = firstUnseenVideo(movies);
        if (title.equals("")) {
            title = firstUnseenVideo(serials);
        }

        return title;
    }

    /* /////////// 2. Best Unseen /////////// */
    public void updateRatingVideos(List<Video> copyVideos, List<Video> videos) {
        for (Video video : videos) {
            video.calculateFinalRating();
            copyVideos.add(video);
        }
    }

    public String bestUnseen(VideoDB videoDB) {
        List<Video> videos = new ArrayList<>();

        List<Video> movies = videoDB.getMovies();
        List<Video> serials = videoDB.getSerials();

        updateRatingVideos(videos, movies);
        updateRatingVideos(videos, serials);

        videos.sort((v1, v2) -> {
            if (v2.getRatingVideo().equals(v1.getRatingVideo())) {
                return v1.getIndexInDatabase().compareTo(v2.getIndexInDatabase());
            }
            return v2.getRatingVideo().compareTo(v1.getRatingVideo());
        });

        for (Video video : videos) {
            String title = video.getTitle();
            if (!history.containsKey(title)) {
                return title;
            }
        }

        return "";
    }

    /* /////////// 3. Popular /////////// */
    public String popular(VideoDB videoDB, UserDB userDB) {
        if (subscriptionType.equals("PREMIUM")) {
            videoDB.setNumberViewsAllVideos(userDB);
            videoDB.setPopularGenresAllVideos();

            HashMap<String, Integer> popularGenres = videoDB.getPopularGenres();

            List<Video> movies = videoDB.getMovies();
            List<Video> serials = videoDB.getSerials();

            String title = "";

            while (!popularGenres.isEmpty()) {
                String genre = Collections.max(popularGenres.entrySet(), Map.Entry.comparingByValue()).getKey();
//                System.out.println(genre);
                //System.out.println(Collections.max(popularGenres.entrySet(), Map.Entry.comparingByValue()));

                for (Video movie : movies) {
                    if (movie.getGenres().contains(genre) && !history.containsKey(movie.getTitle())) {
                        title = movie.getTitle();
                        break;
                    }
                }

                if (title.equals("")) {
                    for (Video serial : serials) {
                        if (serial.getGenres().contains(genre) && !history.containsKey(serial.getTitle())) {
                            title = serial.getTitle();
                            break;
                        }
                    }
                } else {
                    break;
                }

                if (title.equals("")) {
                    popularGenres.remove(genre);
                } else {
                    break;
                }
            }

            //System.out.println("-----------");

            videoDB.setPopularGenresAllVideos();
            return title;
        }

        return "";
    }


    /* /////////// 4. Favorite /////////// */
    public void filterFavoriteVideos(List<Video> copyVideos, List<Video> videos) {
        for (Video video : videos) {
            Integer numberFavorites = video.getNumberOfFavorites();
            if (numberFavorites != 0) {
                copyVideos.add(video);
            }
        }
    }

    public String favoriteRecommendation(VideoDB videoDB, UserDB userDB) {
        String title = "";

        if (subscriptionType.equals("PREMIUM")) {
            List<Video> videos = new ArrayList<>();

            videoDB.setNumberFavoritesAllVideos(userDB);

            List<Video> movies = videoDB.getMovies();
            List<Video> serials = videoDB.getSerials();

            filterFavoriteVideos(videos, movies);
            filterFavoriteVideos(videos, serials);

            videos.sort((v1, v2) -> {
                if (v2.getNumberOfFavorites().equals(v1.getNumberOfFavorites())) {
                    return v1.getIndexInDatabase().compareTo(v2.getIndexInDatabase());
                }
                return v2.getNumberOfFavorites().compareTo(v1.getNumberOfFavorites());
            });

            return firstUnseenVideo(videos);
        }

        return "";
    }

    public List<String> search(String genre, UserDB userDB, VideoDB videoDB) {
        List <String> titles = new ArrayList<>();
        if (subscriptionType.equals("PREMIUM")) {
            List<Video> videos = new ArrayList<>();

            videoDB.setNumberFavoritesAllVideos(userDB);

            videoDB.setVideosByGenre(genre, videos);
            videoDB.sortVideosByRating("asc", videos);

            videos.removeIf(video -> history.containsKey(video.getTitle()));

            for (int i = 0; i < videos.size(); i++) {
                titles.add(videos.get(i).getTitle());
            }

            return titles;
        }

        return titles;
    }
}
