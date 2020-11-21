package entertainment;

import java.util.ArrayList;

public abstract class Video {
    private final String title;
    private final int year;
    private final ArrayList<String> cast;
    private final ArrayList<String> genres;

    protected Integer indexInDatabase;
    protected Integer numberViews;
    protected Double ratingVideo;
    protected Integer numberOfFavorites;

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

    public Integer getNumberOfFavorites() {
        return numberOfFavorites;
    }

    public void setNumberOfFavorites(Integer numberOfFavorites) {
        this.numberOfFavorites = numberOfFavorites;
    }

    public Double getRatingVideo() {
        return ratingVideo;
    }

    public Integer getNumberViews() {
        return numberViews;
    }

    public void setNumberViews(Integer numberViews) {
        this.numberViews = numberViews;
    }

    public Integer getIndexInDatabase() {
        return indexInDatabase;
    }

    public void setIndexInDatabase(Integer indexDatabase) {
        this.indexInDatabase = indexDatabase;
    }

    abstract public void giveRating(final double rating, int numberSeason);

    abstract public Integer getDuration();

    abstract public void calculateFinalRating();
}
