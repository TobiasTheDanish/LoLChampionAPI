package dat.sem3.api.controller;

import dat.sem3.api.exception.UnknownArgument;
import dat.sem3.persistence.dao.ChampionDAO;
import dat.sem3.persistence.model.Champion;
import dat.sem3.util.ChampionSortableFields;
import dat.sem3.api.util.QueryParamParser;
import io.javalin.http.Context;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ChampionController {
    private final static ChampionDAO dao = new ChampionDAO("lol_db");

    public static void getByName(Context ctx) {
        String name = ctx.pathParam("name");
        Optional<Champion> champion = dao.findByName(name);
        if (champion.isEmpty()) {
            ctx.res().setStatus(404);
            ctx.json(new UnknownArgument("No champion with the name '" + name + "' was found. Check for typos and try again"));
        } else {
            ctx.json(champion.get());
        }
    }

    public static void getAllChampions(Context ctx) {
        List<Champion> champions;

        List<String> filterParams = ctx.queryParams("filter");
        String sortParam = ctx.queryParam("sort");
        if (sortParam != null) {
            try {
                String sortBy = sortParam.split(" ")[0];
                String sortDir = "";
                if (sortParam.contains(" ")) {
                    sortDir = sortParam.split(" ")[1];
                }
                var field = ChampionSortableFields.get(sortBy);
                champions = dao.findAllSorted(field);

                if (sortDir.equalsIgnoreCase("desc")) {
                    Collections.reverse(champions);
                }
            } catch (IllegalArgumentException e) {
                ctx.res().setStatus(400);
                ctx.json(e);
                return;
            }
        } else {
            champions = dao.findAll();
        }

        if (!filterParams.isEmpty()) {
            try {
                var predicates = QueryParamParser.parseFilters(filterParams, Champion.class);
                for (Predicate<Champion> predicate : predicates) {
                    champions = champions.stream().filter(predicate).collect(Collectors.toList());
                }
            } catch (RuntimeException e) {
                ctx.res().setStatus(500);
                ctx.json(e);
                return;
            }
        }

        ctx.json(champions);
    }

    public static void getNewestChampion(Context ctx) {
        ctx.json(dao.findMostRecent());
    }

    public static void getRandomChampion(Context ctx) {
        ctx.json(dao.findRandom());
    }
}
