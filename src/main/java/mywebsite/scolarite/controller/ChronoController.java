package mywebsite.scolarite.controller;

import mywebsite.scolarite.entity.Chrono;
import mywebsite.scolarite.service.IMPL.ChronoDayService;
import mywebsite.scolarite.service.IMPL.ChronoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/chrono")
public class ChronoController {

    @Autowired
    ChronoService chronoService;
    @Autowired
    private ChronoDayService chronoDayService;

    @GetMapping("/all")
    public String getChrono(Model model) {
        List<Chrono> allChrono = chronoService.findAll();
        model.addAttribute("allChrono", allChrono);
        model.addAttribute("currentUrl", "chrono_list");
        return "chrono/allChrono";
    }

    @GetMapping("/detail")
    public String getChrono(@RequestParam("chronoId") Long id, Model model) {
        Chrono chrono = chronoService.findByChronoID(id);
        model.addAttribute("chrono", chrono);
        model.addAttribute("currentUrl", "chrono_detail");
        return "chrono/detailChrono";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("chrono", new Chrono());
        model.addAttribute("action","Add");
        model.addAttribute("currentUrl", "chrono_add");
        return "chrono/formChrono";
    }

    @PostMapping("/addChrono")
    public String addChrono(@ModelAttribute Chrono chrono, RedirectAttributes redirectAttributes) {
        chronoService.addChrono(chrono);
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
        return "chrono/formChrono";
    }

    @PostMapping("/updateChrono")
    public String updateChrono(@ModelAttribute Chrono chrono) {
        chronoService.updateChrono(chrono.getChronoId(),chrono);
        return "redirect:/chrono/all";
    }
}
