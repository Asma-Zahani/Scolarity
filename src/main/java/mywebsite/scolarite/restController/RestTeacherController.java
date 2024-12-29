package mywebsite.scolarite.restController;

import mywebsite.scolarite.entity.Teacher;
import mywebsite.scolarite.service.IMPL.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/teachers")
public class RestTeacherController {

    @Autowired
    TeacherService teacherService;

    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.findAll();
    }

    @GetMapping("/{teacherId}")
    public Teacher getTeacherById(@PathVariable("teacherId") Long id) {
        return teacherService.findByTeacherID(id);
    }

    @PostMapping
    public Teacher addTeacher(@RequestBody Teacher teacher) {
        return teacherService.addTeacher(teacher);
    }

    @DeleteMapping("/{teacherId}")
    public String deleteTeacher(@PathVariable("teacherId") Long id) {
        teacherService.deleteTeacher(id);
        return "Teacher with ID " + id + " deleted successfully";
    }

    @PutMapping("/{teacherId}")
    public Teacher updateTeacher(@PathVariable("teacherId") Long id, @RequestBody Teacher teacher) {
        return teacherService.updateTeacher(id, teacher);
    }
}
