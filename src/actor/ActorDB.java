package actor;

import entertainment.*;

import java.util.*;

public class ActorDB {
    private HashMap<String, Actor> actorHashMap = new HashMap<>();

    public ActorDB(List<Actor> actors) {
        for (Actor actor : actors) this.actorHashMap.put(actor.getName(), actor);
    }

    public HashMap<String, Actor> getActorHashMap() {
        return actorHashMap;
    }

    public void setActorHashMap(HashMap<String, Actor> actorHashMap) {
        this.actorHashMap = actorHashMap;
    }

    /* ////////////////////// Queries for Actor ////////////////////// */

    public void sortActorsByRating(String sortType, List<Actor> actors) {
        switch (sortType) {
            case "asc" -> actors.sort((a1, a2) -> {
                if (a1.getRating().equals(a2.getRating())) {
                    return a1.getName().compareTo(a2.getName());
                }
                return a1.getRating().compareTo(a2.getRating());
            });
            case "desc" -> Collections.sort(actors, (a1, a2) -> {
                if (a2.getRating().equals(a1.getRating())) {
                    return a2.getName().compareTo(a1.getName());
                }
                return a2.getRating().compareTo(a1.getRating());
            });
        }
    }

    public List<String> average(String sortType, int number, VideoDB videoDB) {
        List<Actor> actors = new ArrayList<>();

        HashMap<String, Video> dbMovie = videoDB.getMovieHashMap();
        HashMap<String, Video> dbSerial = videoDB.getSerialHashMap();

        for (Actor actor : actorHashMap.values()) {
            actor.calculateRating(dbMovie, dbSerial);
            if (actor.getRating() != 0) {
                actors.add(actor);
            }
        }

        sortActorsByRating(sortType, actors);

        List<String> copyActors = new ArrayList<>();
        for (int i = 0; i < Math.min(actors.size(), number); i++) {
            copyActors.add(actors.get(i).getName());
        }

        return copyActors;
    }

    public List<String> awards(String sortType, int number, VideoDB videoDB) {
        for (Actor actor : actorHashMap.values()) {
            actor.setNumberAwardsOneActor();
        }
        return new ArrayList<>();
    }

    public List<String> filterDescription(String sortType, int number, VideoDB videoDB) {

        return new ArrayList<>();
    }
}
