package security.securityscolarity.service;

import security.securityscolarity.entity.Subject;
import security.securityscolarity.entity.Teacher;
import security.securityscolarity.entity.University;

import java.util.List;
import java.util.Set;

public interface ITeacherService {

    long getTeacherCount();
    List<Teacher> findAll();
    List<Teacher> findTeacherByUniversity(University university);
    List<Teacher> findTeacherByUniversityId(Long universityId);
    Teacher findByTeacherID(Long id);
    Teacher addTeacher(Teacher teacher);
    void deleteTeacher(Long id);
    Teacher updateTeacher(Long id , Teacher teacher);
    List<Teacher> getTeacherNotAssigned();
    void assignSubjectsToTeacher(Set<Subject> subjects, Teacher teacher);
}
