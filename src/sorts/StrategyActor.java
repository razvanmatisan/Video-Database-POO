package sorts;

import actor.Actor;

import java.util.List;

public final class StrategyActor {
    private final SortActor sortActor;

    public StrategyActor(final SortActor sortActor) {
        this.sortActor = sortActor;
    }

    public void sort(final String sortType, final List<Actor> actors) {
        sortActor.sort(actors, sortType);
    }
}
