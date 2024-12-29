package mywebsite.scolarite.controller;

import mywebsite.scolarite.entity.Building;
import mywebsite.scolarite.entity.Room;
import mywebsite.scolarite.entity.Teacher;
import mywebsite.scolarite.service.IMPL.BuildingService;
import mywebsite.scolarite.service.IMPL.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/building")
public class BuildingController {

    @Autowired
    BuildingService buildingService;
    @Autowired
    private RoomService roomService;

    @GetMapping("/all")
    public String getBuilding(Model model) {
        List<Building> allBuilding = buildingService.findAll();
        model.addAttribute("allBuilding", allBuilding);
        model.addAttribute("currentUrl", "building_list");
        return "building/allBuilding";
    }

    @GetMapping("/detail")
    public String getBuilding(@RequestParam("buildingId") Long id, Model model) {
        Building building = buildingService.findByBuildingID(id);
        model.addAttribute("building", building);
        model.addAttribute("currentUrl", "building_detail");
        return "building/detailBuilding";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("building", new Building());
        model.addAttribute("action","Add");
        model.addAttribute("currentUrl", "building_add");
        return "building/formBuilding";
    }

    @PostMapping("/addBuilding")
    public String addBuilding(@ModelAttribute Building building, RedirectAttributes redirectAttributes) {
        buildingService.addBuilding(building);
        redirectAttributes.addFlashAttribute("successMessage", "Building added");
        return "redirect:/building/all";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("buildingId") Long id) {
        buildingService.deleteBuilding(id);
        return "redirect:/building/all";
    }

    @GetMapping("/update")
    public String updateBuilding(@RequestParam("buildingId") Long id, Model model) {
       Building building = buildingService.findByBuildingID(id);
       model.addAttribute("building", building);
       model.addAttribute("action","Update");
       model.addAttribute("currentUrl", "building_update");
       return "building/formBuilding";
    }

    @PostMapping("/updateBuilding")
    public String updateBuilding(@ModelAttribute Building building) {
        buildingService.updateBuilding(building.getBuildingId(),building);
        return "redirect:/building/all";
    }
}
