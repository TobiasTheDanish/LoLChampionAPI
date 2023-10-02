package dat.sem3.persistence.dao;

import dat.sem3.persistence.model.IEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public abstract class ADataAccessObject<T extends IEntity> implements IDataAccessObject<T>{
    protected EntityManagerFactory emf;

    public ADataAccessObject(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * First checks if the entity parameter is already persisted in JPA, if not persists the entity into JPA
     * @param entity the entity to persist
     * @return entity param with a new id, if entity param is not already persisted.
     * If an entity with a matching id is found then the already persisted entity is returned and no data is persisted.
     * Look at @update if you wish to update data
     */
    @Override
    public T persist(T entity) {
        if (entity.getId() == null || find(entity.getClass(), entity.getId()) == null) {
            try (EntityManager em = emf.createEntityManager()) {
                em.getTransaction().begin();
                em.persist(entity);
                em.getTransaction().commit();
            }
            return entity;
        }
        return find(entity.getClass(), entity.getId());
    }

    /**
     * First checks if the entity parameter is already persisted in JPA, then merges the entity into the JPA entity
     * @param entity the entity to merge
     * @return the merged entity if already persisted. Else the entity parameter.
     */
    @Override
    public T update(T entity) {
        T e = find(entity.getClass(), entity.getId());
        if (e != null) {
            try (EntityManager em = emf.createEntityManager()) {
                em.getTransaction().begin();
                em.merge(entity);
                em.getTransaction().commit();
            }
        }

        return entity;
    }

    /**
     * Tries to find a JPA entity with a matching class and id
     * @param tClass the class of which the entity should belong to
     * @param id the id of the entity to look for
     * @return if found returns an entity of type T, else returns null
     * @param <K> the type of the id
     */
    @Override
    public <K> T find(Class<? extends IEntity> tClass, K id) {
        T res = null;
        try (EntityManager em = emf.createEntityManager()) {
            res = (T) em.find(tClass, id);
        }
        return res;
    }

    /**
     * Removes an entity from JPA
     * @param entity the entity to remove
     */
    @Override
    public void delete(T entity) {
        T e = find(entity.getClass(), entity.getId());
        if (e != null) {
            try (EntityManager em = emf.createEntityManager()) {
                em.getTransaction().begin();
                em.remove(entity);
                em.getTransaction().commit();
            }
        }
    }

    /**
     * Removes an entity from JPA
     * @param tClass the class of which the entity should belong to
     * @param id the id of the entity to look for
     * @param <K> the type of the id
     */
    @Override
    public <K> void delete(Class<? extends IEntity> tClass, K id) {
        T entity = find(tClass, id);
        if (entity != null) {
            try (EntityManager em = emf.createEntityManager()) {
                em.getTransaction().begin();
                em.remove(entity);
                em.getTransaction().commit();
            }
        }
    }
}
