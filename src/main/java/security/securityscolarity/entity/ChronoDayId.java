package security.securityscolarity.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChronoDayId implements Serializable {

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "chrono_id", nullable = false)
    private Chrono chrono;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "day_id", nullable = false)
    private Day day;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChronoDayId that = (ChronoDayId) o;
        return Objects.equals(chrono, that.chrono) && Objects.equals(day, that.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chrono, day);
    }
}
