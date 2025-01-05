package security.securityscolarity.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import security.securityscolarity.entity.TeacherConstraint;
import security.securityscolarity.service.IMPL.TeacherConstraintService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/teacher-constraints")
public class RestTeacherConstraintController {

    @Autowired
    TeacherConstraintService teacherConstraintService;

    @GetMapping
    public List<TeacherConstraint> getAllTeacherConstraints() {
        return teacherConstraintService.findAll();
    }

    @GetMapping("/{constraintId}")
    public TeacherConstraint getTeacherConstraintById(@PathVariable("constraintId") Long id) {
        return teacherConstraintService.findByTeacherConstraintID(id);
    }

    @GetMapping("/university/{universityId}")
    public List<TeacherConstraint> findTeacherConstraintByTeacherUniversity(@PathVariable("universityId") Long id) {
        return teacherConstraintService.findTeacherConstraintByTeacherUniversityId(id);
    }

    @PostMapping
    public TeacherConstraint addTeacherConstraint(@RequestBody TeacherConstraint teacherConstraint) {
        return teacherConstraintService.addTeacherConstraint(teacherConstraint);
    }

    @DeleteMapping("/{constraintId}")
    public String deleteTeacherConstraint(@PathVariable("constraintId") Long id) {
        teacherConstraintService.deleteTeacherConstraint(id);
        return "TeacherConstraint with ID " + id + " deleted successfully";
    }

    @PutMapping("/{constraintId}")
    public TeacherConstraint updateTeacherConstraint(@PathVariable("constraintId") Long id, @RequestBody TeacherConstraint teacherConstraint) {
        return teacherConstraintService.updateTeacherConstraint(id, teacherConstraint);
    }
}
