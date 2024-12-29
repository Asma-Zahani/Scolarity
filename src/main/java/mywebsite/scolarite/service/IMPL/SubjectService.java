package mywebsite.scolarite.service.IMPL;

import mywebsite.scolarite.entity.Group;
import mywebsite.scolarite.entity.Subject;
import mywebsite.scolarite.entity.Teacher;
import mywebsite.scolarite.repository.GroupRepository;
import mywebsite.scolarite.repository.SubjectRepository;
import mywebsite.scolarite.repository.TeacherRepository;
import mywebsite.scolarite.service.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SubjectService implements ISubjectService{
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private GroupRepository groupRepository;

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Subject findBySubjectID(Long id) {
        return subjectRepository.findBySubjectId(id);
    }

    public Subject addSubject(Subject Subject) {
        return subjectRepository.save(Subject);
    }
    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findBySubjectId(id);

        subject.getGroups().forEach(group -> group.getSubjects().remove(subject));
        subject.getTeachers().forEach(teacher -> teacher.getSubjects().remove(subject));

        subjectRepository.deleteById(id);
    }

    public Subject updateSubject(Long id , Subject subject) {
        Subject subjectToUpdate = this.findBySubjectID(id);
        subjectToUpdate.setSubjectId(id);
        subjectToUpdate.setSubjectName(subject.getSubjectName());
        subjectToUpdate.setSubjectFullName(subject.getSubjectFullName());
        subjectToUpdate.setSubjectDescription(subject.getSubjectDescription());
        return subjectRepository.save(subjectToUpdate);
    }

    @Override
    public void assignTeacher(Long subjectId, Long teacherId) {
        Subject subject = subjectRepository.findBySubjectId(subjectId);
        Teacher teacher = teacherRepository.findById(teacherId).get();

        subject.getTeachers().add(teacher);
        teacher.getSubjects().add(subject);

        subjectRepository.save(subject);
    }

    @Override
    public void assignGroup(Long subjectId, Long groupId) {
        Subject subject = subjectRepository.findBySubjectId(subjectId);
        Group group = groupRepository.findByGroupId(groupId);

        subject.getGroups().add(group);
        group.getSubjects().add(subject);

        subjectRepository.save(subject);
    }
}
