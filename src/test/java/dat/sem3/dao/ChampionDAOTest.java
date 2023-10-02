package dat.sem3.dao;

import dat.sem3.persistence.config.HibernateConfig;
import dat.sem3.persistence.dao.ChampionDAO;
import dat.sem3.persistence.model.Champion;
import dat.sem3.persistence.webscraping.Scraper;
import jakarta.persistence.Query;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class ChampionDAOTest {
    ChampionDAO dao = new ChampionDAO("lol_db");
    static List<Champion> champs;

    @BeforeAll
    static void startUp() {
        try {
            champs = Scraper.getInstance().scrapeChampions();
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setup() throws IOException {
        assertNotNull(champs);
    }

    @AfterEach
    void tearDown() {
        var emf = HibernateConfig.getEntityManagerFactoryConfig("lol_db");

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("DELETE * FROM public.champions");
            q.executeUpdate();

            q = em.createNativeQuery("DELETE * FROM public.champion_details");
            q.executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    void persist() {
        assertDoesNotThrow(() -> {
            champs.forEach(champion -> dao.persist(champion));

            assertNotNull(champs.get(10).getId());
        });
    }

    @Test
    void update() {
        assertDoesNotThrow(() -> {
            champs.forEach(champion -> {
                dao.persist(champion);
                champion.setReleaseDate(champion.getReleaseDate().plusDays(1));
            });

            assertNotNull(champs.get(10).getId());
        });
    }

    @Test
    void find() {
    }

    @Test
    void delete() {
    }

    @Test
    void testDelete() {
    }

    @Test
    void findWithDetails() {
    }
}