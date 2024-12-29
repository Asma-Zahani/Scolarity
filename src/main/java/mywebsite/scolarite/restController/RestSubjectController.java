package mywebsite.scolarite.restController;

import mywebsite.scolarite.entity.Subject;
import mywebsite.scolarite.service.IMPL.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/subjects")
public class RestSubjectController {

    @Autowired
    SubjectService subjectService;

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectService.findAll();
    }

    @GetMapping("/{subjectId}")
    public Subject getSubjectById(@PathVariable("subjectId") Long id) {
        return subjectService.findBySubjectID(id);
    }

    @PostMapping
    public Subject addSubject(@RequestBody Subject subject) {
        return subjectService.addSubject(subject);
    }

    @DeleteMapping("/{subjectId}")
    public String deleteSubject(@PathVariable("subjectId") Long id) {
        subjectService.deleteSubject(id);
        return "Subject with ID " + id + " deleted successfully";
    }

    @PutMapping("/{subjectId}")
    public Subject updateSubject(@PathVariable("subjectId") Long id, @RequestBody Subject subject) {
        return subjectService.updateSubject(id, subject);
    }
}