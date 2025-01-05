package security.securityscolarity.service.IMPL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.*;
import security.securityscolarity.repository.GroupRepository;
import security.securityscolarity.repository.ScheduleRepository;
import security.securityscolarity.repository.SubjectRepository;
import security.securityscolarity.repository.TeacherRepository;
import security.securityscolarity.service.ISubjectService;

import java.util.List;

@Service
public class SubjectService implements ISubjectService{
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private ScheduleRepository scheduleRepository;

    public int getSubjectCountByGroup(Long groupId) {
        return subjectRepository.countByGroupId(groupId);
    }

    public int getSubjectCountByTeacher(Long teacherId) {
        return subjectRepository.countByTeacherId(teacherId);
    }

    public long getSubjectCount() {
        return subjectRepository.count();
    }

    public long countByUniversity(University university) {
        return subjectRepository.countByUniversity(university);
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public List<Subject> findByUniversity(University university) {
        return subjectRepository.findByUniversity(university);
    }

    public List<Subject> findByGroup(Group group) {
        return subjectRepository.findByGroups(group);
    }
    public List<Subject> findByTeacher(Teacher teacher) {
        return subjectRepository.findByTeachers(teacher);
    }

    public List<Subject> findByUniversityId(Long id) {
        return subjectRepository.findByUniversity(universityService.findByUniversityID(id));
    }

    public Subject findBySubjectID(Long id) {
        return subjectRepository.findBySubjectId(id);
    }

    public Subject addSubject(Subject Subject) {
        return subjectRepository.save(Subject);
    }

    public Subject addSubjectByUniversity(Subject subject, Long universityID) {
        subject.setUniversity(universityService.findByUniversityID(universityID));
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findBySubjectId(id);

        subject.getGroups().forEach(group -> group.getSubjects().remove(subject));
        subject.getTeachers().forEach(teacher -> teacher.getSubjects().remove(subject));

        if (subject.getSchedules() != null) {
            scheduleRepository.deleteAll(subject.getSchedules());
        }

        subjectRepository.deleteById(id);
    }

    public Subject updateSubject(Long id , Subject subject) {
        Subject subjectToUpdate = this.findBySubjectID(id);
        subjectToUpdate.setSubjectId(id);
        subjectToUpdate.setSubjectCode(subject.getSubjectCode());
        subjectToUpdate.setSubjectName(subject.getSubjectName());
        subjectToUpdate.setSubjectFullName(subject.getSubjectFullName());
        subjectToUpdate.setSubjectDescription(subject.getSubjectDescription());
        subjectToUpdate.setFraction(subject.getFraction());
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

    public void clearGroupsAndTeachers(Long subjectId) {
        Subject subject = this.findBySubjectID(subjectId);

        subject.getGroups().forEach(group -> group.getSubjects().remove(subject));
        subject.getTeachers().forEach(teacher -> teacher.getSubjects().remove(subject));

        subjectRepository.save(subject);
    }
}
