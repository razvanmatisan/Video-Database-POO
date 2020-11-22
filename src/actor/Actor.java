package actor;

import entertainment.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Actor {
    private String name;
    private String careerDescription;
    private ArrayList<String> filmography;
    private final Map<ActorsAwards, Integer> awards;
    private Double rating;

    private Integer numberAwards;

    public Actor(final String name, final String careerDescription,
                 final ArrayList<String> filmography,
                 final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
        this.rating = 0.0;
        this.numberAwards = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public void setCareerDescription(final String careerDescription) {
        this.careerDescription = careerDescription;
    }

    public Double getRating() {
        return rating;
    }

    public Integer getNumberAwards() {
        return numberAwards;
    }

    void setNumberAwardsOneActor() {
        Integer finalNumberAwards = 0;
        for (Integer number : awards.values()) {
            finalNumberAwards += number;
        }
        numberAwards = finalNumberAwards;
    }

    public void calculateRating(final List<Video> movies, final List<Video> serials) {
        Video video = null;

        double averageRating = 0.0;
        int numberVideos = 0;

        for (String title : filmography) {
            for (Video movie : movies) {
                if (movie.getTitle().equals(title)) {
                    video = movie;
                    break;
                }
            }

            if (video == null) {
                for (Video serial : serials) {
                    if (serial.getTitle().equals(title)) {
                        video = serial;
                        break;
                    }
                }
            }

            if (video == null) {
                continue;
            }

            video.calculateFinalRating();
            Double rating = video.getRatingVideo();

            if (rating != 0) {
                numberVideos += 1;
                averageRating += rating;
            }

        }

        if (numberVideos == 0) {
            this.rating = 0.0;
        } else {
            averageRating /= numberVideos;
            this.rating = averageRating;
        }
    }
}
