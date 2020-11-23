package user;

import entertainment.Video;
import entertainment.VideoDB;
import sorts.SortRecommendationBestUnseen;
import sorts.SortRecommendationFavorite;
import sorts.SortRecommendationSearch;
import sorts.StrategyRecommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class User {
    private final String username;
    private final String subscriptionType;
    private final Map<String, Integer> history;
    private final ArrayList<String> favoriteVideos;

    private final Map<String, ArrayList<Integer>> videosWithRating;
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

    public ArrayList<String> getFavoriteVideos() {
        return favoriteVideos;
    }

    public Integer getNumberGivenRatings() {
        return numberGivenRatings;
    }

    /* ////////////////////// Commands ////////////////////// */
    public String favorite(final String title) {
        if (favoriteVideos.contains(title)) {
            return "Already in Favorites!";
        } else if (history.containsKey(title)) {
            favoriteVideos.add(title);
            return "Added to Favorites!";
        }
        return "Haven't been seen yet!";
    }

    public void view(final String title) {
        if (history.containsKey(title)) {
            history.put(title, history.get(title) + 1);
        } else {
            history.put(title, 1);
        }
    }

    public String ratingVideo(final Video video, final double rating, final int numberSeason) {
        String title = video.getTitle();
        if (videosWithRating.containsKey(title)) {
            ArrayList<Integer> seasons = videosWithRating.get(title);
            if (seasons.contains(numberSeason)) {
                return "Already rated!";
            } else {
                video.giveRating(rating, numberSeason);
                seasons.add(numberSeason);
                numberGivenRatings++;
                return "Added rating!";
            }
        } else if (history.containsKey(title)) {
            video.giveRating(rating, numberSeason);

            ArrayList<Integer> seasons = new ArrayList<>();
            seasons.add(numberSeason);
            videosWithRating.put(title, seasons);

            numberGivenRatings++;

            return "Added rating!";
        }

        return "Not seen!";
    }

    /* ////////////////////// Recommendations ////////////////////// */

    private String firstUnseenVideo(final List<Video> videos) {
        for (Video video : videos) {
            String title = video.getTitle();
            if (!history.containsKey(title)) {
                return title;
            }
        }
        return "";
    }

    /* /////////// 1. Standard /////////// */
    public String standard(final VideoDB videoDB) {
        List<Video> movies = videoDB.getMovies();
        List<Video> serials = videoDB.getSerials();

        String title = firstUnseenVideo(movies);
        if (title.equals("")) {
            title = firstUnseenVideo(serials);
        }

        return title;
    }

    /* /////////// 2. Best Unseen /////////// */
    private void updateRatingVideos(final List<Video> copyVideos, final List<Video> videos) {
        for (Video video : videos) {
            video.calculateFinalRating();
            copyVideos.add(video);
        }
    }

    public String bestUnseen(final VideoDB videoDB) {
        List<Video> videos = new ArrayList<>();

        List<Video> movies = videoDB.getMovies();
        List<Video> serials = videoDB.getSerials();

        updateRatingVideos(videos, movies);
        updateRatingVideos(videos, serials);

        StrategyRecommendation strategy
                = new StrategyRecommendation(new SortRecommendationBestUnseen());
        strategy.sort(videos);

        return firstUnseenVideo(videos);
    }

    /* /////////// 3. Popular /////////// */
    private String findVideoByPopularGenre(final List<Video> movies, final List<Video> serials,
                                           final HashMap<String, Integer> popularGenres) {
        while (!popularGenres.isEmpty()) {
            String title;
            String genre = Collections.max(popularGenres.entrySet(),
                    Map.Entry.comparingByValue()).getKey();

            title = findVideoByGenre(genre, movies);

            if (title.equals("")) {
                title = findVideoByGenre(genre, serials);
            }

            if (!title.equals("")) {
                return title;
            }
            popularGenres.remove(genre);
        }

        return "";
    }

    private String findVideoByGenre(final String genre, final List<Video> videos) {
        String title;
        for (Video video : videos) {
            if (video.getGenres().contains(genre) && !history.containsKey(video.getTitle())) {
                title = video.getTitle();
                return title;
            }
        }
        return "";
    }

    public String popular(final VideoDB videoDB, final UserDB userDB) {
        if (subscriptionType.equals("PREMIUM")) {
            videoDB.setNumberViewsAllVideos(userDB);
            videoDB.setPopularGenresAllVideos();

            HashMap<String, Integer> popularGenres = videoDB.getPopularGenres();

            List<Video> movies = videoDB.getMovies();
            List<Video> serials = videoDB.getSerials();

            return findVideoByPopularGenre(movies, serials, popularGenres);
        }

        return "";
    }


    /* /////////// 4. Favorite /////////// */
    private void filterFavoriteVideos(final List<Video> copyVideos, final List<Video> videos) {
        for (Video video : videos) {
            Integer numberFavorites = video.getNumberOfFavorites();
            if (numberFavorites != 0) {
                copyVideos.add(video);
            }
        }
    }

    public String favoriteRecommendation(final VideoDB videoDB, final UserDB userDB) {
        if (subscriptionType.equals("PREMIUM")) {
            List<Video> videos = new ArrayList<>();

            videoDB.setNumberFavoritesAllVideos(userDB);

            List<Video> movies = videoDB.getMovies();
            List<Video> serials = videoDB.getSerials();

            filterFavoriteVideos(videos, movies);
            filterFavoriteVideos(videos, serials);

            StrategyRecommendation strategy
                    = new StrategyRecommendation(new SortRecommendationFavorite());
            strategy.sort(videos);

            return firstUnseenVideo(videos);
        }

        return "";
    }

    public List<String> search(final String genre, final UserDB userDB, final VideoDB videoDB) {
        if (subscriptionType.equals("PREMIUM")) {
            List<String> titles;
            List<Video> videos = new ArrayList<>();

            videoDB.setNumberFavoritesAllVideos(userDB);

            videoDB.setVideosByGenre(genre, videos);

            StrategyRecommendation strategy
                    = new StrategyRecommendation(new SortRecommendationSearch());
            strategy.sort(videos);

            videos.removeIf(video -> history.containsKey(video.getTitle()));

            titles = videos.stream()
                .map(Video::getTitle)
                .collect(Collectors.toList());

            return titles;
        }

        return new ArrayList<>();
    }
}
