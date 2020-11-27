package action;

import common.Constants;
import entertainment.Video;
import entertainment.VideoDB;
import fileio.Writer;
import org.json.simple.JSONObject;
import user.User;
import user.UserDB;

import java.io.IOException;
import java.util.HashMap;

public final class Command implements Action {
    private final VideoDB videoDB;
    private final UserDB userDB;

    private final String type;
    private final int actionId;
    private final String title;
    private final String username;
    private final double grade;
    private final int seasonNumber;

    public Command(final int actionId, final String type, final String title,
                   final String username, final double grade, final int seasonNumber,
                   final VideoDB videoDB, final UserDB userDB) {
        this.type = type;
        this.videoDB = videoDB;
        this.userDB = userDB;
        this.title = title;
        this.actionId = actionId;
        this.username = username;
        this.grade = grade;
        this.seasonNumber = seasonNumber;
    }

    @Override
    public JSONObject callAction(final Writer fileWriter) throws IOException {
        HashMap<String, User> userHashMap = userDB.getUserHashMap();
        String message;
        switch (type) {
            case Constants.FAVORITE:
                message = userHashMap.get(username).favorite(title);

                if (message.equals(Constants.ADDED_FAVORITES)) {
                    return fileWriter.writeFile(actionId, "",
                                Constants.SUCCES + title + " was added as favourite");
                } else if (message.equals(Constants.ALREADY_FAVORITES)) {
                    return fileWriter.writeFile(actionId, "",
                            Constants.ERROR + title + " is already in favourite list");
                } else {
                    return fileWriter.writeFile(actionId, "",
                            Constants.ERROR + title + " is not seen");
                }
            case Constants.VIEW:
                User user = userHashMap.get(username);
                user.view(title);
                return fileWriter.writeFile(actionId, "", Constants.SUCCES + title
                                + " was viewed with total views of "
                                + user.getHistory().get(title));
            case Constants.RATING:
                user = userHashMap.get(username);
                Video video;

                if (seasonNumber == 0) {
                    video = videoDB.findMovie(title);
                } else {
                    video = videoDB.findSerial(title);
                }

                assert video != null;
                message = user.ratingVideo(video, grade, seasonNumber);

                if (message.equals(Constants.ADDED_RATING)) {
                    return fileWriter.writeFile(actionId, "",
                            Constants.SUCCES + title
                                    + " was rated with " + grade + " by "
                                    + user.getUsername());

                } else if (message.equals(Constants.ALREADY_RATED)) {
                    return fileWriter.writeFile(actionId, "",
                            Constants.ERROR + title
                                    + " has been already rated");

                } else {
                    return fileWriter.writeFile(actionId, "",
                            Constants.ERROR + title
                                    + " is not seen");
                }
            default:
                throw new IllegalStateException(Constants.UNEXPECTED_VALUE + type);
        }
    }
}
