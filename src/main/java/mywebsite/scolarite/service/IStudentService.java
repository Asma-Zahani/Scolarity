package mywebsite.scolarite.service;

import mywebsite.scolarite.entity.Student;
import mywebsite.scolarite.entity.Teacher;

import java.util.List;

public interface IStudentService {

    List<Student> findAll();
    Student findByStudentID(Long id);
    Student addStudent(Student student);
    void deleteStudent(Long id);
    Student updateStudent(Long id , Student student);
    List<Student> getStudentNotAssignedToUniversity();
    List<Student> getStudentNotAssignedToSubGroup();
}
