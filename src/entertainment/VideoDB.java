package entertainment;

import user.UserDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        int index = 0;

        for (Video movie : movies) {
            movie.setIndexInDatabase(index);
            index++;
        }

        for (Video serial : serials) {
            serial.setIndexInDatabase(index);
            index++;
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

    private void setPopularGenresVideos(final List<Video> videos) {
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

    public void setNumberFavoritesAllVideos(final UserDB userDB) {
        for (Video movie : movies) {
            userDB.setNumberFavoritesVideo(movie);
        }

        for (Video serial : serials) {
            userDB.setNumberFavoritesVideo(serial);
        }
    }

    public void setNumberViewsAllVideos(final UserDB userDB) {
        for (Video movie : movies) {
            userDB.setNumberViewsVideo(movie);
        }

        for (Video serial : serials) {
            userDB.setNumberViewsVideo(serial);
        }
    }

    /* ////////////////////// Queries for Video ////////////////////// */

    /* /////////// 1. Rating /////////// */
    public void sortVideosByRating(final String sortType, final List<Video> videos) {
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

    private void filterVideosRating(final List<Video> videos, final List<List<String>> filters,
                                   final HashMap<String, Video> videoHashMap) {
        List<String> years = filters.get(0);
        List<String> genres = filters.get(1);

        for (Video video : videoHashMap.values()) {
            video.calculateFinalRating();
            int year = video.getYear();
            List<String> genre = video.getGenres();

            if (video.getRatingVideo() != 0.0 && (years.get(0) == null || years.contains("" + year)) &&
                    (genres.get(0) == null || genre.containsAll(genres))) {
                videos.add(video);
            }
        }
    }

    public List<String> rating(final String sortType, final int number,
                               final List<List<String>> filters, final String objectType) {
        List<Video> videos = new ArrayList<>();

        switch (objectType) {
            case "movies" -> filterVideosRating(videos, filters, movieHashMap);
            case "shows" -> filterVideosRating(videos, filters, serialHashMap);
        }

        sortVideosByRating(sortType, videos);

        List<String> copyVideos = new ArrayList<>();
        for (int i = 0; i < Math.min(videos.size(), number); i++) {
            copyVideos.add(videos.get(i).getTitle());
        }

        return copyVideos;
    }

    /* /////////// 2. Favorite /////////// */
    private void sortVideosByFavorite(final String sortType, final List<Video> videos) {
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

    private void filterVideosFavorite(final List<Video> copyVideos, final List<List<String>> filters,
                                final List<Video> videos, final UserDB userDB) {
        List<String> years = filters.get(0);
        List<String> genres = filters.get(1);

        for (Video video : videos) {
            int year = video.getYear();
            List<String> genre = video.getGenres();

            if ((years.get(0) == null || years.contains("" + year))
                    && (genres.get(0) == null || genre.containsAll(genres))) {
                userDB.setNumberFavoritesVideo(video);
                if (video.getNumberOfFavorites() != 0) {
                    copyVideos.add(video);
                }
            }
        }

    }

    public List<String> favorite(final String sortType, final int number,
                                 final List<List<String>> filters, final UserDB userDB, final String objectType) {
        List<Video> videos = new ArrayList<>();

        switch (objectType) {
            case "movies" -> filterVideosFavorite(videos, filters, movies, userDB);
            case "shows" -> filterVideosFavorite(videos, filters, serials, userDB);
        }

        sortVideosByFavorite(sortType, videos);

        List<String> copyVideos = new ArrayList<>();

        for (int i = 0; i < Math.min(videos.size(), number); i++) {
            copyVideos.add(videos.get(i).getTitle());
        }

        return copyVideos;
    }

    /* /////////// 3. Longest /////////// */
    private void sortVideosByDuration(final String sortType, final List<Video> videos) {
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

    private void filterVideosLongest(final List<Video> copyVideos, final List<List<String>> filters,
                                    final List<Video> videos) {
        List<String> years = filters.get(0);
        List<String> genres = filters.get(1);

        for (Video video : videos) {
            int year = video.getYear();
            List<String> genre = video.getGenres();

            if ((years.get(0) == null || years.contains("" + year))
                && (genres.get(0) == null || genre.containsAll(genres))) {
                copyVideos.add(video);
            }
        }
    }

    public List<String> longest(final String sortType, final int number,
                                final List<List<String>> filters, final String objectType) {
        List<Video> videos = new ArrayList<>();

        switch (objectType) {
            case "movies" -> filterVideosLongest(videos, filters, movies);
            case "shows" -> filterVideosLongest(videos, filters, serials);
        }

        sortVideosByDuration(sortType, videos);

        List<String> copyVideos = new ArrayList<>();
        for (int i = 0; i < Math.min(videos.size(), number); i++) {
            copyVideos.add(videos.get(i).getTitle());
        }

        return copyVideos;
    }

    /* /////////// 4. Most Viewed /////////// */
    private void sortVideosByViews(final String sortType, final List<Video> videos) {
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

    private void filterVideosMostViewed(final List<Video> copyVideos, final List<List<String>> filters,
                                     final List<Video> videos, final UserDB userDB) {
        List<String> years = filters.get(0);
        List<String> genres = filters.get(1);
        genres.sort(String::compareTo);

        for (Video video : videos) {
            int year = video.getYear();
            List<String> genre = video.getGenres();
            genre.sort(String::compareTo);

            if ((years.get(0) == null || years.contains("" + year)) &&
                    (genres.get(0) == null || genre.containsAll(genres))) {
                userDB.setNumberViewsVideo(video);
                if (video.getNumberViews() != 0) {
                    copyVideos.add(video);
                }
            }
        }
    }

    public List<String> mostViewed(final String sortType, final int number,
                                   final List<List<String>> filters, final String objectType, final UserDB userDB) {
        List<Video> videos = new ArrayList<>();

        switch (objectType) {
            case "movies" -> filterVideosMostViewed(videos, filters, movies, userDB);
            case "shows" -> filterVideosMostViewed(videos, filters, serials, userDB);
        }

        sortVideosByViews(sortType, videos);

        List<String> copyVideos = new ArrayList<>();
        for (int i = 0; i < Math.min(videos.size(), number); i++) {
            copyVideos.add(videos.get(i).getTitle());
        }

        return copyVideos;
    }
}
