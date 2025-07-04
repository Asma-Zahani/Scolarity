package security.securityscolarity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import security.securityscolarity.entity.*;
import security.securityscolarity.service.IMPL.*;

import java.util.List;

@Controller
@RequestMapping("/chronoDay")
public class ChronoDayController {

    @Autowired
    ChronoDayService chronoDayService;

    @Autowired
    RoomService roomService;
    @Autowired
    private ChronoService chronoService;
    @Autowired
    private DayService dayService;
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public String getChrono(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("allChronoDay", chronoDayService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "chronoDay_list");
        return "UniversityAdmin/chronoDay/allChronoDay";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("chronos", chronoService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("chronoDay", new ChronoDay());
        model.addAttribute("action","Add");
        model.addAttribute("days", dayService.findAll());
        model.addAttribute("currentUrl", "chronoDay_add");
        return "UniversityAdmin/chronoDay/formChronoDay";
    }

    @GetMapping("/addChronos")
    public String showAddChronosForm(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("chronos", chronoService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("chronoDay", new ChronoDay());
        model.addAttribute("action","Add");
        model.addAttribute("days", dayService.findAll());
        model.addAttribute("currentUrl", "chronoDay_add_chronos");
        return "UniversityAdmin/chronoDay/formChronos";
    }

    @GetMapping("/addDays")
    public String showAddDaysForm(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("chronos", chronoService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("chronoDay", new ChronoDay());
        model.addAttribute("action","Add");
        model.addAttribute("days", dayService.findAll());
        model.addAttribute("currentUrl", "chronoDay_add_days");
        return "UniversityAdmin/chronoDay/formDays";
    }

    @PostMapping("/addChronoDay")
    public String addChronoDay(@ModelAttribute ChronoDay chronoDay, RedirectAttributes redirectAttributes) {
        chronoDayService.addChronoDay(chronoDay);
        redirectAttributes.addFlashAttribute("successMessage", "ChronoDay added");
        return "redirect:/chronoDay/all";
    }

    @PostMapping("/addChronosToDay")
    public String addChronosToDay(@ModelAttribute ChronoDay chronoDay,
                                  @RequestParam(name = "chronoIds", required = false) List<Long> chronoIds,
                                  RedirectAttributes redirectAttributes) {
        if (chronoIds != null && !chronoIds.isEmpty()) {
            for (Long id : chronoIds) {
                Chrono chrono = chronoService.findByChronoID(id);
                if (chrono != null) {
                    chronoDayService.addChronoDay(new ChronoDay(new ChronoDayId(chrono,chronoDay.getId().getDay())));
                }
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "ChronoDay added");
        return "redirect:/chronoDay/all";
    }

    @PostMapping("/addDaysToChrono")
    public String addDaysToChrono(@ModelAttribute ChronoDay chronoDay,
                                  @RequestParam(name = "dayIds", required = false) List<Long> dayIds,
                                  RedirectAttributes redirectAttributes) {
        if (dayIds != null && !dayIds.isEmpty()) {
            for (Long id : dayIds) {
                Day day = dayService.findByDayID(id);
                if (day != null) {
                    chronoDayService.addChronoDay(new ChronoDay(new ChronoDayId(chronoDay.getId().getChrono(),day)));
                }
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "ChronoDay added");
        return "redirect:/chronoDay/all";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("chronoId") Long chronoId, @RequestParam("dayId") Long dayId) {
        Chrono chrono = chronoService.findByChronoID(chronoId);
        Day day = dayService.findByDayID(dayId);
        chronoDayService.deleteChronoDay(new ChronoDayId(chrono, day));
        return "redirect:/chronoDay/all";
    }

    @GetMapping("/update")
    public String updateChronoDay(@RequestParam("chronoId") Long chronoId, @RequestParam("dayId") Long dayId, Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
        Chrono chrono = chronoService.findByChronoID(chronoId);
        Day day = dayService.findByDayID(dayId);
        ChronoDay chronoDay = chronoDayService.findByChronoDayId(new ChronoDayId(chrono, day));
        model.addAttribute("chronoDay", chronoDay);
        model.addAttribute("action","Update");
        model.addAttribute("days", dayService.findAll());
        model.addAttribute("chronos", chronoService.findAll());
        model.addAttribute("currentUrl", "chronoDay_update");
        return "UniversityAdmin/chronoDay/formChronoDay";
    }

    @PostMapping("/updateChronoDay")
    public String updateChronoDay(@ModelAttribute ChronoDay chronoDay) {
        chronoDayService.updateChronoDay(chronoDay.getId(),chronoDay);
        return "redirect:/chronoDay/all";
    }
}
