package dat.sem3.api.controller;

import dat.sem3.api.config.ApplicationConfig;
import dat.sem3.persistence.config.HibernateConfig;
import dat.sem3.persistence.dto.ChampionDTO;
import dat.sem3.persistence.dto.ChampionDetailDTO;
import dat.sem3.persistence.model.Champion;
import dat.sem3.persistence.model.ChampionDetails;
import dat.sem3.persistence.webscraping.Scraper;
import io.javalin.Javalin;
import io.javalin.http.Header;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class ChampionControllerTest {
    private static Javalin app;
    private static final String BASE_URL = "http://localhost:7777/api";
    private static EntityManagerFactory emfTest;
    Champion c1, c2;

    @BeforeAll
    static void beforeAll()
    {
        HibernateConfig.setTest(true);
        emfTest = HibernateConfig.getEntityManagerFactory();
        app = Javalin.create();
        ApplicationConfig.startServer(app, 7777);
        RestAssured.baseURI = BASE_URL;
    }

    @BeforeEach
    void beforeEach() {
        ChampionDTO dto1 = new ChampionDTO("Aatrox", "the darkin blade", "somelink", "somelink", LocalDate.of(2013, 7, 10));
        ChampionDetailDTO cdDto1 = new ChampionDetailDTO("Middle", "Mana", "Ranged", "Adaptive");
        ChampionDTO dto2 = new ChampionDTO("Vex", "the sad teenager", "somelink", "somelink", LocalDate.of(2020, 7, 10));
        ChampionDetailDTO cdDto2 = new ChampionDetailDTO("Middle", "Mana", "Ranged", "Adaptive");

        try(EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            c1 = new Champion(dto1, cdDto1);
            c2 = new Champion(dto2, cdDto2);
            em.persist(c1);
            em.persist(c2);
            em.getTransaction().commit();
        }
    }

    @AfterEach
    void afterEach() {
        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM ChampionDetails cd").executeUpdate();
            em.createQuery("DELETE FROM Champion c").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @AfterAll
    static void tearDown()
    {
        HibernateConfig.setTest(false);
        ApplicationConfig.stopServer(app);
    }

    @Test
    void getByName() {
        ChampionDetails details = given()
                .accept("application/json")
                .when()
                .get("/champions/Aatrox")
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("name", equalTo("Aatrox"))
                .body("title", equalTo("the darkin blade"))
                .extract().body().jsonPath().getObject("details", ChampionDetails.class);

        assertThat(details.getRangeType(), equalTo("Ranged"));
    }

    @Test
    void getAllChampions() {
        List<Champion> champions = given()
                .accept("application/json")
                .when()
                .get("/champions")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("");

        assertThat(champions.size(), equalTo(2));
    }

    @Test
    void getNewestChampion() {
        ChampionDetails details = given()
                .accept("application/json")
                .when()
                .get("/champions/newest")
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("name", equalTo("Vex"))
                .body("title", equalTo("the sad teenager"))
                .extract().body().jsonPath().getObject("details", ChampionDetails.class);

        assertThat(details.getRangeType(), equalTo("Ranged"));
    }
}