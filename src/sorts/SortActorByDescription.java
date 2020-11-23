package sorts;

import actor.Actor;

import java.util.Comparator;
import java.util.List;

public final class SortActorByDescription implements SortActor {
    @Override
    public void sort(final List<Actor> actors, final String sortType) {
        switch (sortType) {
            case "asc" -> actors.sort(Comparator.comparing(Actor::getName));
            case "desc" -> actors.sort((a1, a2) -> a2.getName().compareTo(a1.getName()));
            default -> throw new IllegalStateException("Unexpected value: " + sortType);
        }
    }
}
