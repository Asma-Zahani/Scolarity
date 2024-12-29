package mywebsite.scolarite.controller;

import mywebsite.scolarite.entity.*;
import mywebsite.scolarite.service.IMPL.GroupService;
import mywebsite.scolarite.service.IMPL.StudentService;
import mywebsite.scolarite.service.IMPL.SubGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/subGroup")
public class SubGroupController {

    @Autowired
    SubGroupService subGroupService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private StudentService studentService;

    @GetMapping("/all")
    public String getSubGroup(Model model) {
        List<SubGroup> allSubGroup = subGroupService.findAll();
        model.addAttribute("allSubGroup", allSubGroup);
        model.addAttribute("currentUrl", "subGroup_list");
        return "subGroup/allSubGroup";
    }

    @GetMapping("/detail")
    public String getSubGroup(@RequestParam("subGroupId") Long id, Model model) {
        SubGroup subGroup = subGroupService.findBySubGroupID(id);
        model.addAttribute("subGroup", subGroup);
        model.addAttribute("currentUrl", "subGroup_detail");
        return "subGroup/detailSubGroup";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("subGroup", new SubGroup());
        model.addAttribute("action","Add");
        model.addAttribute("groups",groupService.findAll());
        model.addAttribute("students",studentService.getStudentNotAssignedToSubGroup());
        model.addAttribute("currentUrl", "subGroup_add");
        return "subGroup/formSubGroup";
    }

    @PostMapping("/addSubGroup")
    public String addSubGroup(@ModelAttribute SubGroup subGroup,
                              @RequestParam(name = "studentIds", required = false) List<Long> studentIds,
                              RedirectAttributes redirectAttributes) {
        subGroupService.addSubGroup(subGroup);
        if (studentIds != null && !studentIds.isEmpty()) {
            for (Long id : studentIds) {
                Student student = studentService.findByStudentID(id);
                if (student != null) {
                    student.setSubGroup(subGroup);
                    studentService.updateStudent(student.getId(), student);
                }
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "SubGroup added");
        return "redirect:/subGroup/all";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("subGroupId") Long id) {
        subGroupService.deleteSubGroup(id);
        return "redirect:/subGroup/all";
    }

    @GetMapping("/assignStudents")
    public String assignStudents(Model model) {
        model.addAttribute("subGroups",subGroupService.findAll());
        model.addAttribute("students",studentService.getStudentNotAssignedToSubGroup());
        model.addAttribute("currentUrl", "assign_subGroup_students");
        return "subGroup/assignStudents";
    }

    @PostMapping("/assignSubGroupToStudents")
    public String assignSubGroupToStudents(@RequestParam("subGroupId") Long subGroupId,
                                           @RequestParam(name = "studentIds", required = false) List<Long> studentIds,
                                         RedirectAttributes redirectAttributes) {
        SubGroup subGroup = subGroupService.findBySubGroupID(subGroupId);
        if (studentIds != null && !studentIds.isEmpty()) {
            for (Long id : studentIds) {
                Student student = studentService.findByStudentID(id);
                if (student != null) {
                    student.setSubGroup(subGroup);
                    studentService.updateStudent(student.getId(), student);
                }
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "Students successfully assigned to subGroup.");
        return "redirect:/subGroup/all";
    }

    @GetMapping("/update")
    public String updateSubGroup(@RequestParam("subGroupId") Long id, Model model) {
        SubGroup subGroup = subGroupService.findBySubGroupID(id);
        model.addAttribute("subGroup", subGroup);
        model.addAttribute("action","Update");
        model.addAttribute("groups",groupService.findAll());
        model.addAttribute("students",studentService.findAll());
        model.addAttribute("currentUrl", "subGroup_update");
        return "subGroup/formSubGroup";
    }

    @PostMapping("/updateSubGroup")
    public String updateSubGroup(@ModelAttribute SubGroup subGroup,
                                 @RequestParam(name = "studentIds", required = false) List<Long> studentIds) {
        subGroupService.updateSubGroup(subGroup.getSubGroupId(),subGroup);
        if (studentIds != null && !studentIds.isEmpty()) {
            for (Long id : studentIds) {
                Student student = studentService.findByStudentID(id);
                if (student != null) {
                    student.setSubGroup(subGroup);
                    studentService.updateStudent(student.getId(), student);
                }
            }
        }
        return "redirect:/subGroup/all";
    }
}