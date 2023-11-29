package dat.sem3.api.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dat.sem3.api.config.ApplicationConfig;
import dat.sem3.api.controller.ChampionController;
import dat.sem3.api.controller.ScrapingController;
import dat.sem3.persistence.config.HibernateConfig;
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
        app = Javalin.create();
    }

    @Override
    public void setup() {
    }

    @Override
    public void start() {
        HibernateConfig.setTest(false);
        ApplicationConfig.startServer(app, 7070);
    }

    @Override
    public void start(int port) {
        HibernateConfig.setTest(false);
        ApplicationConfig.startServer(app, port);
    }
}
