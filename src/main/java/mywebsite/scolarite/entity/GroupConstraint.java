package mywebsite.scolarite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
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

    private Integer maxDaysPerWeek;
    private Integer maxDailyGaps;
    private Integer maxWeeklyGaps;
    private Integer maxDailyHours;
    private Integer minDailyHours;
    private Integer maxContinuousHours;
    private Integer maxHoursInInterval;
    private Integer maxDailyAmplitude;
    private Integer maxStartHour;


    @ManyToMany
    @JoinTable(
            name = "group_constraint_unavailable_days",
            joinColumns = @JoinColumn(name = "constraint_id"),
            inverseJoinColumns = @JoinColumn(name = "day_id")
    )
    private List<Day> unavailableDays;

    private Integer minRestHours;
}
