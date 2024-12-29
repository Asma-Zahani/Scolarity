package mywebsite.scolarite.restController;

import mywebsite.scolarite.entity.Room;
import mywebsite.scolarite.service.IMPL.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/rooms")
public class RestRoomController {

    @Autowired
    RoomService roomService;

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.findAll();
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
}