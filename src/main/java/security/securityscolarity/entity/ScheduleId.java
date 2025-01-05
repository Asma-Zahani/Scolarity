package security.securityscolarity.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
}

