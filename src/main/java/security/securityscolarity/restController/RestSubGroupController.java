package security.securityscolarity.restController;

import security.securityscolarity.entity.Building;
import security.securityscolarity.entity.SubGroup;
import security.securityscolarity.entity.Subject;
import security.securityscolarity.service.IMPL.SubGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
    @RequestMapping("/api/subgroups")
public class RestSubGroupController {

    @Autowired
    SubGroupService subGroupService;

    @GetMapping
    public List<SubGroup> getAllSubGroups() {
        return subGroupService.findAll();
    }

    @GetMapping("/university/{universityId}")
    public List<SubGroup> getSubGroupsByUniversity(@PathVariable("universityId") Long id) {
        return subGroupService.findByGroupUniversityId(id);
    }

    @GetMapping("/{subGroupId}")
    public SubGroup getSubGroupById(@PathVariable("subGroupId") Long id) {
        return subGroupService.findBySubGroupID(id);
    }

    @PostMapping
    public SubGroup addSubGroup(@RequestBody SubGroup subGroup) {
        return subGroupService.addSubGroup(subGroup);
    }

    @PostMapping("/{groupId}")
    public SubGroup addSubGroupByGroup(@PathVariable("groupId") Long id, @RequestBody SubGroup subGroup) {
        return subGroupService.addSubGroupByGroup(subGroup,id);
    }

    @DeleteMapping("/{subGroupId}")
    public String deleteSubGroup(@PathVariable("subGroupId") Long id) {
        subGroupService.deleteSubGroup(id);
        return "SubGroup with ID " + id + " deleted successfully";
    }

    @PutMapping("/{subGroupId}")
    public SubGroup updateSubGroup(@PathVariable("subGroupId") Long id, @RequestBody SubGroup subGroup) {
        return subGroupService.updateSubGroup(id, subGroup);
    }
}
