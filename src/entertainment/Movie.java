package entertainment;

import java.util.ArrayList;
import java.util.List;

public final class Movie extends Video {
    private final Integer duration;
    private final List<Double> ratings = new ArrayList<>();

    public Movie(final String title, final ArrayList<String> cast,
                          final ArrayList<String> genres, final int year,
                          final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
    }

    public Integer getDuration() {
        return duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    @Override
    public void giveRating(final double rating, final int numberSeason) {
        /*
        If this video has not seasons, then it is a Movie.
         */
        if (numberSeason != 0) {
            return;
        }
        this.ratings.add(rating);
    }

    @Override
    public void calculateFinalRating() {
        if (ratings.size() == 0) {
            super.ratingVideo = 0.0;
            return;
        }

        Double sum = 0.0;
        for (Double rating : ratings) {
            sum += rating;
        }

        super.ratingVideo = (sum / ratings.size());
    }
}
