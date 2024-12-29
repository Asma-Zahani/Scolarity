package mywebsite.scolarite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User implements Serializable {
    private String level;

    @ManyToOne
    @JoinColumn(name = "subgroup_id", nullable = true)
    private SubGroup subGroup;

    @ManyToOne
    @JoinColumn(name = "university_id")
    @JsonBackReference(value = "university-students")
    @ToString.Exclude
    private University university;
}
