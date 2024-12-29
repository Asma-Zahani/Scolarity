package mywebsite.scolarite.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

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
}
