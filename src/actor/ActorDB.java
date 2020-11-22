package actor;

import entertainment.Video;
import entertainment.VideoDB;
import utils.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActorDB {
    private final HashMap<String, Actor> actorHashMap = new HashMap<>();

    public ActorDB(final List<Actor> actors) {
        for (Actor actor : actors) this.actorHashMap.put(actor.getName(), actor);
    }

    public HashMap<String, Actor> getActorHashMap() {
        return actorHashMap;
    }

    /* ////////////////////// Queries for Actor ////////////////////// */

    private void sortActorsByRating(final String sortType, final List<Actor> actors) {
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
        }
    }

    public List<String> average(final String sortType, final int number, final VideoDB videoDB) {
        List<Actor> actors = new ArrayList<>();

        List<Video> movies = videoDB.getMovies();
        List<Video> serials = videoDB.getSerials();

        for (Actor actor : actorHashMap.values()) {
            actor.calculateRating(movies, serials);
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

    private void sortActorsByAwards(final String sortType, final List<Actor> actors) {
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
        }
    }

    public List<String> awards(final String sortType, final List<List<String>> filters) {
        List<ActorsAwards> filterActorAwards = new ArrayList<>();
        List<Actor> actors = new ArrayList<>();

        List<String> awards = filters.get(3);

        for (String award : awards) {
            filterActorAwards.add(Utils.stringToAwards(award));
        }

        for (Actor actor : actorHashMap.values()) {
            List<ActorsAwards> actorAwards = new ArrayList<>(actor.getAwards().keySet());

            if (actorAwards.containsAll(filterActorAwards)) {
                actor.setNumberAwardsOneActor();
                actors.add(actor);
            }
        }

        sortActorsByAwards(sortType, actors);

        List<String> copyActors = new ArrayList<>();
        for (Actor actor : actors) {
            copyActors.add(actor.getName());
        }

        return copyActors;
    }

    public List<String> filterDescription(final String sortType, final List<List<String>> filters) {
        List<Actor> actors = new ArrayList<>();

        List<String> words = filters.get(2);

        for (Actor actor : actorHashMap.values()) {
            int ok = 1;
            String description = actor.getCareerDescription();
            for (String word : words) {
                Pattern pattern = Pattern.compile("[ .,!?'(-]" + word + "[ ,.!?')-]", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(description);
                if (!matcher.find()) {
                    ok = 0;
                    break;
                }
            }
            if (ok == 1) {
                actors.add(actor);
            }
        }

        switch (sortType) {
            case "asc" -> actors.sort(Comparator.comparing(Actor::getName));
            case "desc" -> actors.sort((a1, a2) -> a2.getName().compareTo(a1.getName()));
        }

        List<String> copyActors = new ArrayList<>();

        for (Actor actor : actors) {
            copyActors.add(actor.getName());
        }

        return copyActors;
    }
}
