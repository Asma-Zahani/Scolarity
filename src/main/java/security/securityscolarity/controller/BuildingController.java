package security.securityscolarity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import security.securityscolarity.entity.*;
import security.securityscolarity.service.IMPL.BuildingService;
import security.securityscolarity.service.IMPL.RoomService;
import security.securityscolarity.service.IMPL.UniversityService;
import security.securityscolarity.service.IMPL.UserService;

@Controller
@RequestMapping("/building")
public class BuildingController {

    @Autowired
    BuildingService buildingService;
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public String getBuilding(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("allBuilding", buildingService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "building_list");
        return "UniversityAdmin/building/allBuilding";
    }

    @GetMapping("/detail")
    public String getBuilding(@RequestParam("buildingId") Long id, Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
        Building building = buildingService.findByBuildingID(id);
        model.addAttribute("building", building);
        model.addAttribute("currentUrl", "building_detail");
        return "UniversityAdmin/building/detailBuilding";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
        model.addAttribute("building", new Building());
        model.addAttribute("action","Add");
        model.addAttribute("currentUrl", "building_add");
        return "UniversityAdmin/building/formBuilding";
    }

    @PostMapping("/addBuilding")
    public String addBuilding(@ModelAttribute Building building, RedirectAttributes redirectAttributes) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            building.setUniversity(universityAdmin.getUniversity());
            buildingService.addBuilding(building);
        }
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
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
       Building building = buildingService.findByBuildingID(id);
       model.addAttribute("building", building);
       model.addAttribute("action","Update");
       model.addAttribute("currentUrl", "building_update");
       return "UniversityAdmin/building/formBuilding";
    }

    @PostMapping("/updateBuilding")
    public String updateBuilding(@ModelAttribute Building building) {
        buildingService.updateBuilding(building.getBuildingId(),building);
        return "redirect:/building/all";
    }
}
