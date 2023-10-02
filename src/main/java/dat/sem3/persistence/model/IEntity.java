package dat.sem3.persistence.model;

import java.io.Serializable;

public interface IEntity<K extends Serializable> {
    K getId();
}
