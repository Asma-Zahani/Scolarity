package mywebsite.scolarite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @NonNull
    private String roomName;
    @NonNull
    private Integer capacity;
    private String roomDescription;

    @ManyToOne
    @JoinColumn(name = "building_id")
    @ToString.Exclude
    private Building building;

    @OneToOne(mappedBy = "room", cascade = CascadeType.ALL)
    @JoinColumn(name = "constraint_id")
    @JsonManagedReference(value = "constraint-rooms")
    private RoomConstraint constraint;
}
