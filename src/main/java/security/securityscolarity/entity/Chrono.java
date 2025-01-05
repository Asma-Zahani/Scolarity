package security.securityscolarity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalTime;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Chrono implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chronoId;

    @NonNull
    private String chronoName;
    @NonNull
    private LocalTime startTime;
    @NonNull
    private LocalTime endTime;
    private String chronoDescription;

    @ManyToOne
    @JoinColumn(name = "university_id")
    @JsonIgnore
    @ToString.Exclude
    private University university;
}
