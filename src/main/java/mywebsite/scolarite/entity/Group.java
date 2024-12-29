package mywebsite.scolarite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "`group`")
public class Group implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;
    @NonNull
    private String groupName;
    private String groupDescription;
    @NonNull
    private Integer learnersCount;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Department department;

    @NonNull
    private String groupYear;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonManagedReference
    private Set<SubGroup> subGroups;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "group_subject",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @ToString.Exclude
    @JsonBackReference
    private Set<Subject> subjects = new HashSet<>();

    @OneToOne(mappedBy = "group", cascade = CascadeType.ALL)
    @JoinColumn(name = "constraint_id")
    @JsonManagedReference(value = "constraint-groups")
    private GroupConstraint constraint;
}
