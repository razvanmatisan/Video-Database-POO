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
    /**
     * List of all movies from input
     */
    private final List<Video> movies = new ArrayList<>();

    /**
     * List of all serials from input
     */
    private final List<Video> serials = new ArrayList<>();

    /**
     * Database that stores the genre of a movie and the the total number of views
     * of users who watch videos of this genre
     */
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

    /**
     * Set the index of all videos in order of appearance from input.
     */
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

    /**
     * Method that finds a movie in database after its title
     * @param title the title of the movie
     * @return the found movie or null in case it's not found
     */
    public Video findMovie(final String title) {
        for (Video movie : movies) {
            if (movie.getTitle().equals(title)) {
                return movie;
            }
        }
        return null;
    }

    /**
     * Method that finds a serial in database after its title
     * @param title the title of the movie
     * @return the found serial or null in case it's not found
     */
    public Video findSerial(final String title) {
        for (Video serial : serials) {
            if (serial.getTitle().equals(title)) {
                return serial;
            }
        }
        return null;
    }

    /**
     * Filter all videos by a given genre
     */
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

    /**
     * Add genres from videos from a given list of videos
     * and/or update their number of views
     */
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

    /**
     * Complete the entire database that stores genres and their number of views
     */
    public void setPopularGenresAllVideos() {
        setPopularGenresVideos(movies);
        setPopularGenresVideos(serials);
    }

    /**
     * Method that sets for all videos the number of times a video is in the list of
     * favorites of all users.
     */
    public void setNumberFavoritesAllVideos(final UserDB userDB) {
        for (Video movie : movies) {
            userDB.setNumberFavoritesVideo(movie);
        }

        for (Video serial : serials) {
            userDB.setNumberFavoritesVideo(serial);
        }
    }

    /**
     * Method that sets for all videos the number of views a video has.
     */
    public void setNumberViewsAllVideos(final UserDB userDB) {
        for (Video movie : movies) {
            userDB.setNumberViewsVideo(movie);
        }

        for (Video serial : serials) {
            userDB.setNumberViewsVideo(serial);
        }
    }

    /**
     * Method that filters the videos by specific criteria
     * @param copyVideos list of videos where filtered videos are stored
     * @param filters criteria of choosing the videos
     * @param videos list of videos that need to be filtered
     */
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

    /**
     * Method that returns the first "number" videos.
     * If number is greater thant the size of the list, returns the entire list.
     */
    private List<String> getFinalListVideos(final int number, final List<Video> videos) {
        List<String> copyVideos = new ArrayList<>();
        for (int i = 0; i < Math.min(videos.size(), number); i++) {
            copyVideos.add(videos.get(i).getTitle());
        }

        return copyVideos;
    }

    /**
     * Method that returns the first "number" videos
     * after they were filtered and sorted by rating.
     */
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

    /**
     * Method that returns the first "number" videos
     * after they were filtered and sorted by number of favorites.
     */
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

    /**
     * Method that returns the first "number" videos
     * after they were filtered and sorted by their duration.
     */
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

    /**
     * Method that returns the first "number" videos
     * after they were filtered and sorted by their number of views.
     */
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
