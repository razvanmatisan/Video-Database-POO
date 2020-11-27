package main;

import action.Action;
import action.ActionFactory;
import actor.Actor;
import actor.ActorDB;
import checker.Checker;
import checker.Checkstyle;
import common.Constants;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.VideoDB;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import fileio.Writer;
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
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
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
        ActorDB actorDB = new ActorDB(myActors);
        VideoDB videoDB = new VideoDB(myMovies, mySeries);

        videoDB.setIndexDatabase();

        ActionFactory actionFactory = new ActionFactory(videoDB, actorDB, userDB);

        for (ActionInputData command : commands) {
            Action action = actionFactory.getAction(command);
            JSONObject out = action.callAction(fileWriter);

            // noinspection unchecked
            arrayResult.add(out);
        }

        fileWriter.closeJSON(arrayResult);
    }
}
