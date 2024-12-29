package mywebsite.scolarite.service.IMPL;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import mywebsite.scolarite.entity.Group;
import mywebsite.scolarite.entity.Student;
import mywebsite.scolarite.entity.Subject;
import mywebsite.scolarite.entity.Teacher;
import mywebsite.scolarite.repository.SubjectRepository;
import mywebsite.scolarite.repository.TeacherRepository;
import mywebsite.scolarite.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Teacher findByTeacherID(Long id) {
        return teacherRepository.findById(id).get();
    }

    public Teacher addTeacher(Teacher Teacher) {
        return teacherRepository.save(Teacher);
    }
    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }

    public Teacher updateTeacher(Long id , Teacher teacher) {
        Teacher teacherToUpdate = teacherRepository.findById(id).get();
        teacherToUpdate.setId(id);
        teacherToUpdate.setFirstName(teacher.getFirstName());
        teacherToUpdate.setLastName(teacher.getLastName());
        teacherToUpdate.setEmail(teacher.getEmail());
        teacherToUpdate.setPassword(teacher.getPassword());
        teacherToUpdate.setActive(teacher.isActive());
        teacherToUpdate.setSpecialite(teacher.getSpecialite());
        teacherToUpdate.setUniversity(teacher.getUniversity());
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
