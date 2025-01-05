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
public class GroupConstraint extends BaseConstraint implements Serializable {

    @OneToOne
    @JsonBackReference(value = "constraint-groups")
    private Group group;

    private Integer maxDaysPerWeek = 6;
    private Integer maxDaysPerWeekWeight = 100;

    private Integer maxDailyGaps = 1;
    private Integer maxDailyGapsWeight = 100;

    private Integer maxWeeklyGaps = 3;
    private Integer maxWeeklyGapsWeight = 100;

    private Integer maxDailyHours = 6;
    private Integer maxDailyHoursWeight = 100;

    private Integer minDailyHours = 2;
    private Integer minDailyHoursWeight = 100;

    private Integer maxContinuousHours = 4;
    private Integer maxContinuousHoursWeight = 100;

    private Integer maxDailyAmplitude = 6;
    private Integer maxDailyAmplitudeWeight = 100;

    private Integer minRestHours = 8;
    private Integer minRestHoursWeight = 100;

    @ManyToMany
    @JoinTable(
            name = "group_constraint_unavailable_days",
            joinColumns = @JoinColumn(name = "constraint_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "chrono_id", referencedColumnName = "chrono_id"),
                    @JoinColumn(name = "day_id", referencedColumnName = "day_id")
            }
    )
    private List<ChronoDay> unavailableDays = new ArrayList<>();

}
