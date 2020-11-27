package entertainment;

import java.util.ArrayList;

public abstract class Video {
    private final String title;
    private final int year;
    private final ArrayList<String> cast;
    private final ArrayList<String> genres;

    /**
     * The index from database
     */
    protected Integer indexInDatabase;

    /**
     * The total number of views the users watched the video
     */
    protected Integer numberViews;

    /**
     * The total number of appearances of a video in all users' favorite list
     */
    protected Integer numberOfFavorites;

    /**
     * Video Rating
     */
    protected Double ratingVideo;

    public Video(final String title, final int year,
                 final ArrayList<String> cast, final ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
        this.ratingVideo = 0.0;
        this.numberOfFavorites = 0;
        this.numberViews = 0;
        this.indexInDatabase = 0;
    }

    public final String getTitle() {
        return title;
    }

    public final int getYear() {
        return year;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }

    public final Integer getNumberOfFavorites() {
        return numberOfFavorites;
    }

    public final void setNumberOfFavorites(final Integer numberOfFavorites) {
        this.numberOfFavorites = numberOfFavorites;
    }

    public final Double getRatingVideo() {
        return ratingVideo;
    }

    public final Integer getNumberViews() {
        return numberViews;
    }

    public final void setNumberViews(final Integer numberViews) {
        this.numberViews = numberViews;
    }

    public final Integer getIndexInDatabase() {
        return indexInDatabase;
    }

    public final void setIndexInDatabase(final Integer indexDatabase) {
        this.indexInDatabase = indexDatabase;
    }

    /**
     * Add a rating in the list of ratings given by the users.
     * @param rating the new rating
     * @param numberSeason the number of the season
     *                     for which the rating is. (= 0 if video is a movie)
     */
    public abstract void giveRating(double rating, int numberSeason);

    /**
     * Method that returns the total duration of a video.
     */
    public abstract Integer getDuration();

    /**
     * Method that calculates the final rating of a video.
     * If it's a movie, it's been calculated the average rating of given ratings.
     * If it's a serial, it's been calculated the average rating of all seasons.
     */
    public abstract void calculateFinalRating();
}
