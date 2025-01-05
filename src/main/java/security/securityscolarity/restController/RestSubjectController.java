package security.securityscolarity.restController;

import security.securityscolarity.entity.Building;
import security.securityscolarity.entity.Group;
import security.securityscolarity.entity.Subject;
import security.securityscolarity.service.IMPL.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/subjects")
public class RestSubjectController {

    @Autowired
    SubjectService subjectService;

    @GetMapping("/count")
    public Long getSubjectCount() {
        return subjectService.getSubjectCount();
    }

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectService.findAll();
    }

    @GetMapping("/university/{universityId}")
    public List<Subject> getSubjectsByUniversity(@PathVariable("universityId") Long id) {
        return subjectService.findByUniversityId(id);
    }

    @GetMapping("/{subjectId}")
    public Subject getSubjectById(@PathVariable("subjectId") Long id) {
        return subjectService.findBySubjectID(id);
    }

    @PostMapping
    public Subject addSubject(@RequestBody Subject subject) {
        return subjectService.addSubject(subject);
    }

    @PostMapping("/{universityId}")
    public Subject addSubjectByUniversity(@PathVariable("universityId") Long id, @RequestBody Subject subject) {
        return subjectService.addSubjectByUniversity(subject,id);
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

    @PutMapping("teacher/{subjectId}/{teacherId}")
    public String assignTeacher(@PathVariable("subjectId") Long subjectId,
                                 @PathVariable("teacherId") Long teacherId) {
        subjectService.assignTeacher(subjectId, teacherId);
        return "Subject Assigned to Teacher";
    }

    @PutMapping("group/{subjectId}/{groupId}")
    public String assignGroup(@PathVariable("subjectId") Long subjectId,
                                @PathVariable("groupId") Long groupId) {
        subjectService.assignGroup(subjectId, groupId);
        return "Subject Assigned to Group";
    }
}