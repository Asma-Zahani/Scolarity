package security.securityscolarity.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class SubGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subGroupId;

    @NonNull
    private String subGroupName;
    private String subGroupDescription;
    @NonNull
    private Integer learnersCount;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference(value = "group-subGroups")
    private Group group;

    @OneToMany(mappedBy = "subGroup", cascade = CascadeType.ALL, orphanRemoval = false)
    @ToString.Exclude
    private Set<Student> students;
}
