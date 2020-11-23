package sorts;

import user.User;

import java.util.List;

public final class StrategyUser {
    private final SortUser sortUser;

    public StrategyUser(final SortUser sortUser) {
        this.sortUser = sortUser;
    }

    public void sort(final String sortType, final List<User> users) {
        sortUser.sort(sortType, users);
    }
}
