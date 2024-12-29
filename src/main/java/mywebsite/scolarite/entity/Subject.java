package mywebsite.scolarite.entity;

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
    private Set<Teacher> teachers;

    @ManyToMany(mappedBy = "subjects", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @ToString.Exclude
    @JsonIgnore
    private Set<Group> groups;
}
