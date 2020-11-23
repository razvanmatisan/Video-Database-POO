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


public final class Conversion {
    private final List<User> users = new ArrayList<>();
    private final List<Actor> actors = new ArrayList<>();
    private final List<Movie> movies = new ArrayList<>();
    private final List<Serial> serials = new ArrayList<>();

    public List<User> convertUser(final List<UserInputData> users1) {
        this.users.clear();

        for (UserInputData user : users1) {
            String username = user.getUsername();
            String subscriptionType = user.getSubscriptionType();
            Map<String, Integer> history = user.getHistory();
            ArrayList<String> favoriteVideos = user.getFavoriteMovies();

            this.users.add(new User(username, subscriptionType, history, favoriteVideos));
        }

        return this.users;
    }

    public List<Actor> convertActor(final List<ActorInputData> actors1) {
        this.actors.clear();

        for (ActorInputData actor : actors1) {
            String name = actor.getName();
            String careerDescription = actor.getCareerDescription();
            ArrayList<String> filmography = actor.getFilmography();
            Map<ActorsAwards, Integer> awards = actor.getAwards();

            this.actors.add(new Actor(name, careerDescription, filmography, awards));
        }

        return this.actors;
    }

    public List<Movie> convertMovie(final List<MovieInputData> movies1) {
        this.movies.clear();

        for (MovieInputData movie : movies1) {
            String title = movie.getTitle();
            int year = movie.getYear();
            ArrayList<String> cast = movie.getCast();
            ArrayList<String> genres = movie.getGenres();
            int duration = movie.getDuration();

            this.movies.add(new Movie(title, cast, genres, year, duration));
        }

        return this.movies;
    }

    public List<Serial> convertSerial(final List<SerialInputData> serials1) {
        this.serials.clear();

        for (SerialInputData serial : serials1) {
            String title = serial.getTitle();
            int year = serial.getYear();
            ArrayList<String> cast = serial.getCast();
            ArrayList<String> genres = serial.getGenres();
            int numberOfSeasons = serial.getNumberSeason();
            ArrayList<Season> seasons = serial.getSeasons();

            this.serials.add(new Serial(title, cast, genres, numberOfSeasons, seasons, year));
        }

        return this.serials;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public List<Serial> getSeries() {
        return serials;
    }
}
