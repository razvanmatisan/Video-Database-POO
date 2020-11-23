package actor;

import entertainment.Video;
import entertainment.VideoDB;
import sorts.SortActorByAwards;
import sorts.SortActorByDescription;
import sorts.SortActorByRating;
import sorts.StrategyActor;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ActorDB {
    private final HashMap<String, Actor> actorHashMap = new HashMap<>();

    public ActorDB(final List<Actor> actors) {
        for (Actor actor : actors) {
            this.actorHashMap.put(actor.getName(), actor);
        }
    }

    /* ////////////////////// Queries for Actor ////////////////////// */
    /* /////////// 1. Average /////////// */
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

        StrategyActor strategy = new StrategyActor(new SortActorByRating());
        strategy.sort(sortType, actors);

        List<String> copyActors = new ArrayList<>();
        for (int i = 0; i < Math.min(actors.size(), number); i++) {
            copyActors.add(actors.get(i).getName());
        }

        return copyActors;
    }

    /* /////////// 2. Awards /////////// */
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

        StrategyActor strategy = new StrategyActor(new SortActorByAwards());
        strategy.sort(sortType, actors);

        List<String> copyActors = new ArrayList<>();
        for (Actor actor : actors) {
            copyActors.add(actor.getName());
        }

        return copyActors;
    }

    /* /////////// 3. Filter Description /////////// */
    private void filterActorsByDescription(final List<Actor> actors, final List<String> words) {
        for (Actor actor : actorHashMap.values()) {
            int ok = 1;
            String description = actor.getCareerDescription();
            for (String word : words) {
                String regex = "[ .,!?'(-]" + word + "[ ,.!?')-]";
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
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
    }
    public List<String> filterDescription(final String sortType, final List<List<String>> filters) {
        List<Actor> actors = new ArrayList<>();

        List<String> words = filters.get(2);
        filterActorsByDescription(actors, words);

        StrategyActor strategy = new StrategyActor(new SortActorByDescription());
        strategy.sort(sortType, actors);

        List<String> copyActors = new ArrayList<>();

        for (Actor actor : actors) {
            copyActors.add(actor.getName());
        }

        return copyActors;
    }
}
