package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.securityscolarity.entity.Group;
import security.securityscolarity.entity.Teacher;
import security.securityscolarity.entity.University;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findTeacherById(long id);
    List<Teacher> findByUniversity(University university);
    List<Teacher> findBySchedules_IdGroup(Group schedules_id_group);
    long countByUniversity(University university);
}
