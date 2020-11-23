package action;

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
//    private final ActorDB actorDB;
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
        //this.actorDB = actorDB;
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
            case "favorite":
                message = userHashMap.get(username).favorite(title);

                if (message.equals("Added to Favorites!")) {
                    return fileWriter.writeFile(actionId, "",
                                "success -> " + title + " was added as favourite");
                } else if (message.equals("Already in Favorites!")) {
                    return fileWriter.writeFile(actionId, "",
                            "error -> " + title + " is already in favourite list");
                } else {
                    return fileWriter.writeFile(actionId, "",
                            "error -> " + title + " is not seen");
                }
            case "view":
                User user = userHashMap.get(username);
                user.view(title);
                return fileWriter.writeFile(actionId, "", "success -> " + title
                                + " was viewed with total views of "
                                + user.getHistory().get(title));
            case "rating":
                user = userHashMap.get(username);
                Video video;

                if (seasonNumber == 0) {
                    video = videoDB.findMovie(title);
                } else {
                    video = videoDB.findSerial(title);
                }

                assert video != null;
                message = user.ratingVideo(video, grade, seasonNumber);

                if (message.equals("Added rating!")) {
                    return fileWriter.writeFile(actionId, "",
                            "success -> " + title
                                    + " was rated with " + grade + " by "
                                    + user.getUsername());

                } else if (message.equals("Already rated!")) {
                    return fileWriter.writeFile(actionId, "",
                            "error -> " + title
                                    + " has been already rated");

                } else {
                    return fileWriter.writeFile(actionId, "",
                            "error -> " + title
                                    + " is not seen");
                }
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
