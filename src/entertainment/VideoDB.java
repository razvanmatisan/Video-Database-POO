package entertainment;

import user.User;
import user.UserDB;

import java.util.*;

public class VideoDB {
    private final HashMap<String, Video> movieHashMap = new HashMap<>();
    private final HashMap<String, Video> serialHashMap = new HashMap<>();
    private final List<Video> movies = new ArrayList<>();
    private final List<Video> serials = new ArrayList<>();

    private final HashMap<String, Integer> popularGenres = new HashMap<>();

    public VideoDB(List<Movie> movies, List<Serial> series) {
        for (Movie movie : movies) {
            this.movieHashMap.put(movie.getTitle(), movie);
            this.movies.add(movie);
        }
        for (Serial serial : series) {
            this.serialHashMap.put(serial.getTitle(), serial);
            this.serials.add(serial);
        }
    }

    public HashMap<String, Video> getMovieHashMap() {
        return movieHashMap;
    }

    public HashMap<String, Video> getSerialHashMap() {
        return serialHashMap;
    }

    public HashMap<String, Integer> getPopularGenres() {
        return popularGenres;
    }

    public List<Video> getMovies() {
        return movies;
    }

    public List<Video> getSerials() {
        return serials;
    }

    public void setIndexDatabase() {
        Integer index = 0;

        for (Video movie : movies) {
            movie.setIndexInDatabase(index);
            index += 1;
        }

        for (Video serial : serials) {
            serial.setIndexInDatabase(index);
            index += 1;
        }
    }

    public void setVideosByGenre(String genre, List<Video> videos) {
        for (Video movie : movies) {
            if (movie.getGenres().contains(genre)) {
                videos.add(movie);
            }
        }

        for (Video serial : serials) {
            if (serial.getGenres().contains(genre)) {
                videos.add(serial);
            }
        }
    }

    private void setPopularGenresVideos(List<Video> videos) {
        for (Video video : videos) {
            ArrayList<String> genres = video.getGenres();
            Integer numberViews = video.getNumberViews();

            for (String genre : genres) {
                if (popularGenres.containsKey(genre)) {
                    popularGenres.put(genre, popularGenres.get(genre) + numberViews);
                } else {
                    popularGenres.put(genre, numberViews);
                }
            }
        }
    }

    public void setPopularGenresAllVideos() {
        setPopularGenresVideos(movies);
        setPopularGenresVideos(serials);
    }

    public void setNumberFavoritesAllVideos(UserDB userDB) {
        for (User user : userDB.getUserHashMap().values()) {
            for (Video movie : movies) {
                user.setNumberFavoritesVideo(movie);
            }

            for (Video serial : serials) {
                user.setNumberFavoritesVideo(serial);
            }
        }
    }

    public void setNumberViewsAllVideos(UserDB userDB) {
        for (User user : userDB.getUserHashMap().values()) {
            for (Video movie : movies) {
                user.setNumberViewsVideo(movie);
            }

            for (Video serial : serials) {
                user.setNumberViewsVideo(serial);
            }
        }
    }

    /* ////////////////////// Queries for Video ////////////////////// */

    /* /////////// 1. Rating /////////// */
    public void sortVideosByRating(String sortType, List<Video> videos) {
        switch (sortType) {
            case "asc" -> videos.sort((v1, v2) -> {
                if (v1.getRatingVideo().equals(v2.getRatingVideo())) {
                    return v1.getTitle().compareTo(v2.getTitle());
                }
                return v1.getRatingVideo().compareTo(v2.getRatingVideo());
            });
            case "desc" -> videos.sort((v1, v2) -> {
                if (v2.getRatingVideo().equals(v1.getRatingVideo())) {
                    return v2.getTitle().compareTo(v1.getTitle());
                }
                return v2.getRatingVideo().compareTo(v1.getRatingVideo());
            });
        }
    }

    public void filterVideosRating(List<Video> videos, List<List<String>> filters,
                                   HashMap<String, Video> videoHashMap) {
        List<String> years = filters.get(0);
        List<String> genres = filters.get(1);

        for (Video video : videoHashMap.values()) {
            video.calculateFinalRating();
            int year = video.getYear();
            List<String> genre = video.getGenres();

            if (video.getRatingVideo() != 0.0 && years.contains("" + year) && genres.contains(genre.get(0))) {
                videos.add(video);
            }
        }
    }

    public List<String> rating(String sortType, int number, List<List<String>> filters, String objectType) {
        List<Video> videos = new ArrayList<>();

        switch(objectType) {
            case "movies":
                filterVideosRating(videos, filters, movieHashMap);
            case "shows":
                filterVideosRating(videos, filters, serialHashMap);
        }

        sortVideosByRating(sortType, videos);

        List<String> copyVideos = new ArrayList<>();
        for (int i = 0; i < Math.min(videos.size(), number); i++) {
            copyVideos.add(videos.get(i).getTitle());
        }

        return copyVideos;
    }

    /* /////////// 2. Favorite /////////// */
    public void sortVideosByFavorite(String sortType, List<Video> videos) {
        switch (sortType) {
            case "asc" -> videos.sort((v1, v2) -> {
                if (v1.getNumberOfFavorites().equals(v2.getNumberOfFavorites())) {
                    return v1.getTitle().compareTo(v2.getTitle());
                }
                return v1.getNumberOfFavorites().compareTo(v2.getNumberOfFavorites());
            });
            case "desc" -> videos.sort((v1, v2) -> {
                if (v2.getNumberOfFavorites().equals(v1.getNumberOfFavorites())) {
                    return v2.getTitle().compareTo(v1.getTitle());
                }
                return v2.getNumberOfFavorites().compareTo(v1.getNumberOfFavorites());
            });
        }
    }

    public void filterVideosFavorite(List<Video> videos, List<List<String>> filters,
                                HashMap<String, Video> videoHashMap, UserDB userDB) {
        List<String> years = filters.get(0);
        List<String> genres = filters.get(1);

        for (Video video : videoHashMap.values()) {
            int year = video.getYear();
            List<String> genre = video.getGenres();

            if (years.contains("" + year) && Collections.indexOfSubList(genre, genres) != -1) {
                for (User user : userDB.getUserHashMap().values()) {
                    user.setNumberFavoritesVideo(video);
                }
                if (video.getNumberOfFavorites() != 0) {
                    videos.add(video);
                }
            }
        }

    }

    public List<String> favorite(String sortType, int number,
                                 List<List<String>> filters, UserDB userDB, String objectType) {
        List<Video> videos = new ArrayList<>();

        switch(objectType) {
            case "movies":
                filterVideosFavorite(videos, filters, movieHashMap, userDB);
            case "shows":
                filterVideosFavorite(videos, filters, serialHashMap, userDB);
        }

        sortVideosByFavorite(sortType, movies);
        sortVideosByFavorite(sortType, serials);

        List<String> copyVideos = new ArrayList<>();
        for (int i = 0; i < Math.min(videos.size(), number); i++) {
            copyVideos.add(videos.get(i).getTitle());
        }

        return copyVideos;
    }

    /* /////////// 3. Longest /////////// */
    public void sortVideosByDuration(String sortType, List<Video> videos) {
        switch (sortType) {
            case "asc" -> videos.sort((v1, v2) -> {
                if (v1.getDuration().equals(v2.getDuration())) {
                    return v1.getTitle().compareTo(v2.getTitle());
                }
                return v1.getDuration().compareTo(v2.getDuration());
            });
            case "desc" -> videos.sort((v1, v2) -> {
                if (v2.getDuration().equals(v1.getDuration())) {
                    return v2.getTitle().compareTo(v1.getTitle());
                }
                return v2.getDuration().compareTo(v1.getDuration());
            });
        }
    }

    public void filterVideosLongest(List<Video> videos, List<List<String>> filters,
                                    HashMap<String, Video> videoHashMap) {
        List<String> years = filters.get(0);
        List<String> genres = filters.get(1);

        for (Video video : videoHashMap.values()) {
            int year = video.getYear();
            List<String> genre = video.getGenres();

            if (years.contains("" + year) && Collections.indexOfSubList(genre, genres) != -1) {
                videos.add(video);
            }
        }
    }

    public List<String> longest(String sortType, int number,
                                List<List<String>> filters, String objectType) {
        List<Video> videos = new ArrayList<>();

        switch(objectType) {
            case "movies":
                filterVideosLongest(videos, filters, movieHashMap);
            case "shows":
                filterVideosLongest(videos, filters, serialHashMap);
        }

        sortVideosByDuration(sortType, videos);

        List<String> copyVideos = new ArrayList<>();
        for (int i = 0; i < Math.min(videos.size(), number); i++) {
            copyVideos.add(videos.get(i).getTitle());
        }

        return copyVideos;
    }

    /* /////////// 4. Most Viewed /////////// */
    public void sortVideosByViews(String sortType, List<Video> videos) {
        switch (sortType) {
            case "asc" -> videos.sort((v1, v2) -> {
                if (v1.getNumberViews().equals(v2.getNumberViews())) {
                    return v1.getTitle().compareTo(v2.getTitle());
                }
                return v1.getNumberViews().compareTo(v2.getNumberViews());
            });
            case "desc" -> videos.sort((v1, v2) -> {
                if (v2.getNumberViews().equals(v1.getNumberViews())) {
                    return v2.getTitle().compareTo(v1.getTitle());
                }
                return v2.getNumberViews().compareTo(v1.getNumberViews());
            });
        }
    }

    public void filterVideosMostViewed(List<Video> videos, List<List<String>> filters,
                                     HashMap<String, Video> videoHashMap, UserDB userDB) {
        List<String> years = filters.get(0);
        List<String> genres = filters.get(1);

        for (Video video : videoHashMap.values()) {
            int year = video.getYear();
            List<String> genre = video.getGenres();

            if (years.contains("" + year) && Collections.indexOfSubList(genre, genres) != -1) {
                for (User user : userDB.getUserHashMap().values()) {
                    user.setNumberViewsVideo(video);
                }

                if (video.getNumberViews() != 0) {
                    videos.add(video);
                }
            }
        }
    }

    public List<String> mostViewed(String sortType, int number,
                                   List<List<String>> filters, String objectType, UserDB userDB) {
        List<Video> videos = new ArrayList<>();

        switch(objectType) {
            case "movies":
                filterVideosMostViewed(videos, filters, movieHashMap, userDB);
            case "shows":
                filterVideosMostViewed(videos, filters, serialHashMap, userDB);
        }

        sortVideosByViews(sortType, videos);

        List<String> copyVideos = new ArrayList<>();
        for (int i = 0; i < Math.min(videos.size(), number); i++) {
            copyVideos.add(videos.get(i).getTitle());
        }

        return copyVideos;
    }
}
