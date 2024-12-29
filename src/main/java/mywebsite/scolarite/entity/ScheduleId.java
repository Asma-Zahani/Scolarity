package mywebsite.scolarite.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ScheduleId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "chrono_id", nullable = false)
    private Chrono chrono;

    @ManyToOne
    @JoinColumn(name = "day_id", nullable = false)
    private Day day;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

}

