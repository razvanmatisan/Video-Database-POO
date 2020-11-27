package sorts;

import user.User;

import java.util.List;

/**
 * Interface for sorting users.
 */
public interface SortUser {
    /**
     * Method that sorts users depending on sortType.
     */
    void sort(String sortType, List<User> users);
}
