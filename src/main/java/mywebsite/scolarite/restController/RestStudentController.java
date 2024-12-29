package mywebsite.scolarite.restController;

import mywebsite.scolarite.entity.Student;
import mywebsite.scolarite.service.IMPL.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/students")
public class RestStudentController {

    @Autowired
    StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.findAll();
    }

    @GetMapping("/{studentId}")
    public Student getStudentById(@PathVariable("studentId") Long id) {
        return studentService.findByStudentID(id);
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @DeleteMapping("/{studentId}")
    public String deleteStudent(@PathVariable("studentId") Long id) {
        studentService.deleteStudent(id);
        return "Student with ID " + id + " deleted successfully";
    }

    @PutMapping("/{studentId}")
    public Student updateStudent(@PathVariable("studentId") Long id, @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }
}