package mywebsite.scolarite.service;

import mywebsite.scolarite.entity.Subject;

import java.util.List;

public interface ISubjectService {

    List<Subject> findAll();
    Subject findBySubjectID(Long id);
    Subject addSubject(Subject subject);
    void deleteSubject(Long id);
    Subject updateSubject(Long id , Subject subject);
    void assignTeacher(Long subjectId, Long teacherId);
    void assignGroup(Long subjectId, Long groupId);
}
