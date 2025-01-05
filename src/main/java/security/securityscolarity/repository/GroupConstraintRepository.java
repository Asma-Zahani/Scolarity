package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.securityscolarity.entity.GroupConstraint;
import security.securityscolarity.entity.TeacherConstraint;
import security.securityscolarity.entity.University;

import java.util.List;

public interface GroupConstraintRepository extends JpaRepository<GroupConstraint, Long> {
    GroupConstraint findGroupConstraintById(Long id);
    List<GroupConstraint> findGroupConstraintByGroupUniversity(University university);
}