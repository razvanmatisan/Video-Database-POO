package action;

import actor.ActorDB;
import entertainment.VideoDB;
import fileio.Writer;
import org.json.simple.JSONObject;
import user.UserDB;

import java.io.IOException;
import java.util.List;

public final class Query implements Action {
    private final VideoDB videoDB;
    private final ActorDB actorDB;
    private final UserDB userDB;

    private final String type;
    private final String objectType;
    private final String sortType;
    private final List<List<String>> filters;
    private final int number;
    private final int actionId;

    public Query(final int actionId, final String type, final String objectType,
                 final String sortType, final List<List<String>> filters, final int number,
                 final VideoDB videoDB, final ActorDB actorDB, final UserDB userDB) {
        this.type = type;
        this.videoDB = videoDB;
        this.actorDB = actorDB;
        this.userDB = userDB;
        this.filters = filters;
        this.objectType = objectType;
        this.sortType = sortType;
        this.number = number;
        this.actionId = actionId;
    }
    @Override
    public JSONObject callAction(final Writer fileWriter) throws IOException {
        switch (type) {
            case "average" -> {
                List<String> sortedActorsByRating = actorDB.average(sortType, number, videoDB);
                return fileWriter.writeFile(actionId, "",
                        "Query result: " + sortedActorsByRating);
            }
            case "awards" -> {
                List<String> sortedActorsByAwards = actorDB.awards(sortType, filters);
                return fileWriter.writeFile(actionId, "",
                        "Query result: " + sortedActorsByAwards);
            }
            case "filter_description" -> {
                List<String> sortedActorsByDescription
                        = actorDB.filterDescription(sortType, filters);
                return fileWriter.writeFile(actionId, "",
                        "Query result: " + sortedActorsByDescription);
            }
            case "ratings" -> {
                List<String> sortedVideosByRating
                        = videoDB.rating(sortType, number, filters, objectType);
                return fileWriter.writeFile(actionId, "",
                        "Query result: " + sortedVideosByRating);
            }
            case "favorite" -> {
                List<String> sortedVideosByFavorite
                        = videoDB.favorite(sortType, number, filters, userDB, objectType);
                return fileWriter.writeFile(actionId, "",
                        "Query result: " + sortedVideosByFavorite);
            }
            case "longest" -> {
                List<String> sortedVideosByLongest =
                        videoDB.longest(sortType, number, filters, objectType);
                return fileWriter.writeFile(actionId, "",
                        "Query result: " + sortedVideosByLongest);
            }
            case "most_viewed" -> {
                List<String> sortedVideosByMostViewed
                        = videoDB.mostViewed(sortType, number, filters, objectType, userDB);
                return fileWriter.writeFile(actionId, "",
                        "Query result: " + sortedVideosByMostViewed);
            }
            case "num_ratings" -> {
                List<String> sortedUsers = userDB.numberOfRatings(sortType, number);
                return fileWriter.writeFile(actionId, "",
                        "Query result: " + sortedUsers);
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
