package main;

import actor.Actor;
import actor.ActorDB;
import checker.Checker;
import checker.Checkstyle;
import common.Constants;
import entertainment.*;
import fileio.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import user.User;
import user.UserDB;
import utils.Conversion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        List<ActionInputData> commands = input.getCommands();
        List<UserInputData> users = input.getUsers();
        List<ActorInputData> actors = input.getActors();
        List<MovieInputData> movies = input.getMovies();
        List<SerialInputData> series = input.getSerials();

        Conversion conversion = new Conversion();

        List<User> myUsers = conversion.convertUser(users);
        List<Actor> myActors = conversion.convertActor(actors);
        List<Movie> myMovies = conversion.convertMovie(movies);
        List<Serial> mySeries = conversion.convertSerial(series);

        UserDB userDB = new UserDB(myUsers);
        HashMap<String, User> dbUser = userDB.getUserHashMap();

        ActorDB actorDB = new ActorDB(myActors);
        HashMap<String, Actor> dbActor = actorDB.getActorHashMap();

        VideoDB videoDB = new VideoDB(myMovies, mySeries);
        videoDB.setIndexDatabase();
        HashMap<String, Video> dbMovie = videoDB.getMovieHashMap();
        HashMap<String, Video> dbSerial = videoDB.getSerialHashMap();

        for (ActionInputData command : commands) {
            String username = command.getUsername();
            String type = command.getType();
            switch(command.getActionType()) {
                case "command":
                    String title = command.getTitle();
                    switch (command.getType()) {
                        case "favorite":
                            if (dbUser.containsKey(username)) {
                                String message = dbUser.get(username).favorite(title);
                                JSONObject out;
                                if (message.equals("Added to Favorites!")) {
                                    out = fileWriter.writeFile(command.getActionId(), "",
                                            "success -> " + command.getTitle() + " was added as favourite");
                                } else if (message.equals("Already in Favorites!")) {
                                    out = fileWriter.writeFile(command.getActionId(), "",
                                            "error -> " + command.getTitle() + " is already in favourite list");
                                } else {
                                    out = fileWriter.writeFile(command.getActionId(), "",
                                            "error -> " + command.getTitle() + " is not seen");
                                }
                                arrayResult.add(out);
                            }
                            break;
                        case "view":
                            if (dbUser.containsKey(username)) {
                                User user = dbUser.get(username);
                                user.view(title);
                                JSONObject out = fileWriter.writeFile(command.getActionId(), "",
                                        "success -> " + command.getTitle() +
                                                " was viewed with total views of " + user.getHistory().get(title));
                                arrayResult.add(out);
                            }
                            break;
                        case "rating":
                            if (dbUser.containsKey(username)) {
                                User user = dbUser.get(username);
                                Video video;

                                if (command.getSeasonNumber() == 0) {
                                    video = dbMovie.get(title);
                                } else {
                                    video = dbSerial.get(title);
                                }

                                JSONObject out;
                                String message = user.ratingVideo(video, command.getGrade(), command.getSeasonNumber());

                                if (message.equals("Added rating!")) {
                                    out = fileWriter.writeFile(command.getActionId(), "",
                                            "success -> " + command.getTitle() +
                                                    " was rated with " + command.getGrade() + " by " +
                                                    user.getUsername());

                                } else if (message.equals("Already rated!")) {
                                    out = fileWriter.writeFile(command.getActionId(), "",
                                            "error -> " + command.getTitle() +
                                                    " has been already rated");

                                } else {
                                    out = fileWriter.writeFile(command.getActionId(), "",
                                            "error -> " + command.getTitle() +
                                                    " is not seen");
                                }

                                arrayResult.add(out);
                            }
                            break;
                    }
                    break;
                case "query":
                    String criteria = command.getCriteria();
                    String sortType = command.getSortType();
                    List<List<String>> filters = command.getFilters();
                    String objectType = command.getObjectType();
                    int number = command.getNumber();

                    JSONObject out;

                    switch (criteria) {
                        case "average" -> {
                            List<String> sortedActorsByRating = actorDB.average(sortType, number, videoDB);
                            out = fileWriter.writeFile(command.getActionId(), "",
                                    "Query result: " + sortedActorsByRating);
                            arrayResult.add(out);
                        }
                        case "awards" -> {
                            List<String> sortedActorsByAwards = actorDB.awards(sortType, number, videoDB);
                            out = fileWriter.writeFile(command.getActionId(), "",
                                    "Query result: " + sortedActorsByAwards);
                            arrayResult.add(out);
                        }
                        case "filter_description" -> {
                            List<String> sortedActorsByDescription = actorDB.filterDescription(sortType, number, videoDB);
                            out = fileWriter.writeFile(command.getActionId(), "",
                                    "Query result: " + sortedActorsByDescription);
                            arrayResult.add(out);
                        }
                        case "ratings" -> {
                            List<String> sortedVideosByRating = videoDB.rating(sortType, number, filters, objectType);
                            out = fileWriter.writeFile(command.getActionId(), "",
                                    "Query result: " + sortedVideosByRating);
                            arrayResult.add(out);
                        }
                        case "favorite" -> {
                            List<String> sortedVideosByFavorite = videoDB.favorite(sortType, number,
                                    filters, userDB, objectType);
                            out = fileWriter.writeFile(command.getActionId(), "",
                                    "Query result: " + sortedVideosByFavorite);
                            arrayResult.add(out);
                        }
                        case "longest" -> {
                            List<String> sortedVideosByLongest = videoDB.longest(sortType, number, filters, objectType);
                            out = fileWriter.writeFile(command.getActionId(), "",
                                    "Query result: " + sortedVideosByLongest);
                            arrayResult.add(out);
                        }
                        case "most_viewed" -> {
                            List<String> sortedVideosByMostViewed = videoDB.mostViewed(sortType, number, filters,
                                    objectType, userDB);
                            out = fileWriter.writeFile(command.getActionId(), "",
                                    "Query result: " + sortedVideosByMostViewed);
                            arrayResult.add(out);
                        }
                        case "num_ratings" -> {
                            List<String> sortedUsers = userDB.numberOfRatings(sortType, number);
                            out = fileWriter.writeFile(command.getActionId(), "",
                                    "Query result: " + sortedUsers);
                            arrayResult.add(out);
                        }
                    }
                    break;
                case "recommendation":
                    User user = dbUser.get(username);
                    switch(type) {
                        case "standard":
                            String standardRecommendation = user.standard(videoDB);
                            if (!standardRecommendation.equals("")) {
                                out = fileWriter.writeFile(command.getActionId(), "",
                                        "StandardRecommendation result: " + standardRecommendation);
                            } else {
                                out = fileWriter.writeFile(command.getActionId(), "",
                                        "StandardRecommendation cannot be applied!");
                            }

                            arrayResult.add(out);
                            break;

                        case "best_unseen":
                            String bestRatedUnseenRecommendation = user.bestUnseen(videoDB);
                            out = fileWriter.writeFile(command.getActionId(), "",
                                    "BestRatedUnseenRecommendation result: " + bestRatedUnseenRecommendation);
                            arrayResult.add(out);
                            break;
                        case "popular":
                            String popularRecommendation = user.popular(videoDB, userDB);
                            if (!popularRecommendation.equals("")) {
                                out = fileWriter.writeFile(command.getActionId(), "",
                                        "PopularRecommendation result: " + popularRecommendation);
                            } else {
                                out = fileWriter.writeFile(command.getActionId(), "",
                                        "PopularRecommendation cannot be applied!");
                            }
                            arrayResult.add(out);
                            break;
                        case "favorite":
                            String favoriteRecommendation = user.favoriteRecommendation(videoDB, userDB);
                            if (!favoriteRecommendation.equals("")) {
                                out = fileWriter.writeFile(command.getActionId(), "",
                                        "FavoriteRecommendation result: " + favoriteRecommendation);
                            } else {
                                    out = fileWriter.writeFile(command.getActionId(), "",
                                            "FavoriteRecommendation cannot be applied!");
                            }
                            arrayResult.add(out);
                            break;
                        case "search":
                            String genre = command.getGenre();
                            List<String> searchRecommendation = user.search(genre, userDB, videoDB);
                            if (!searchRecommendation.isEmpty()) {
                                out = fileWriter.writeFile(command.getActionId(), "",
                                        "SearchRecommendation result: " + searchRecommendation);
                            } else {
                                out = fileWriter.writeFile(command.getActionId(), "",
                                        "SearchRecommendation cannot be applied!");
                            }
                            arrayResult.add(out);
                            break;
                    }
                    break;
            }
        }

        fileWriter.closeJSON(arrayResult);
    }
}
