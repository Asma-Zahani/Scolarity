package security.securityscolarity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import security.securityscolarity.entity.*;
import security.securityscolarity.repository.ChronoDayRepository;
import security.securityscolarity.repository.ChronoRepository;
import security.securityscolarity.repository.DayRepository;
import security.securityscolarity.service.IMPL.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/roomConstraints")
public class RoomConstraintController {

    @Autowired
    RoomConstraintService roomConstraintService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChronoDayService chronoDayService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ChronoDayRepository chronoDayRepository;
    @Autowired
    private ChronoRepository chronoRepository;
    @Autowired
    private DayRepository dayRepository;

    @GetMapping("/all")
    public String getRoomConstraints(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("roomConstraints", roomConstraintService.findRoomConstraintByRoomBuildingUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "roomConstraint_list");
        return "UniversityAdmin/roomConstraint/allRoomConstraints";
    }

    @GetMapping("/detail")
    public String getRoomConstraint(@RequestParam("roomConstraintId") Long id, Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
        RoomConstraint roomConstraint = roomConstraintService.findByRoomConstraintID(id);
        model.addAttribute("roomConstraint", roomConstraint);
        model.addAttribute("currentUrl", "roomConstraint_detail");
        return "UniversityAdmin/roomConstraint/detailRoomConstraint";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("rooms",roomService.findByBuildingUniversity(universityAdmin.getUniversity()));
            model.addAttribute("chronoDays",chronoDayService.findByUniversity(universityAdmin.getUniversity()));
        }
        RoomConstraint roomConstraint = new RoomConstraint();
        List<Chrono> chronos = Arrays.asList(chronoRepository.findByChronoName("S4"), chronoRepository.findByChronoName("S5"), chronoRepository.findByChronoName("S6"));
        roomConstraint.initializeUnavailableChronoDays(chronos , dayRepository.findByDayName("Samedi"));
        model.addAttribute("roomConstraint", roomConstraint);
        model.addAttribute("action", "Add");
        model.addAttribute("currentUrl", "roomConstraint_add");
        return "UniversityAdmin/roomConstraint/formRoomConstraint";
    }

    @PostMapping("/addRoomConstraint")
    public String addRoomConstraint(@ModelAttribute RoomConstraint roomConstraint,
                                    @RequestParam("chronoDayIds") String[] chronoDayIds,
                                    RedirectAttributes redirectAttributes) {
        List<ChronoDay> unavailableDays = Arrays.stream(chronoDayIds)
                .map(chronoDayIdStr -> {
                    String[] parts = chronoDayIdStr.split(";");
                    Long chronoId = Long.parseLong(parts[0]);
                    Long dayId = Long.parseLong(parts[1]);
                    return chronoDayRepository.findById(new ChronoDayId(
                            chronoRepository.findByChronoId(chronoId),
                            dayRepository.findByDayId(dayId))
                    ).orElseThrow(() -> new RuntimeException("ChronoDay not found"));
                })
                .collect(Collectors.toList());

        roomConstraint.setUnavailableChronoDays(unavailableDays);

        roomConstraintService.addRoomConstraint(roomConstraint);
        redirectAttributes.addFlashAttribute("successMessage", "Room constraint added");
        return "redirect:/roomConstraints";
    }

    @GetMapping("/delete")
    public String deleteRoomConstraint(@RequestParam("roomConstraintId") Long id, RedirectAttributes redirectAttributes) {
        roomConstraintService.deleteRoomConstraint(id);
        redirectAttributes.addFlashAttribute("successMessage", "Room constraint deleted");
        return "redirect:/roomConstraints";
    }

    @GetMapping("/update")
    public String updateRoomConstraint(@RequestParam("roomConstraintId") Long id, Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("rooms",roomService.findByBuildingUniversity(universityAdmin.getUniversity()));
            model.addAttribute("chronoDays",chronoDayService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("roomConstraint", roomConstraintService.findByRoomConstraintID(id));
        model.addAttribute("action", "Update");
        model.addAttribute("currentUrl", "roomConstraint_update");
        return "UniversityAdmin/roomConstraint/formRoomConstraint";
    }

    @PostMapping("/updateRoomConstraint")
    public String updateRoomConstraint(@ModelAttribute RoomConstraint roomConstraint,
                                       @RequestParam("chronoDayIds") String[] chronoDayIds,
                                       RedirectAttributes redirectAttributes) {
        List<ChronoDay> unavailableDays = Arrays.stream(chronoDayIds)
                .map(chronoDayIdStr -> {
                    String[] parts = chronoDayIdStr.split(";");
                    Long chronoId = Long.parseLong(parts[0]);
                    Long dayId = Long.parseLong(parts[1]);
                    return chronoDayRepository.findById(new ChronoDayId(
                            chronoRepository.findByChronoId(chronoId),
                            dayRepository.findByDayId(dayId))
                    ).orElseThrow(() -> new RuntimeException("ChronoDay not found"));
                })
                .collect(Collectors.toList());

        roomConstraint.setUnavailableChronoDays(unavailableDays);
        roomConstraintService.updateRoomConstraint(roomConstraint.getId(), roomConstraint);
        redirectAttributes.addFlashAttribute("successMessage", "Room constraint updated");
        return "redirect:/roomConstraints";
    }
}