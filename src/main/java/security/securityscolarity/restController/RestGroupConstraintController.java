package security.securityscolarity.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import security.securityscolarity.entity.GroupConstraint;
import security.securityscolarity.service.IMPL.GroupConstraintService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/group-constraints")
public class RestGroupConstraintController {

    @Autowired
    GroupConstraintService groupConstraintService;

    @GetMapping
    public List<GroupConstraint> getAllGroupConstraints() {
        return groupConstraintService.findAll();
    }

    @GetMapping("/{constraintId}")
    public GroupConstraint getGroupConstraintById(@PathVariable("constraintId") Long id) {
        return groupConstraintService.findByGroupConstraintID(id);
    }

    @GetMapping("/university/{universityId}")
    public List<GroupConstraint> findGroupConstraintByGroupUniversity(@PathVariable("universityId") Long id) {
        return groupConstraintService.findGroupConstraintByGroupUniversityId(id);
    }

    @PostMapping
    public GroupConstraint addGroupConstraint(@RequestBody GroupConstraint groupConstraint) {
        return groupConstraintService.addGroupConstraint(groupConstraint);
    }

    @DeleteMapping("/{constraintId}")
    public String deleteGroupConstraint(@PathVariable("constraintId") Long id) {
        groupConstraintService.deleteGroupConstraint(id);
        return "GroupConstraint with ID " + id + " deleted successfully";
    }

    @PutMapping("/{constraintId}")
    public GroupConstraint updateGroupConstraint(@PathVariable("constraintId") Long id, @RequestBody GroupConstraint groupConstraint) {
        return groupConstraintService.updateGroupConstraint(id, groupConstraint);
    }
}
