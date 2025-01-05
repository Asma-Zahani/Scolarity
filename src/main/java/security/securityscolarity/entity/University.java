package security.securityscolarity.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class University implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long universityId;
    @NonNull
    private String universityName;
    private String universityDescription;

    @OneToMany(mappedBy = "university", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = false)
    @JsonManagedReference(value = "university-teachers")
    @ToString.Exclude
    private Set<Teacher> teachers;

    @OneToMany(mappedBy = "university", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = false)
    @JsonManagedReference(value = "university-students")
    @ToString.Exclude
    private Set<Student> students;

    @OneToOne(mappedBy = "university")
    @JsonManagedReference(value = "university-admin")
    @ToString.Exclude
    private UniversityAdmin universityAdmin;

    @OneToMany(mappedBy = "university", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = false)
    private Set<Building> building;

    @OneToMany(mappedBy = "university", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = false)
    @JsonIgnore
    private Set<Subject> subjects;

    @OneToMany(mappedBy = "university", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = false)
    @JsonIgnore
    private Set<Group> groups;

    @OneToMany(mappedBy = "university", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = false)
    @JsonIgnore
    private Set<Chrono> chronos;
}
