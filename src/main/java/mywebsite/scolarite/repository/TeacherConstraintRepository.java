package mywebsite.scolarite.repository;

import mywebsite.scolarite.entity.Teacher;
import mywebsite.scolarite.entity.TeacherConstraint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherConstraintRepository extends JpaRepository<TeacherConstraint, Long> {
    TeacherConstraint findTeacherConstraintByTeacher(Teacher teacher);
}