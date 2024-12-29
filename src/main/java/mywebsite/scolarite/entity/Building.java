package mywebsite.scolarite.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Building implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long buildingId;

    @NonNull
    private String buildingName;
    private String buildingDescription;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="building")
    @ToString.Exclude
    @JsonIgnore
    private Set<Room> rooms;
}
