package sorts;

import actor.Actor;

import java.util.List;

/**
 * Interface for sorting actors.
 */
public interface SortActor {
    /**
     * Method that sorts actors depending on sortType.
     */
    void sort(List<Actor> actors, String sortType);
}
