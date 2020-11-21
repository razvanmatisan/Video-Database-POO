package user;

import java.util.*;

public class UserDB {
    private HashMap<String, User> userHashMap = new HashMap<>();

    public UserDB(List<User> users) {
        for (User user : users) this.userHashMap.put(user.getUsername(), user);
    }

    public HashMap<String, User> getUserHashMap() {
        return userHashMap;
    }

    public void setUserHashMap(HashMap<String, User> userHashMap) {
        this.userHashMap = userHashMap;
    }

    /* ////////////////////// Query User ////////////////////// */

    public void sortUsersByRatings(String sortType, List<User> users) {
        switch (sortType) {
            case "asc" -> users.sort((u1, u2) -> {
                if (u1.getNumberGivenRatings().equals(u2.getNumberGivenRatings())) {
                    return u1.getUsername().compareTo(u2.getUsername());
                }
                return u1.getNumberGivenRatings().compareTo(u2.getNumberGivenRatings());
            });
            case "desc" -> users.sort((u1, u2) -> {
                if (u2.getNumberGivenRatings().equals(u1.getNumberGivenRatings())) {
                    return u2.getUsername().compareTo(u1.getUsername());
                }
                return u2.getNumberGivenRatings().compareTo(u1.getNumberGivenRatings());
            });
        }
    }

    public List<String> numberOfRatings(String sortType, int number) {
        List<User> users = new ArrayList<>();

        for (User user : userHashMap.values()) {
            if (user.getNumberGivenRatings() != 0) {
                users.add(user);
            }
        }

        sortUsersByRatings(sortType, users);

        List<String> copyUsers = new ArrayList<>();
        for (int i = 0; i < Math.min(users.size(), number); i++) {
            copyUsers.add(users.get(i).getUsername());
        }

        return copyUsers;
    }
}
