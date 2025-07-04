package security.securityscolarity.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChronoDay implements Serializable {

    @EmbeddedId
    private ChronoDayId id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChronoDay chronoDay = (ChronoDay) o;
        return Objects.equals(id, chronoDay.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}