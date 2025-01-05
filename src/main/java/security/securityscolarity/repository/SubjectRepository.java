package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import security.securityscolarity.entity.Group;
import security.securityscolarity.entity.Subject;
import security.securityscolarity.entity.Teacher;
import security.securityscolarity.entity.University;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findBySubjectId(Long id);
    List<Subject> findByUniversity(University university);
    @Query("SELECT COUNT(s) FROM Subject s JOIN s.groups g WHERE g.groupId = :groupId")
    int countByGroupId(@Param("groupId") Long groupId);
    @Query("SELECT COUNT(s) FROM Subject s JOIN s.teachers t WHERE t.id = :teacherId")
    int countByTeacherId(@Param("teacherId") Long teacherId);
    List<Subject> findByGroups(Group group);
    List<Subject> findByTeachers(Teacher teacher);
    long countByUniversity(University university);
}
