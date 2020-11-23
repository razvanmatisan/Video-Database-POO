package sorts;

import actor.Actor;

import java.util.List;

public final class SortActorByRating implements SortActor {

    @Override
    public void sort(final List<Actor> actors, final String sortType) {
        switch (sortType) {
            case "asc" -> actors.sort((a1, a2) -> {
                if (a1.getRating().equals(a2.getRating())) {
                    return a1.getName().compareTo(a2.getName());
                }
                return a1.getRating().compareTo(a2.getRating());
            });
            case "desc" -> actors.sort((a1, a2) -> {
                if (a2.getRating().equals(a1.getRating())) {
                    return a2.getName().compareTo(a1.getName());
                }
                return a2.getRating().compareTo(a1.getRating());
            });
            default -> throw new IllegalStateException("Unexpected value: " + sortType);
        }
    }
}
