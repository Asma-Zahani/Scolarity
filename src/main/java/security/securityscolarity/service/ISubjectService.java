package security.securityscolarity.service;

import security.securityscolarity.entity.Subject;
import security.securityscolarity.entity.University;

import java.util.List;

public interface ISubjectService {

    long getSubjectCount();
    List<Subject> findAll();
    List<Subject> findByUniversity(University university);
    List<Subject> findByUniversityId(Long universityId);
    Subject findBySubjectID(Long id);
    Subject addSubject(Subject subject);
    void deleteSubject(Long id);
    Subject updateSubject(Long id , Subject subject);
    void assignTeacher(Long subjectId, Long teacherId);
    void assignGroup(Long subjectId, Long groupId);
}
