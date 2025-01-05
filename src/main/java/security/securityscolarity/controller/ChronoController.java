package security.securityscolarity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import security.securityscolarity.entity.Chrono;
import security.securityscolarity.entity.CustomUserDetail;
import security.securityscolarity.entity.UniversityAdmin;
import security.securityscolarity.entity.User;
import security.securityscolarity.service.IMPL.ChronoDayService;
import security.securityscolarity.service.IMPL.ChronoService;
import security.securityscolarity.service.IMPL.UserService;

import java.util.List;

@Controller
@RequestMapping("/chrono")
public class ChronoController {

    @Autowired
    ChronoService chronoService;
    @Autowired
    private ChronoDayService chronoDayService;
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public String getChrono(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("allChrono", chronoService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "chrono_list");
        return "UniversityAdmin/chrono/allChrono";
    }

    @GetMapping("/detail")
    public String getChrono(@RequestParam("chronoId") Long id, Model model) {
        Chrono chrono = chronoService.findByChronoID(id);
        model.addAttribute("chrono", chrono);
        model.addAttribute("currentUrl", "chrono_detail");
        return "UniversityAdmin/chrono/detailChrono";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("chrono", new Chrono());
        model.addAttribute("action","Add");
        model.addAttribute("currentUrl", "chrono_add");
        return "UniversityAdmin/chrono/formChrono";
    }

    @PostMapping("/addChrono")
    public String addChrono(@ModelAttribute Chrono chrono, RedirectAttributes redirectAttributes) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            chrono.setUniversity(universityAdmin.getUniversity());
            chronoService.addChrono(chrono);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Chrono added");
        return "redirect:/chrono/all";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("chronoId") Long id) {
        chronoDayService.deleteChronoDayByChronoId(id);
        chronoService.deleteChrono(id);
        return "redirect:/chrono/all";
    }

    @GetMapping("/update")
    public String updateChrono(@RequestParam("chronoId") Long id, Model model) {
        Chrono chrono = chronoService.findByChronoID(id);
        model.addAttribute("chrono", chrono);
        model.addAttribute("action","Update");
        model.addAttribute("currentUrl", "chrono_update");
        return "UniversityAdmin/chrono/formChrono";
    }

    @PostMapping("/updateChrono")
    public String updateChrono(@ModelAttribute Chrono chrono) {
        chronoService.updateChrono(chrono.getChronoId(),chrono);
        return "redirect:/chrono/all";
    }
}
