package security.securityscolarity.service.IMPL;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.*;
import security.securityscolarity.repository.ScheduleRepository;
import security.securityscolarity.repository.SubjectRepository;
import security.securityscolarity.repository.TeacherRepository;
import security.securityscolarity.service.ITeacherService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class TeacherService implements ITeacherService{

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private ScheduleRepository scheduleRepository;

    public long getTeacherCount() {
        return teacherRepository.count();
    }

    public long countByUniversity(University university) {
        return teacherRepository.countByUniversity(university);
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public List<Teacher> findTeacherByUniversity(University university) {
        return teacherRepository.findByUniversity(university);
    }

    public List<Teacher> findBySchedules_IdGroup(Group group) {
        return teacherRepository.findBySchedules_IdGroup(group);
    }

    public List<Teacher> findTeacherByUniversityId(Long id) {
        return teacherRepository.findByUniversity(universityService.findByUniversityID(id));
    }

    public Teacher findByTeacherID(Long id) {
        return teacherRepository.findById(id).get();
    }

    public Teacher addTeacher(Teacher teacher) {
        teacher.setRoles(Collections.singletonList(Role.TEACHER));
        teacher.setPassword(new BCryptPasswordEncoder().encode(teacher.getPassword()));
        return teacherRepository.save(teacher);
    }

    public  Teacher addTeacherByUniversity(Teacher teacher, Long universityID) {
        teacher.setUniversity(universityService.findByUniversityID(universityID));
        return teacherRepository.save(teacher);
    }

    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id).get();

        teacher.getSubjects().forEach(subject -> subject.getTeachers().remove(teacher));

        if (teacher.getSchedules() != null) {
            scheduleRepository.deleteAll(teacher.getSchedules());
        }
        teacherRepository.deleteById(id);
    }

    public Teacher updateTeacher(Long id , Teacher teacher) {
        Teacher teacherToUpdate = teacherRepository.findTeacherById(id);
        teacherToUpdate.setFirstName(teacher.getFirstName());
        teacherToUpdate.setLastName(teacher.getLastName());
        teacherToUpdate.setEmail(teacher.getEmail());
        if (teacher.getPassword() != null && !teacher.getPassword().isEmpty()) {
            String encodedPassword = new BCryptPasswordEncoder().encode(teacher.getPassword());
            teacherToUpdate.setPassword(encodedPassword);
        }
        teacherToUpdate.setActive(teacher.isActive());
        teacherToUpdate.setSpecialite(teacher.getSpecialite());
        teacherToUpdate.setSubjects(teacher.getSubjects());
        return teacherRepository.save(teacherToUpdate);
    }

    public List<Teacher> getTeacherNotAssigned() {
        String sqlQuery = "SELECT * FROM `user` WHERE dtype = \"Teacher\" and university_id is null";
        Query query = entityManager.createNativeQuery(sqlQuery, Teacher.class);
        return query.getResultList();
    }

    public void assignSubjectsToTeacher(Set<Subject> subjects, Teacher teacher) {
        for (Subject subject : subjects) {
            subject.getTeachers().add(teacher);
            teacher.getSubjects().add(subject);
        }
        teacherRepository.save(teacher);
        subjectRepository.saveAll(subjects);
    }
}
