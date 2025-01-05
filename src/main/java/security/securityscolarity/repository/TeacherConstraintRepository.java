package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.securityscolarity.entity.Teacher;
import security.securityscolarity.entity.TeacherConstraint;
import security.securityscolarity.entity.University;

import java.util.List;

public interface TeacherConstraintRepository extends JpaRepository<TeacherConstraint, Long> {
    TeacherConstraint findTeacherConstraintById(Long id);
    List<TeacherConstraint> findTeacherConstraintByTeacherUniversity(University teacher_university);
}