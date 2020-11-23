package entertainment;

import sorts.SortVideoByDuration;
import sorts.SortVideoByFavorite;
import sorts.SortVideoByRating;
import sorts.SortVideoByViews;
import sorts.StrategyVideo;

import user.UserDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class VideoDB {
    private final List<Video> movies = new ArrayList<>();
    private final List<Video> serials = new ArrayList<>();

    private final HashMap<String, Integer> popularGenres = new HashMap<>();

    public VideoDB(final List<Movie> movies, final List<Serial> series) {
        this.movies.addAll(movies);
        this.serials.addAll(series);
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

    public Video findMovie(final String title) {
        for (Video movie : movies) {
            if (movie.getTitle().equals(title)) {
                return movie;
            }
        }
        return null;
    }

    public Video findSerial(final String title) {
        for (Video serial : serials) {
            if (serial.getTitle().equals(title)) {
                return serial;
            }
        }
        return null;
    }

    public void setVideosByGenre(final String genre, final List<Video> videos) {
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
    private void filterVideos(final List<Video> copyVideos,
                              final List<List<String>> filters, final List<Video> videos) {
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

    private List<String> getFinalListVideos(final int number, final List<Video> videos) {
        List<String> copyVideos = new ArrayList<>();
        for (int i = 0; i < Math.min(videos.size(), number); i++) {
            copyVideos.add(videos.get(i).getTitle());
        }

        return copyVideos;
    }

    /* /////////// 1. Rating /////////// */
    public List<String> rating(final String sortType, final int number,
                               final List<List<String>> filters, final String objectType) {
        List<Video> videos = new ArrayList<>();

        switch (objectType) {
            case "movies" -> filterVideos(videos, filters, movies);
            case "shows" -> filterVideos(videos, filters, serials);
            default -> throw new IllegalStateException("Unexpected value: " + objectType);
        }

        for (Video video : videos) {
            video.calculateFinalRating();
        }

        videos.removeIf(video -> video.getRatingVideo() == 0);

        StrategyVideo strategy = new StrategyVideo(new SortVideoByRating());
        strategy.sort(sortType, videos);

        return getFinalListVideos(number, videos);
    }

    /* /////////// 2. Favorite /////////// */
    public List<String> favorite(final String sortType,
                                 final int number, final List<List<String>> filters,
                                 final UserDB userDB, final String objectType) {
        List<Video> videos = new ArrayList<>();

        switch (objectType) {
            case "movies" -> filterVideos(videos, filters, movies);
            case "shows" -> filterVideos(videos, filters, serials);
            default -> throw new IllegalStateException("Unexpected value: " + objectType);
        }

        for (Video video : videos) {
            userDB.setNumberFavoritesVideo(video);
        }
        videos.removeIf(video -> video.getNumberOfFavorites() == 0);

        StrategyVideo strategy = new StrategyVideo(new SortVideoByFavorite());
        strategy.sort(sortType, videos);

        return getFinalListVideos(number, videos);
    }

    /* /////////// 3. Longest /////////// */
    public List<String> longest(final String sortType, final int number,
                                final List<List<String>> filters, final String objectType) {
        List<Video> videos = new ArrayList<>();

        switch (objectType) {
            case "movies" -> filterVideos(videos, filters, movies);
            case "shows" -> filterVideos(videos, filters, serials);
            default -> throw new IllegalStateException("Unexpected value: " + objectType);
        }

        StrategyVideo strategy = new StrategyVideo(new SortVideoByDuration());
        strategy.sort(sortType, videos);

        return getFinalListVideos(number, videos);
    }

    /* /////////// 4. Most Viewed /////////// */
    public List<String> mostViewed(final String sortType,
                                   final int number, final List<List<String>> filters,
                                   final String objectType, final UserDB userDB) {
        List<Video> videos = new ArrayList<>();

        switch (objectType) {
            case "movies" -> filterVideos(videos, filters, movies);
            case "shows" -> filterVideos(videos, filters, serials);
            default -> throw new IllegalStateException("Unexpected value: " + objectType);
        }

        for (Video video : videos) {
            userDB.setNumberViewsVideo(video);
        }
        videos.removeIf(video -> video.getNumberViews() == 0);

        StrategyVideo strategy = new StrategyVideo(new SortVideoByViews());
        strategy.sort(sortType, videos);

        return getFinalListVideos(number, videos);
    }
}
