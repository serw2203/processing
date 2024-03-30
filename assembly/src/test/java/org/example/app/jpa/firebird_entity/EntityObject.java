package org.example.app.jpa.firebird_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@SequenceGenerator(name = "A_SEQUENCE_GEN", sequenceName = "ENTITY_ID_GEN", allocationSize = 1)
abstract class EntityObject implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "A_SEQUENCE_GEN")
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || !this.getClass().isAssignableFrom(o.getClass()) || !this.getClass().isInstance(o)) {
            return false;
        }

        EntityObject that = (EntityObject) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }
}
