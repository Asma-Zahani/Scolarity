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
@RequestMapping("/groupConstraints")
public class GroupConstraintController {

    @Autowired
    GroupConstraintService groupConstraintService;

    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private ChronoDayService chronoDayService;
    @Autowired
    private ChronoDayRepository chronoDayRepository;
    @Autowired
    private ChronoRepository chronoRepository;
    @Autowired
    private DayRepository dayRepository;
    @GetMapping("/all")
    public String getGroupConstraints(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("groupConstraints", groupConstraintService.findGroupConstraintByGroupUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "groupConstraint_list");
        return "UniversityAdmin/groupConstraint/allGroupConstraints";
    }

    @GetMapping("/detail")
    public String getGroupConstraint(@RequestParam("groupConstraintId") Long id, Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
        GroupConstraint groupConstraint = groupConstraintService.findByGroupConstraintID(id);
        model.addAttribute("groupConstraint", groupConstraint);
        model.addAttribute("currentUrl", "groupConstraint_detail");
        return "UniversityAdmin/groupConstraint/detailGroupConstraint";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("groups",groupService.findByUniversity(universityAdmin.getUniversity()));
            model.addAttribute("chronoDays",chronoDayService.findByUniversity(universityAdmin.getUniversity()));
        }
        GroupConstraint groupConstraint = new GroupConstraint();
        List<Chrono> chronos = Arrays.asList(chronoRepository.findByChronoName("S4"), chronoRepository.findByChronoName("S5"), chronoRepository.findByChronoName("S6"));
        groupConstraint.initializeUnavailableChronoDays(chronos , dayRepository.findByDayName("Samedi"));
        model.addAttribute("groupConstraint", groupConstraint);
        model.addAttribute("action", "Add");
        model.addAttribute("currentUrl", "groupConstraint_add");
        return "UniversityAdmin/groupConstraint/formGroupConstraint";
    }

    @PostMapping("/addGroupConstraint")
    public String addGroupConstraint(@ModelAttribute GroupConstraint groupConstraint,
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

        groupConstraint.setUnavailableChronoDays(unavailableDays);

        groupConstraintService.addGroupConstraint(groupConstraint);
        redirectAttributes.addFlashAttribute("successMessage", "Group constraint added");
        return "redirect:/groupConstraints";
    }

    @GetMapping("/delete")
    public String deleteGroupConstraint(@RequestParam("groupConstraintId") Long id, RedirectAttributes redirectAttributes) {
        groupConstraintService.deleteGroupConstraint(id);
        redirectAttributes.addFlashAttribute("successMessage", "Group constraint deleted");
        return "redirect:/groupConstraints";
    }

    @GetMapping("/update")
    public String updateGroupConstraint(@RequestParam("groupConstraintId") Long id, Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("groups",groupService.findByUniversity(universityAdmin.getUniversity()));
            model.addAttribute("chronoDays",chronoDayService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("groupConstraint", groupConstraintService.findByGroupConstraintID(id));
        model.addAttribute("action", "Update");
        model.addAttribute("currentUrl", "groupConstraint_update");
        return "UniversityAdmin/groupConstraint/formGroupConstraint";
    }

    @PostMapping("/updateGroupConstraint")
    public String updateGroupConstraint(@ModelAttribute GroupConstraint groupConstraint,
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

        groupConstraint.setUnavailableChronoDays(unavailableDays);

        groupConstraintService.updateGroupConstraint(groupConstraint.getId(), groupConstraint);
        redirectAttributes.addFlashAttribute("successMessage", "Group constraint updated");
        return "redirect:/groupConstraints";
    }
}