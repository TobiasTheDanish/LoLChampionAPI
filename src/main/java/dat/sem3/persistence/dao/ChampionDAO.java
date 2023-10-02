package dat.sem3.persistence.dao;

import dat.sem3.persistence.config.HibernateConfig;
import dat.sem3.persistence.model.Champion;
import dat.sem3.util.ChampionSortableFields;
import jakarta.persistence.Parameter;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ChampionDAO extends ADataAccessObject<Champion> {
    public ChampionDAO(String dbName) {
        super(HibernateConfig.getEntityManagerFactoryConfig(dbName));
    }

    /**
     * First finds the entity to update, then merges the entities together.
     * @param entity the entity to merge
     * @return the merged entity.
     */
    @Override
    public Champion update(Champion entity) {
        try(var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Champion champion = find(entity.getClass(), entity.getId());
            champion.setName(entity.getName());
            champion.setTitle(entity.getTitle());
            champion.setReleaseDate(entity.getReleaseDate());
            champion.setImageLink(entity.getImageLink());
            champion.setDetails(entity.getDetails());
            em.getTransaction().commit();

            return champion;
        }
    }

    /**
     * A helper function that checks whether a persisted entity has matching ids with the champion param.
     * If a match is found, the JPA entity is updated with the data from the champion param, else a new JPA entity is persisted
     * @param champion the champion entity to persist or update
     * @return the updated or persisted entity
     */
    public Champion save(Champion champion) {
        if (entityExist(champion)) {
            return update(champion);
        } else {
            return persist(champion);
        }
    }

    public boolean entityExist(Champion champion) {
        try (var em = emf.createEntityManager()){
            TypedQuery<Tuple> q = em.createQuery("SELECT COUNT(c) > 0, c.id FROM Champion c WHERE c.name = :name OR c.title = :title GROUP BY c.id", Tuple.class);
            q.setParameter("name", champion.getName());
            q.setParameter("title", champion.getTitle());

            if (!q.getResultList().isEmpty()) {
                Tuple res = q.getResultList().get(0);
                if (res.get(0, Boolean.class)) {
                    champion.setId(res.get(1, String.class));
                }
                return res.get(0, Boolean.class);
            }
            return false;
        }
    }

    public Champion findWithDetails(String id) {
        Champion res = find(Champion.class, id);
        Hibernate.initialize(res);
        return res;
    }

    public Optional<Champion> findByName(String name) {
        try (var em = emf.createEntityManager()){
            TypedQuery<Champion> q = em.createQuery("SELECT c FROM Champion c WHERE LOWER(c.name) = LOWER(:name)", Champion.class);
            q.setParameter("name", name);

            return q.getResultList().stream().findFirst();
        }
    }

    public List<Champion> findAll() {
        try (var em = emf.createEntityManager()){
            em.getTransaction().begin();
            TypedQuery<Champion> q = em.createQuery("SELECT c FROM Champion c ORDER BY c.name", Champion.class);
            em.getTransaction().commit();
            return q.getResultList();
        }
    }

    public Champion findMostRecent() {
        try (var em = emf.createEntityManager()){
            TypedQuery<Champion> q = em.createQuery("SELECT c FROM Champion c ORDER BY c.releaseDate DESC LIMIT 1 ", Champion.class);
            return q.getSingleResult();
        }
    }

    public Champion findRandom() {
        try (var em = emf.createEntityManager()) {
            TypedQuery<Champion> q = em.createQuery("SELECT c FROM Champion c", Champion.class);
            var list = q.getResultList();
            return list.get(new Random().nextInt(list.size()));
        }
    }

    public List<Champion> findAllSorted(ChampionSortableFields sortBy) {
        try (var em = emf.createEntityManager()){
            TypedQuery<Champion> q =
                    switch (sortBy) {
                        case NAME ->
                                em.createQuery("SELECT c FROM Champion c ORDER BY c.name", Champion.class);
                        case TITLE ->
                                em.createQuery("SELECT c FROM Champion c ORDER BY c.title", Champion.class);
                        case RELEASE_DATE ->
                                em.createQuery("SELECT c FROM Champion c ORDER BY c.releaseDate", Champion.class);
                        case POSITIONS ->
                                em.createQuery("SELECT c FROM Champion c ORDER BY c.details.positions", Champion.class);
                        case RESOURCE ->
                                em.createQuery("SELECT c FROM Champion c ORDER BY c.details.resource", Champion.class);
                        case RANGE_TYPE ->
                                em.createQuery("SELECT c FROM Champion c ORDER BY c.details.rangeType", Champion.class);
                        case ADAPTIVE_TYPE ->
                                em.createQuery("SELECT c FROM Champion c ORDER BY c.details.adaptiveType", Champion.class);
                    };
            return q.getResultList();
        }
    }
}