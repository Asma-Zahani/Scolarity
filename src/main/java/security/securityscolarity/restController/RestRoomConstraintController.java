package security.securityscolarity.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import security.securityscolarity.entity.RoomConstraint;
import security.securityscolarity.service.IMPL.RoomConstraintService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/room-constraints")
public class RestRoomConstraintController {

    @Autowired
    RoomConstraintService roomConstraintService;

    @GetMapping
    public List<RoomConstraint> getAllRoomConstraints() {
        return roomConstraintService.findAll();
    }

    @GetMapping("/{constraintId}")
    public RoomConstraint getRoomConstraintById(@PathVariable("constraintId") Long id) {
        return roomConstraintService.findByRoomConstraintID(id);
    }

    @GetMapping("/university/{universityId}")
    public List<RoomConstraint> findRoomConstraintByRoomBuildingUniversityId(@PathVariable("universityId") Long id) {
        return roomConstraintService.findRoomConstraintByRoomBuildingUniversityId(id);
    }

    @PostMapping
    public RoomConstraint addRoomConstraint(@RequestBody RoomConstraint roomConstraint) {
        return roomConstraintService.addRoomConstraint(roomConstraint);
    }

    @DeleteMapping("/{constraintId}")
    public String deleteRoomConstraint(@PathVariable("constraintId") Long id) {
        roomConstraintService.deleteRoomConstraint(id);
        return "RoomConstraint with ID " + id + " deleted successfully";
    }

    @PutMapping("/{constraintId}")
    public RoomConstraint updateRoomConstraint(@PathVariable("constraintId") Long id, @RequestBody RoomConstraint roomConstraint) {
        return roomConstraintService.updateRoomConstraint(id, roomConstraint);
    }
}
