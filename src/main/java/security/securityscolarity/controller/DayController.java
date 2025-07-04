package security.securityscolarity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import security.securityscolarity.entity.CustomUserDetail;
import security.securityscolarity.entity.Day;
import security.securityscolarity.entity.UniversityAdmin;
import security.securityscolarity.entity.User;
import security.securityscolarity.service.IMPL.ChronoDayService;
import security.securityscolarity.service.IMPL.DayService;
import security.securityscolarity.service.IMPL.UserService;

import java.util.List;

@Controller
@RequestMapping("/day")
public class DayController {

    @Autowired
    DayService dayService;
    @Autowired
    private ChronoDayService chronoDayService;
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public String getDay(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
        List<Day> allDay = dayService.findAll();
        model.addAttribute("allDay", allDay);
        model.addAttribute("currentUrl", "day_list");
        return "UniversityAdmin/day/allDay";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
        model.addAttribute("day", new Day());
        model.addAttribute("action","Add");
        model.addAttribute("currentUrl", "day_add");
        return "UniversityAdmin/day/formDay";
    }

    @PostMapping("/addDay")
    public String addDay(@ModelAttribute Day day, RedirectAttributes redirectAttributes) {
        dayService.addDay(day);
        redirectAttributes.addFlashAttribute("successMessage", "Day added");
        return "redirect:/day/all";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("dayId") Long id) {
        chronoDayService.deleteChronoDayByDayId(id);
        dayService.deleteDay(id);
        return "redirect:/day/all";
    }

    @GetMapping("/update")
    public String updateDay(@RequestParam("dayId") Long id, Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
        Day day = dayService.findByDayID(id);
        model.addAttribute("day", day);
        model.addAttribute("action","Update");
        model.addAttribute("currentUrl", "day_update");
        return "UniversityAdmin/day/formDay";
    }

    @PostMapping("/updateDay")
    public String updateDay(@ModelAttribute Day day) {
        dayService.updateDay(day.getDayId(),day);
        return "redirect:/day/all";
    }
}
