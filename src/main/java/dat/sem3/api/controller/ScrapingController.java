package dat.sem3.api.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dat.sem3.persistence.dao.ChampionDAO;
import dat.sem3.persistence.model.Champion;
import dat.sem3.persistence.webscraping.Scraper;
import io.javalin.http.Context;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScrapingController {
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static final ChampionDAO championDAO = new ChampionDAO("lol_db");
    private static ScrapingResponse mostRecentResponse = new ScrapingResponse();

    public static void beginScrape(Context ctx) {
        if (mostRecentResponse.startTime == null || mostRecentResponse.endTime != null) {
            mostRecentResponse = new ScrapingResponse("Scraping starting.");
            mostRecentResponse.setStatusCode(202);
            ctx.res().setStatus(202);
            ctx.json(mostRecentResponse);
            executorService.submit(() -> {
                try {
                    mostRecentResponse = new ScrapingResponse("Scraping underway.", LocalDateTime.now());
                    List<Champion> championList = Scraper.getInstance().scrapeChampions();
                    mostRecentResponse.setChampionsScraped(championList.size());
                    mostRecentResponse.setMessage("Data persistence underway");
                    championList.forEach(championDAO::save);
                    mostRecentResponse.setEndTime(LocalDateTime.now());
                    mostRecentResponse.setStatusCode(200);
                    mostRecentResponse.setMessage("Scraping and persistence finished");
                } catch (IOException | ExecutionException | InterruptedException e) {
                    mostRecentResponse.setStatusCode(500);
                    mostRecentResponse.setMessage("Error occurred when scraping: " + e.getMessage());
                }
            });
        } else {
            ctx.res().setStatus(202);
            ctx.json("Scraping process already running");
        }
    }

    public static void scrapeInfo(Context ctx) {
        ctx.res().setStatus(mostRecentResponse.getStatusCode());
        ctx.json(mostRecentResponse);
    }
}

@Getter
@Setter
@JsonSerialize(using = ScrapingResponseSerializer.class)
class ScrapingResponse {
    int championsScraped;
    String message;
    LocalDateTime startTime;
    LocalDateTime endTime;
    int statusCode;

    public ScrapingResponse() {
        championsScraped = 0;
        message = "";
        startTime = null;
        endTime = null;
        statusCode = 202;
    }

    public ScrapingResponse(String message) {
        this.message = message;
        championsScraped = 0;
        startTime = null;
        endTime = null;
        statusCode = 202;
    }

    public ScrapingResponse(String message, LocalDateTime startTime) {
        this.message = message;
        this.startTime = startTime;
        championsScraped = 0;
        this.endTime = null;
        statusCode = 202;
    }
}

class ScrapingResponseSerializer extends StdSerializer<ScrapingResponse> {
    protected ScrapingResponseSerializer() {
        super(ScrapingResponse.class);
    }
    protected ScrapingResponseSerializer(Class<ScrapingResponse> t) {
        super(t);
    }

    @Override
    public void serialize(ScrapingResponse scrapingResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("message", scrapingResponse.message);
        jsonGenerator.writeNumberField("championsScraped", scrapingResponse.championsScraped);
        jsonGenerator.writeObjectField("startTime", scrapingResponse.startTime);
        jsonGenerator.writeObjectField("endTime", scrapingResponse.endTime);
        jsonGenerator.writeEndObject();
    }
}