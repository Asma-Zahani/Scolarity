package security.securityscolarity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Subject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectId;

    @NonNull
    private String subjectCode;
    @NonNull
    private String subjectName;
    private String subjectFullName;
    private String subjectDescription;
    private int fraction = 1;
    @NonNull
    private String session;

    @ManyToMany(mappedBy = "subjects", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @ToString.Exclude
    @JsonIgnore
    private Set<Teacher> teachers = new HashSet<>();

    @ManyToMany(mappedBy = "subjects", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @ToString.Exclude
    private Set<Group> groups = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "university_id")
    @JsonIgnore
    @ToString.Exclude
    private University university;

    @OneToMany(mappedBy = "id.subject", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<Schedule> schedules = new ArrayList<>();
}