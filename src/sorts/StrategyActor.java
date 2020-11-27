package sorts;

import actor.Actor;

import java.util.List;

/**
 * Class that is specialized in executing a given sorting operation for actors.
 */
public final class StrategyActor {
    private final SortActor sortActor;

    public StrategyActor(final SortActor sortActor) {
        this.sortActor = sortActor;
    }

    /**
     * Method that implements a sorting operation for actors that was received
     * in constructor.
     */
    public void sort(final String sortType, final List<Actor> actors) {
        sortActor.sort(actors, sortType);
    }
}
