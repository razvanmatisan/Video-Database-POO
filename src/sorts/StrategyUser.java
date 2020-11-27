package sorts;

import user.User;

import java.util.List;

/**
 * Class that is specialized in executing a given sorting operation for users.
 */
public final class StrategyUser {
    private final SortUser sortUser;

    public StrategyUser(final SortUser sortUser) {
        this.sortUser = sortUser;
    }

    /**
     * Method that implements a sorting operation for users that was received
     * in constructor.
     */
    public void sort(final String sortType, final List<User> users) {
        sortUser.sort(sortType, users);
    }
}
