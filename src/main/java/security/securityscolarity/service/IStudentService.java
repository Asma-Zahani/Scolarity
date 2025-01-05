package security.securityscolarity.service;

import security.securityscolarity.entity.Student;
import security.securityscolarity.entity.University;

import java.util.List;

public interface IStudentService {

    long getStudentCount();
    List<Student> findAll();
    List<Student> findStudentByUniversity(University university);
    List<Student> findStudentByUniversityId(Long universityId);
    Student findByStudentID(Long id);
    Student addStudent(Student student);
    void deleteStudent(Long id);
    Student updateStudent(Long id , Student student);
    List<Student> getStudentNotAssignedToUniversity();
    List<Student> getStudentNotAssignedToSubGroupWithUniversity(University university);
    List<Student> getStudentNotAssignedToSubGroupWithUniversityId(Long universityId);
}
