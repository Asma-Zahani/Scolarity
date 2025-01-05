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
            name = "room_constraint_unavailable_days",
            joinColumns = @JoinColumn(name = "constraint_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "chrono_id", referencedColumnName = "chrono_id"),
                    @JoinColumn(name = "day_id", referencedColumnName = "day_id")
            }
    )
    private List<ChronoDay> unavailableDays = new ArrayList<>();
}
