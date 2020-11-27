package sorts;

import actor.Actor;

import java.util.List;

/**
 * Class that implements the operation of sorting actors by their awards
 */
public final class SortActorByAwards implements SortActor {
    @Override
    public void sort(final List<Actor> actors, final String sortType) {
        switch (sortType) {
            case "asc" -> actors.sort((a1, a2) -> {
                if (a1.getNumberAwards().equals(a2.getNumberAwards())) {
                    return a1.getName().compareTo(a2.getName());
                }
                return a1.getNumberAwards().compareTo(a2.getNumberAwards());
            });
            case "desc" -> actors.sort((a1, a2) -> {
                if (a2.getNumberAwards().equals(a1.getNumberAwards())) {
                    return a2.getName().compareTo(a1.getName());
                }
                return a2.getNumberAwards().compareTo(a1.getNumberAwards());
            });
            default -> throw new IllegalStateException("Unexpected value: " + sortType);
        }
    }
}
