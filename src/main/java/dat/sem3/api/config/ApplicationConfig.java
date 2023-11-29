package dat.sem3.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dat.sem3.persistence.config.HibernateConfig;
import dat.sem3.util.ExceptionSerializer;
import dat.sem3.util.LocalDateSerializer;
import dat.sem3.util.LocalDateTimeSerializer;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.json.JavalinJackson;
import io.javalin.plugin.bundled.RouteOverviewPlugin;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;


@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ApplicationConfig {

    public static void configurations(JavalinConfig config) {
        // logging
        if (System.getenv("DEPLOYED") == null)
            config.plugins.enableDevLogging(); // enables extensive development logging in terminal

        // http
        config.http.defaultContentType = "application/json"; // default content type for requests

        // routing
        config.routing.ignoreTrailingSlashes = true; // removes trailing slashes for all routes

        // Route overview
        config.plugins.register(new RouteOverviewPlugin("/routes")); // enables route overview at /routes

        SimpleModule module = new SimpleModule();
        module.addSerializer(Exception.class, new ExceptionSerializer());
        module.addSerializer(LocalDate.class, new LocalDateSerializer());
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        config.jsonMapper(new JavalinJackson(mapper));
    }

    public static String getProperty(String propName) throws IOException {
        try (InputStream is = HibernateConfig.class.getClassLoader().getResourceAsStream("properties-from-pom.properties")) {
            Properties prop = new Properties();
            prop.load(is);
            return prop.getProperty(propName);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IOException("Could not read property from pom file. Build Maven!");
        }
    }

    public static void startServer(Javalin app, int port) {
        app.updateConfig(ApplicationConfig::configurations);
        app.routes(Routes.getRoutes());
        app.start(port);
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }

}