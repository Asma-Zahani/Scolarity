package security.securityscolarity.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Day implements Serializable, Comparable<Day> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dayId;
    @NonNull
    private String dayName;

    @NonNull
    private Integer dayNumber;

    @Override
    public int compareTo(Day other) {
        return Integer.compare(this.dayNumber, other.dayNumber);
    }
}
