package security.securityscolarity.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RoomConstraint extends BaseConstraint implements Serializable {

    @OneToOne
    @JsonBackReference(value = "constraint-rooms")
    private Room room;

    @ManyToMany
    @JoinTable(
            name = "room_constraint_unavailable_chronoDays",
            joinColumns = @JoinColumn(name = "constraint_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "chrono_id", referencedColumnName = "chrono_id"),
                    @JoinColumn(name = "day_id", referencedColumnName = "day_id")
            }
    )
    private List<ChronoDay> unavailableChronoDays = new ArrayList<>();

    public void initializeUnavailableChronoDays(List<Chrono> chronos, Day day) {
        for (Chrono chrono : chronos) {
            ChronoDayId chronoDayId = new ChronoDayId();
            chronoDayId.setChrono(chrono);
            chronoDayId.setDay(day);

            ChronoDay chronoDay = new ChronoDay(chronoDayId);

            unavailableChronoDays.add(chronoDay);
        }
    }
}
