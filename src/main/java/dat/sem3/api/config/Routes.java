package dat.sem3.api.config;

import dat.sem3.api.controller.ChampionController;
import dat.sem3.api.controller.ScrapingController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {
    public static EndpointGroup getRoutes() {
        return () -> {
            path("api", () -> {
                path("champions", () -> {
                    get(ChampionController::getAllChampions);
                    path("newest", () -> {
                        get(ChampionController::getNewestChampion);
                    });
                    path("random", () -> {
                        get(ChampionController::getRandomChampion);
                    });
                    path("{name}", () -> {
                        get(ChampionController::getByName);
                    });
                });
            });

            path("scrape", () -> {
                get(ScrapingController::beginScrape);
                path("info", () -> {
                    get(ScrapingController::scrapeInfo);
                });
            });
        };
    }
}
