package dat.sem3.api.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dat.sem3.api.controller.ChampionController;
import dat.sem3.api.controller.ScrapingController;
import dat.sem3.util.ExceptionSerializer;
import dat.sem3.util.LocalDateSerializer;
import dat.sem3.util.LocalDateTimeSerializer;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static io.javalin.apibuilder.ApiBuilder.*;

public class JavalinRestAPI implements IRestApplication{
    private final Javalin app;

    public JavalinRestAPI() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Exception.class, new ExceptionSerializer());
        module.addSerializer(LocalDate.class, new LocalDateSerializer());
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson(mapper));
        });
    }

    @Override
    public void setup() {
        app.get("/", ctx -> {
            ctx.result("Hello world");
        });

        app.routes(() -> {
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
        });
    }

    @Override
    public void start() {
        app.start(80);
    }

    @Override
    public void start(int port) {
        app.start(port);
    }
}
