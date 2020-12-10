package org.example.app.jpa.h2entity;

import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;
import org.example.app.jpa.SystemConstants;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
abstract class EntityObject implements Serializable {
    @Id
    @SequenceGenerator(name = SystemConstants.SEQUENCE_GENERATOR_NAME,
        initialValue = SystemConstants.SEQUENCE_INITIAL_VALUE,
        sequenceName = SystemConstants.SEQUENCE_NAME,
        allocationSize = SystemConstants.SEQUENCE_ALLOCATION_SIZE)
    @GeneratedValue(generator = SystemConstants.SEQUENCE_GENERATOR_NAME,
        strategy = GenerationType.SEQUENCE)
    private Long id;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !getClass().isAssignableFrom(o.getClass()) || !getClass().isInstance(o)) {
            return false;
        }
        EntityObject that = (EntityObject) o;
        return getId() == null ? false : getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
