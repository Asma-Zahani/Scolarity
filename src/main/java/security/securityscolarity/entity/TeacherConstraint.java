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
public class TeacherConstraint extends BaseConstraint implements Serializable {

    @OneToOne
    @JsonBackReference(value = "constraint-teachers")
    private Teacher teacher;

    private Integer maxDaysPerWeek = 6;
    private Integer maxDaysPerWeekWeight = 100;

    private Integer minDaysPerWeek = 1;
    private Integer minDaysPerWeekWeight = 100;

    private Integer maxConsecutiveDays = 2;
    private Integer maxConsecutiveDaysWeight = 100;

    private Integer maxDailyGaps = 1;
    private Integer maxDailyGapsWeight = 100;

    private Integer maxWeeklyGaps = 3;
    private Integer maxWeeklyGapsWeight = 100;

    private Integer maxDailyHours = 6;
    private Integer maxDailyHoursWeight = 100;

    private Integer minDailyHours = 2;
    private Integer minDailyHoursWeight = 100;

    private Integer maxDailyAmplitude = 6;
    private Integer maxDailyAmplitudeWeight = 100;

    private Integer maxContinuousHours = 4;
    private Integer maxContinuousHoursWeight = 100;

    private Integer minRestHours = 1;
    private Integer minRestHoursWeight = 100;

    @ManyToMany
    @JoinTable(
            name = "teacher_constraint_unavailable_chronoDays",
            joinColumns = @JoinColumn(name = "constraint_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "chrono_id", referencedColumnName = "chrono_id"),
                    @JoinColumn(name = "day_id", referencedColumnName = "day_id")
            }
    )
    private List<ChronoDay> unavailableChronoDays = new ArrayList<>();

    @ManyToMany
    private List<Day> unavailableDays = new ArrayList<>();

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
