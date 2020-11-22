package utils;

import actor.Actor;
import actor.ActorsAwards;
import entertainment.Movie;
import entertainment.Season;
import entertainment.Serial;
import fileio.ActorInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Conversion {
    private List<User> users = new ArrayList<>();
    private List<Actor> actors = new ArrayList<>();
    private List<Movie> movies = new ArrayList<>();
    private List<Serial> series = new ArrayList<>();

    public List<User> convertUser(List<UserInputData> users) {
        this.users.clear();

        for (UserInputData user : users) {
            String username = user.getUsername();
            String subscriptionType = user.getSubscriptionType();
            Map<String, Integer> history = user.getHistory();
            ArrayList<String> favoriteVideos = user.getFavoriteMovies();

            this.users.add(new User(username, subscriptionType, history, favoriteVideos));
        }

        return this.users;
    }

    public List<Actor> convertActor(List<ActorInputData> actors) {
        this.actors.clear();

        for (ActorInputData actor : actors) {
            String name = actor.getName();
            String careerDescription = actor.getCareerDescription();
            ArrayList<String> filmography = actor.getFilmography();
            Map<ActorsAwards, Integer> awards = actor.getAwards();

            this.actors.add(new Actor(name, careerDescription, filmography, awards));
        }

        return this.actors;
    }

    public List<Movie> convertMovie(List<MovieInputData> movies) {
        this.movies.clear();

        for (MovieInputData movie : movies) {
            String title = movie.getTitle();
            int year = movie.getYear();
            ArrayList<String> cast = movie.getCast();
            ArrayList<String> genres = movie.getGenres();
            int duration = movie.getDuration();

            this.movies.add(new Movie(title, cast, genres, year, duration));
        }

        return this.movies;
    }

    public List<Serial> convertSerial(List<SerialInputData> series) {
        this.series.clear();

        for (SerialInputData serial : series) {
            String title = serial.getTitle();
            int year = serial.getYear();
            ArrayList<String> cast = serial.getCast();
            ArrayList<String> genres = serial.getGenres();
            int numberOfSeasons = serial.getNumberSeason();
            ArrayList<Season> seasons = serial.getSeasons();

            this.series.add(new Serial(title, cast, genres, numberOfSeasons, seasons, year));
        }

        return this.series;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Serial> getSeries() {
        return series;
    }

    public void setSeries(List<Serial> series) {
        this.series = series;
    }

}
