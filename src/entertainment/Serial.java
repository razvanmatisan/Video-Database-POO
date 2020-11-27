package entertainment;

import java.util.ArrayList;
import java.util.List;

public final class Serial extends Video {
    private final int numberOfSeasons;
    private final ArrayList<Season> seasons;
    private final Integer duration;

    public Serial(final String title, final ArrayList<String> cast,
                           final ArrayList<String> genres,
                           final int numberOfSeasons, final ArrayList<Season> seasons,
                           final int year) {
        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
        this.duration = calculateDuration();
    }

    public int getNumberSeason() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public Integer getDuration() {
        return duration;
    }

    @Override
    public void giveRating(final double rating, final int currentSeason) {
        if (currentSeason <= 0) {
            return;
        }

        Season season = seasons.get(currentSeason - 1);

        List<Double> ratings = season.getRatings();
        ratings.add(rating);
        season.setRatings(ratings);
    }

    @Override
    public void calculateFinalRating() {
        double sum = 0.0;

        for (Season season : seasons) {
            List<Double> ratings = season.getRatings();
            if (ratings.size() != 0) {
                double sumSeason = 0.0;
                for (double rating : ratings) {
                    sumSeason += rating;
                }
                sum += (sumSeason / ratings.size());
            }
        }

        if (sum == 0.0) {
            super.ratingVideo = 0.0;
        } else {
            super.ratingVideo = (sum / numberOfSeasons);
        }
    }

    /**
     * Calculate the duration of the entire serial
     * @return the total duration
     */
    private Integer calculateDuration() {
        int totalDuration = 0;

        for (Season season : seasons) {
            totalDuration += season.getDuration();
        }

        return totalDuration;
    }
}
