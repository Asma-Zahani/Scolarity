package security.securityscolarity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import security.securityscolarity.entity.CustomUserDetail;
import security.securityscolarity.entity.Room;
import security.securityscolarity.entity.UniversityAdmin;
import security.securityscolarity.entity.User;
import security.securityscolarity.service.IMPL.BuildingService;
import security.securityscolarity.service.IMPL.RoomService;
import security.securityscolarity.service.IMPL.UserService;

import java.util.List;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    RoomService roomService;

    @Autowired
    BuildingService buildingService;
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public String getRoom(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("allRoom", roomService.findByBuildingUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "room_list");
        return "UniversityAdmin/room/allRoom";
    }

    @GetMapping("/detail")
    public String getRoom(@RequestParam("roomId") Long id, Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
        Room room = roomService.findByRoomID(id);
        model.addAttribute("room", room);
        model.addAttribute("currentUrl", "room_detail");
        return "UniversityAdmin/room/detailRoom";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("buildings",buildingService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("room", new Room());
        model.addAttribute("action","Add");
        model.addAttribute("currentUrl", "room_add");
        return "UniversityAdmin/room/formRoom";
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
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
        Room room = roomService.findByRoomID(id);
        model.addAttribute("room", room);
        model.addAttribute("action","Update");
        model.addAttribute("buildings",buildingService.findAll());
        model.addAttribute("currentUrl", "room_update");
        return "UniversityAdmin/room/formRoom";
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