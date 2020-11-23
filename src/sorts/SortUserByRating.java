package sorts;

import user.User;

import java.util.List;

public final class SortUserByRating implements SortUser {

    @Override
    public void sort(final String sortType, final List<User> users) {
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
            default -> throw new IllegalStateException("Unexpected value: " + sortType);
        }
    }
}
