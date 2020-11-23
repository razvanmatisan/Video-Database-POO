package action;

import actor.ActorDB;
import entertainment.VideoDB;
import fileio.Writer;
import org.json.simple.JSONObject;
import user.User;
import user.UserDB;

import java.io.IOException;
import java.util.List;

public final class Recommendation implements Action {
    private final VideoDB videoDB;
    private final ActorDB actorDB;
    private final UserDB userDB;
    private final String type;

    private final String username;
    private final int actionId;
    private final String genre;

    public Recommendation(final int actionId, final String type,
                          final String username, final String genre,
                          final VideoDB videoDB, final ActorDB actorDB, final UserDB userDB) {
        this.type = type;
        this.videoDB = videoDB;
        this.actorDB = actorDB;
        this.userDB = userDB;
        this.username = username;
        this.actionId = actionId;
        this.genre = genre;
    }

    @Override
    public JSONObject callAction(final Writer fileWriter) throws IOException {
        User user = userDB.getUserHashMap().get(username);
        switch (type) {
            case "standard" -> {
                String standardRecommendation = user.standard(videoDB);
                if (!standardRecommendation.equals("")) {
                    return fileWriter.writeFile(actionId, "",
                            "StandardRecommendation result: " + standardRecommendation);
                } else {
                    return fileWriter.writeFile(actionId, "",
                            "StandardRecommendation cannot be applied!");
                }
            }
            case "best_unseen" -> {
                String bestRatedUnseenRecommendation = user.bestUnseen(videoDB);
                if (!bestRatedUnseenRecommendation.equals("")) {
                    return fileWriter.writeFile(actionId, "",
                            "BestRatedUnseenRecommendation result: "
                                    + bestRatedUnseenRecommendation);
                } else {
                    return fileWriter.writeFile(actionId, "",
                            "BestRatedUnseenRecommendation cannot be applied!");
                }
            }
            case "popular" -> {
                String popularRecommendation = user.popular(videoDB, userDB);
                if (!popularRecommendation.equals("")) {
                    return fileWriter.writeFile(actionId, "",
                            "PopularRecommendation result: " + popularRecommendation);
                } else {
                    return fileWriter.writeFile(actionId, "",
                            "PopularRecommendation cannot be applied!");
                }
            }
            case "favorite" -> {
                String favoriteRecommendation = user.favoriteRecommendation(videoDB, userDB);
                if (!favoriteRecommendation.equals("")) {
                    return fileWriter.writeFile(actionId, "",
                            "FavoriteRecommendation result: " + favoriteRecommendation);
                } else {
                    return fileWriter.writeFile(actionId, "",
                            "FavoriteRecommendation cannot be applied!");
                }
            }
            case "search" -> {
                List<String> searchRecommendation = user.search(genre, userDB, videoDB);
                if (!searchRecommendation.isEmpty()) {
                    return fileWriter.writeFile(actionId, "",
                            "SearchRecommendation result: " + searchRecommendation);
                } else {
                    return fileWriter.writeFile(actionId, "",
                            "SearchRecommendation cannot be applied!");
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
