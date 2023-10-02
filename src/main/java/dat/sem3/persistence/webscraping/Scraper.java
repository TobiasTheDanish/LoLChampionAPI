package dat.sem3.persistence.webscraping;

import dat.sem3.persistence.dto.ChampionDTO;
import dat.sem3.persistence.dto.ChampionDetailDTO;
import dat.sem3.persistence.model.Champion;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Scraper {
    private final String baseURL;
    private final ExecutorService executorService;

    private static Scraper instance = null;

    private Scraper() {
        baseURL = "https://leagueoflegends.fandom.com";
        executorService = Executors.newCachedThreadPool();
    }

    public static Scraper getInstance() {
        if (instance == null) {
            instance = new Scraper();
        }

        return instance;
    }

    public List<Champion> scrapeChampions() throws IOException, ExecutionException, InterruptedException {
        List<ChampionDTO> championDTOS = scrapeChampionDTOS();

        return scrapeChampionDetails(championDTOS);
    }

    protected List<ChampionDTO> scrapeChampionDTOS() throws IOException {
        List<ChampionDTO> championDTOS = new ArrayList<>();
        Document dom = Jsoup.connect(baseURL + "/wiki/List_of_champions").get();

        Elements championRows = dom.select("#mw-content-text > div.mw-parser-output > table > tbody > tr");

        championRows.stream()
                .skip(1)
                .forEach(element -> championDTOS.add(getChampionDTO(element)));
        return championDTOS;
    }

    protected List<Champion> scrapeChampionDetails(List<ChampionDTO> championDTOS) throws ExecutionException, InterruptedException {
        List<Champion> champions = new Vector<>(championDTOS.size());
        int count = (int)Math.ceil(championDTOS.size() / 5.0);
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<ChampionDTO> sublist;
            if (i < 4) {
                sublist = championDTOS.subList(i * count, (i + 1) * count);
            } else {
                sublist = championDTOS.subList(i*count, championDTOS.size());
            }

            futures.add(executorService.submit(new ScraperRunnable(instance, sublist, champions)));
        }

        for (Future<?> future : futures) {
            future.get();
        }

        return champions;
    }

    protected ChampionDetailDTO getChampionDetailDTO(String detailLink) throws IOException {
        Document dom = Jsoup.connect(baseURL + detailLink).get();

        final String[] positions = new String[] {""};
        final String[] rangeType = new String[1];
        final String[] resource = new String[1];
        final String[] adaptiveType = new String[1];

        dom.select("div.pi-item.pi-data").forEach(element -> {
            String dataSource = element.attr("data-source");

            switch (dataSource) {
                case "position" -> {
                    Elements posElements = element.select("div > span[data-tip]");
                    for (int i = 0; i < posElements.size(); i++) {
                        positions[0] += posElements.get(i).attr("data-tip") + (i != posElements.size() - 1 ? "," : "");
                    }
                }
                case "resource" -> {
                    resource[0] = element.select("div > span").first().attr("data-tip");
                }
                case "rangetype" -> {
                    rangeType[0] = element.select("div > span").first().attr("data-tip");
                }
                case "adaptivetype" -> {
                    adaptiveType[0] = element.select("div > span").first().text();
                }
                default -> {
                }
            }
        });

        return ChampionDetailDTO.builder()
                .positions(positions[0])
                .adaptiveType(adaptiveType[0])
                .rangeType(rangeType[0])
                .resource(resource[0])
                .build();
    }

    private ChampionDTO getChampionDTO(Element element) {
        Element nameLink = element.select("td:nth-child(1) > span > span > a").first();
        String name = null, title = null, detailsLink = null;
        if (nameLink != null) {
            detailsLink = nameLink.attr("href");
            String[] nameTitle = nameLink.html().split("<br>\n");
            name = nameTitle[0].replace("&amp;" , "&");
            title = nameTitle[1];
        }

        Element image = element.select("td:nth-child(1) > div > a > img").first();
        String imageSrc = null;

        if (image != null) {
            imageSrc = image.attr("data-src").split("scale-to-width-down")[0];
        }

        String releaseDate = element.select("td:nth-child(3)").text();

        return ChampionDTO.builder()
                .name(name)
                .title(title)
                .detailLink(detailsLink)
                .imageLink(imageSrc)
                .releaseDate(LocalDate.parse(releaseDate))
                .build();
    }
}