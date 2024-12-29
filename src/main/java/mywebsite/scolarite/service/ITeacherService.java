package mywebsite.scolarite.service;

import mywebsite.scolarite.entity.Teacher;

import java.util.List;

public interface ITeacherService {

    List<Teacher> findAll();
    Teacher findByTeacherID(Long id);
    Teacher addTeacher(Teacher teacher);
    void deleteTeacher(Long id);
    Teacher updateTeacher(Long id , Teacher teacher);
    List<Teacher> getTeacherNotAssigned();
}
