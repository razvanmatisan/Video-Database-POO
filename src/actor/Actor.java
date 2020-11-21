package actor;

import entertainment.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Actor {
    private String name;
    private String careerDescription;
    private ArrayList<String> filmography;
    private Map<ActorsAwards, Integer> awards;
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

    public void calculateRating(HashMap<String, Video> movieDB, HashMap<String, Video> serialDB) {
        Video video;

        Double averageRating = 0.0;
        int numberVideos = 0;

        for (String title : filmography) {
            Double sum = 0.0;

            if (movieDB.containsKey(title)) {
                video = movieDB.get(title);
            } else if (serialDB.containsKey(title)) {
                video = serialDB.get(title);
            } else {
                continue;
            }

            video.calculateFinalRating();
            Double rating = video.getRatingVideo();

            if (rating != 0) {
                numberVideos += 1;
                averageRating += rating;
            }

        }

        averageRating /= numberVideos;
        this.rating = averageRating;
    }
}
