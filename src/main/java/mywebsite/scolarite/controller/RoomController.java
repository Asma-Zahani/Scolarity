package mywebsite.scolarite.controller;

import mywebsite.scolarite.entity.Room;
import mywebsite.scolarite.service.IMPL.BuildingService;
import mywebsite.scolarite.service.IMPL.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    RoomService roomService;

    @Autowired
    BuildingService buildingService;

    @GetMapping("/all")
    public String getRoom(Model model) {
        List<Room> allRoom = roomService.findAll();
        model.addAttribute("allRoom", allRoom);
        model.addAttribute("currentUrl", "room_list");
        return "room/allRoom";
    }

    @GetMapping("/detail")
    public String getRoom(@RequestParam("roomId") Long id, Model model) {
        Room room = roomService.findByRoomID(id);
        model.addAttribute("room", room);
        model.addAttribute("currentUrl", "room_detail");
        return "room/detailRoom";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("action","Add");
        model.addAttribute("buildings",buildingService.findAll());
        model.addAttribute("currentUrl", "room_add");
        return "room/formRoom";
    }

    @PostMapping("/addRoom")
    public String addRoom(@ModelAttribute Room room,@RequestParam(name = "idBuilding", required = false) Long idBuilding, RedirectAttributes redirectAttributes) {
        roomService.addRoom(room);
        if (idBuilding != null) {
            roomService.assignRoomToBuilding(room, idBuilding);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Room added");
        return "redirect:/room/all";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("roomId") Long id) {
        roomService.deleteRoom(id);
        return "redirect:/room/all";
    }

    @GetMapping("/update")
    public String updateRoom(@RequestParam("roomId") Long id, Model model) {
        Room room = roomService.findByRoomID(id);
        model.addAttribute("room", room);
        model.addAttribute("action","Update");
        model.addAttribute("buildings",buildingService.findAll());
        model.addAttribute("currentUrl", "room_update");
        return "room/formRoom";
    }

    @PostMapping("/updateRoom")
    public String updateRoom(@ModelAttribute Room room, @RequestParam(name = "idBuilding", required = false) Long idBuilding) {
        roomService.updateRoom(room.getRoomId(),room);
        if (idBuilding != null) {
            roomService.assignRoomToBuilding(room, idBuilding);
        }
        return "redirect:/room/all";
    }
}