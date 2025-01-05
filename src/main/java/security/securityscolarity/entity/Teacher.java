package security.securityscolarity.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
public class Teacher extends User implements Serializable {
    @NonNull
    @Enumerated(EnumType.STRING)
    private Specialite specialite;

    @ManyToOne
    @JoinColumn(name = "university_id")
    @JsonBackReference(value = "university-teachers")
    @ToString.Exclude
    private University university;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "teacher_subjects",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @ToString.Exclude
    private Set<Subject> subjects = new HashSet<>();

    @OneToOne(mappedBy = "teacher", cascade = CascadeType.ALL)
    @JoinColumn(name = "constraint_id")
    @JsonManagedReference(value = "constraint-teachers")
    private TeacherConstraint constraint;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "teacher-schedules")
    @ToString.Exclude
    private List<Schedule> schedules;
}
