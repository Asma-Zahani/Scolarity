package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.securityscolarity.entity.Student;
import security.securityscolarity.entity.SubGroup;
import security.securityscolarity.entity.Teacher;
import security.securityscolarity.entity.University;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByUniversity(University university);
    List<Student> findBySubGroup_Group_GroupId(Long groupId);
    List<Student> findBySubGroup_Group_GroupIdAndIdNot(Long groupId, Long userId);
    int countBySubGroup_Group_GroupId(Long groupId);
    long countByUniversity(University university);

    int countBySubGroup_Group_GroupIdAndIdNot(Long groupId, Long id);
}
