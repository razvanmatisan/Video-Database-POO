package user;

import entertainment.Video;
import sorts.SortUserByRating;
import sorts.StrategyUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UserDB {
    /**
     * Database that stores all users.
     */
    private final HashMap<String, User> userHashMap = new HashMap<>();

    public UserDB(final List<User> users) {
        for (User user : users) {
            this.userHashMap.put(user.getUsername(), user);
        }
    }

    public HashMap<String, User> getUserHashMap() {
        return userHashMap;
    }

    /**
     * Method that sets the number of views for a given video.
     */
    public void setNumberViewsVideo(final Video video) {
        Integer numberOfViews = 0;
        String title = video.getTitle();

        for (User user : userHashMap.values()) {
            Map<String, Integer> history = user.getHistory();
            if (history.containsKey(title)) {
                numberOfViews += history.get(title);
            }
        }

        video.setNumberViews(numberOfViews);
    }

    /**
     * Method that sets the number of appearances in favorite videos
     * for a given video.
     */
    public void setNumberFavoritesVideo(final Video video) {
        int numberOfFavorites = 0;

        for (User user : userHashMap.values()) {
            List<String> favoriteVideos = user.getFavoriteVideos();
            for (String favorite : favoriteVideos) {
                if (video.getTitle().equals(favorite)) {
                    numberOfFavorites++;
                }
            }
        }

        video.setNumberOfFavorites(numberOfFavorites);
    }

    /**
     * Method that returns the first "number" users that are sorted by
     * the number of given ratings.
     */
    public List<String> numberOfRatings(final String sortType, final int number) {
        List<User> users = new ArrayList<>();

        for (User user : userHashMap.values()) {
            if (user.getNumberGivenRatings() != 0) {
                users.add(user);
            }
        }

        StrategyUser strategy = new StrategyUser(new SortUserByRating());
        strategy.sort(sortType, users);

        List<String> copyUsers = new ArrayList<>();
        for (int i = 0; i < Math.min(users.size(), number); i++) {
            copyUsers.add(users.get(i).getUsername());
        }

        return copyUsers;
    }
}
