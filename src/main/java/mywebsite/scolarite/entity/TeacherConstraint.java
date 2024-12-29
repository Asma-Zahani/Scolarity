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
public class TeacherConstraint extends BaseConstraint implements Serializable {

    @OneToOne
    @JsonBackReference(value = "constraint-teachers")
    private Teacher teacher;

    private Integer maxDaysPerWeek;
    private Integer minDaysPerWeek;
    private Integer maxConsecutiveDays;
    private Integer maxDailyGaps;
    private Integer maxWeeklyGaps;
    private Integer maxDailyHours;
    private Integer minDailyHours;
    private Integer maxHoursInInterval; // Intervalle spécifique.
    private Integer maxDailyAmplitude; // Début-fin d'une journée.
    private Integer maxContinuousHours;
    private Integer minGapsBetweenActivities;

    private Integer minRestHours;

    @ManyToMany
    @JoinTable(
            name = "teacher_constraint_unavailable_days",
            joinColumns = @JoinColumn(name = "constraint_id"),
            inverseJoinColumns = @JoinColumn(name = "day_id")
    )
    private List<Day> unavailableDays;

}