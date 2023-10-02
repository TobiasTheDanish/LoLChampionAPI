package dat.sem3.persistence.webscraping;

import dat.sem3.persistence.dto.ChampionDTO;
import dat.sem3.persistence.dto.ChampionDetailDTO;
import dat.sem3.persistence.model.Champion;

import java.io.IOException;
import java.util.List;

public class ScraperRunnable implements Runnable {
    private final Scraper scraper;
    private final List<ChampionDTO> list;
    private final List<Champion> resList;

    public ScraperRunnable(Scraper scraper, List<ChampionDTO> list, List<Champion> resList) {
        this.scraper = scraper;
        this.list = list;
        this.resList = resList;
    }

    @Override
    public void run() {
        list.forEach(championDTO -> {
            try {
                ChampionDetailDTO championDetailDTO = scraper.getChampionDetailDTO(championDTO.getDetailLink());
                resList.add(new Champion(championDTO, championDetailDTO));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
