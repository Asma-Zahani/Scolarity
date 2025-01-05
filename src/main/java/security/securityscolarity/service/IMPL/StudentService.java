package security.securityscolarity.service.IMPL;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.Role;
import security.securityscolarity.entity.Student;
import security.securityscolarity.entity.Teacher;
import security.securityscolarity.entity.University;
import security.securityscolarity.repository.StudentRepository;
import security.securityscolarity.service.IStudentService;

import java.util.Collections;
import java.util.List;

@Service
public class StudentService implements IStudentService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    private UniversityService universityService;

    public long getStudentCount() {
        return studentRepository.count();
    }

    public long countByUniversity(University university) {
        return studentRepository.countByUniversity(university);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public List<Student> findStudentByUniversity(University university) {
        return studentRepository.findByUniversity(university);
    }

    public List<Student> findStudentByUniversityId(Long id) {
        return studentRepository.findByUniversity(universityService.findByUniversityID(id));
    }

    public Student findByStudentID(Long id) {
        return studentRepository.findById(id).get();
    }

    public Student addStudent(Student student) {
        student.setRoles(Collections.singletonList(Role.STUDENT));
        student.setPassword(new BCryptPasswordEncoder().encode(student.getPassword()));
        return studentRepository.save(student);
    }
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Student updateStudent(Long id , Student student) {
        Student studentToUpdate = studentRepository.findById(id).get();
        studentToUpdate.setId(id);
        studentToUpdate.setFirstName(student.getFirstName());
        studentToUpdate.setLastName(student.getLastName());
        studentToUpdate.setEmail(student.getEmail());
        studentToUpdate.setPassword(new BCryptPasswordEncoder().encode(student.getPassword()));
        studentToUpdate.setActive(student.isActive());
        studentToUpdate.setLevel(student.getLevel());
        studentToUpdate.setSubGroup(student.getSubGroup());
        return studentRepository.save(studentToUpdate);
    }

    public List<Student> getStudentNotAssignedToUniversity() {
        String sqlQuery = "SELECT * FROM `user` WHERE dtype = \"Student\" and university_id is null";
        Query query = entityManager.createNativeQuery(sqlQuery, Student.class);
        return query.getResultList();
    }

    public List<Student> getStudentNotAssignedToSubGroupWithUniversity(University university) {
        String sqlQuery = "SELECT * FROM `user` WHERE dtype = \"Student\" AND subgroup_id IS NULL AND university_id = ?";
        Query query = entityManager.createNativeQuery(sqlQuery, Student.class);
        query.setParameter(1, university.getUniversityId());
        return query.getResultList();
    }

    public List<Student> getStudentNotAssignedToSubGroupWithUniversityId(Long id) {
        String sqlQuery = "SELECT * FROM `user` WHERE dtype = \"Student\" AND subgroup_id IS NULL AND university_id = ?";
        Query query = entityManager.createNativeQuery(sqlQuery, Student.class);
        query.setParameter(1, universityService.findByUniversityID(id).getUniversityId());
        return query.getResultList();
    }

    public List<Student> findStudentsByGroup(Long groupId) {
        return studentRepository.findBySubGroup_Group_GroupId(groupId);
    }

    public int countStudentsByGroup(Long groupId) {
        return studentRepository.countBySubGroup_Group_GroupId(groupId);
    }
}
