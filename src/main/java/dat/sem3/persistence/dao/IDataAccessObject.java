package dat.sem3.persistence.dao;

import dat.sem3.persistence.model.IEntity;

public interface IDataAccessObject<T extends IEntity> {
    T persist(T entity);
    T update(T entity);
    <K> T find(Class<? extends IEntity> tClass, K id);
    void delete(T entity);
    <K> void delete(Class<? extends IEntity> tClass, K id);
}
