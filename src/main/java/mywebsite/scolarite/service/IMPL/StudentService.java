package mywebsite.scolarite.service.IMPL;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import mywebsite.scolarite.entity.Student;
import mywebsite.scolarite.repository.StudentRepository;
import mywebsite.scolarite.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService implements IStudentService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    StudentRepository studentRepository;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findByStudentID(Long id) {
        return studentRepository.findById(id).get();
    }

    public Student addStudent(Student Student) {
        return studentRepository.save(Student);
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
        studentToUpdate.setPassword(student.getPassword());
        studentToUpdate.setActive(student.isActive());
        studentToUpdate.setLevel(student.getLevel());
        studentToUpdate.setSubGroup(student.getSubGroup());
        studentToUpdate.setUniversity(student.getUniversity());
        return studentRepository.save(studentToUpdate);
    }

    public List<Student> getStudentNotAssignedToUniversity() {
        String sqlQuery = "SELECT * FROM `user` WHERE dtype = \"Student\" and university_id is null";
        Query query = entityManager.createNativeQuery(sqlQuery, Student.class);
        return query.getResultList();
    }

    public List<Student> getStudentNotAssignedToSubGroup() {
        String sqlQuery = "SELECT * FROM `user` WHERE dtype = \"Student\" and subgroup_id is null";
        Query query = entityManager.createNativeQuery(sqlQuery, Student.class);
        return query.getResultList();
    }
}
