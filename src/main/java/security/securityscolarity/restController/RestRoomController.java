package security.securityscolarity.restController;

import security.securityscolarity.entity.Room;
import security.securityscolarity.entity.Student;
import security.securityscolarity.entity.Subject;
import security.securityscolarity.service.IMPL.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import security.securityscolarity.service.IMPL.UniversityService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/rooms")
public class RestRoomController {

    @Autowired
    RoomService roomService;
    @Autowired
    private UniversityService universityService;

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.findAll();
    }

    @GetMapping("/count/{universityId}")
    public Long getRoomCountByUniversity(@PathVariable("universityId") Long id) {
        return roomService.countByUniversity(universityService.findByUniversityID(id));
    }

    @GetMapping("/university/{universityId}")
    public List<Room> getRoomsByUniversity(@PathVariable("universityId") Long id) {
        return roomService.findByBuildingUniversityId(id);
    }

    @GetMapping("/{roomId}")
    public Room getRoomById(@PathVariable("roomId") Long id) {
        return roomService.findByRoomID(id);
    }

    @PostMapping
    public Room addRoom(@RequestBody Room room) {
        return roomService.addRoom(room);
    }

    @DeleteMapping("/{roomId}")
    public String deleteRoom(@PathVariable("roomId") Long id) {
        roomService.deleteRoom(id);
        return "Room with ID " + id + " deleted successfully";
    }

    @PutMapping("/{roomId}")
    public Room updateRoom(@PathVariable("roomId") Long id, @RequestBody Room room) {
        return roomService.updateRoom(id, room);
    }

    @PutMapping("/assign/{buildingId}")
    public String assignRoomToBuilding(@RequestBody Room room, @PathVariable("buildingId") Long id) {
        roomService.assignRoomToBuilding(room,id);
        return "Room with ID " + room.getRoomId() + " assigned successfully";
    }
}