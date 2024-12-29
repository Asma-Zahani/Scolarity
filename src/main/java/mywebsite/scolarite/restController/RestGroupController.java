package mywebsite.scolarite.restController;

import mywebsite.scolarite.entity.Group;
import mywebsite.scolarite.service.IMPL.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/groups")
public class RestGroupController {

    @Autowired
    GroupService groupService;

    @GetMapping
    public List<Group> getAllGroups() {
        return groupService.findAll();
    }

    @GetMapping("/{groupId}")
    public Group getGroupById(@PathVariable("groupId") Long id) {
        return groupService.findByGroupID(id);
    }

    @PostMapping
    public Group addGroup(@RequestBody Group group) {
        return groupService.addGroup(group);
    }

    @DeleteMapping("/{groupId}")
    public String deleteGroup(@PathVariable("groupId") Long id) {
        groupService.deleteGroup(id);
        return "Group with ID " + id + " deleted successfully";
    }

    @PutMapping("/{groupId}")
    public Group updateGroup(@PathVariable("groupId") Long id, @RequestBody Group group) {
        return groupService.updateGroup(id, group);
    }
}