package security.securityscolarity.restController;

import security.securityscolarity.entity.Teacher;
import security.securityscolarity.service.IMPL.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/teachers")
public class RestTeacherController {

    @Autowired
    TeacherService teacherService;

    @GetMapping("/count")
    public Long getTeacherCount() {
        return teacherService.getTeacherCount();
    }

    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.findAll();
    }

    @GetMapping("/university/{universityId}")
    public List<Teacher> getTeachersByUniversity(@PathVariable("universityId") Long id) {
        return teacherService.findTeacherByUniversityId(id);
    }

    @GetMapping("/{teacherId}")
    public Teacher getTeacherById(@PathVariable("teacherId") Long id) {
        return teacherService.findByTeacherID(id);
    }

    @PostMapping
    public Teacher addTeacher(@RequestBody Teacher teacher) {
        return teacherService.addTeacher(teacher);
    }

    @PostMapping("/{universityId}")
    public Teacher addTeacherByUniversity(@PathVariable("universityId") Long id, @RequestBody Teacher teacher) {
        return teacherService.addTeacherByUniversity(teacher,id);
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

    @GetMapping("/notAssigned")
    public List<Teacher> getTeacherNotAssignedToUniversity() {
        return teacherService.getTeacherNotAssigned();
    }
}
