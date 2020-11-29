package action;

import actor.ActorDB;
import common.Constants;
import entertainment.VideoDB;
import fileio.ActionInputData;
import user.UserDB;

import java.util.List;

/**
 * A Factory Class that generates object of concrete class
 * (Command, Query, Recommendation) based on information from input
 */
public final class ActionFactory {
    private final VideoDB videoDB;
    private final ActorDB actorDB;
    private final UserDB userDB;

    public ActionFactory(final VideoDB videoDB, final ActorDB actorDB, final UserDB userDB) {
        this.videoDB = videoDB;
        this.actorDB = actorDB;
        this.userDB = userDB;
    }

    /**
     * @param action stores the information from input
     * @return Query/Command/Recommendation object
     */
    public Action getAction(final ActionInputData action) {
        String actionType = action.getActionType();
        String type = action.getType();
        String title = action.getTitle();
        String username = action.getUsername();
        String criteria = action.getCriteria();
        String sortType = action.getSortType();
        String objectType = action.getObjectType();
        String genre = action.getGenre();
        List<List<String>> filters = action.getFilters();

        int number = action.getNumber();
        int actionId = action.getActionId();
        double grade = action.getGrade();
        int seasonNumber = action.getSeasonNumber();


        switch (actionType) {
            case (Constants.COMMAND) -> {
                return new Command(actionId, type, title, username, grade, seasonNumber,
                        videoDB, userDB);
            }
            case(Constants.QUERY) -> {
                return new Query(actionId, criteria, objectType, sortType, filters, number,
                        videoDB, actorDB, userDB);
            }
            case(Constants.RECOMMENDATION) -> {
                return new Recommendation(actionId, type, username, genre,
                        videoDB, userDB);
            }
            default -> throw new IllegalStateException("Unexpected value: " + actionType);
        }
    }
}
