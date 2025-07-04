package security.securityscolarity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import security.securityscolarity.entity.*;
import security.securityscolarity.service.IMPL.GroupService;
import security.securityscolarity.service.IMPL.StudentService;
import security.securityscolarity.service.IMPL.SubGroupService;
import security.securityscolarity.service.IMPL.UserService;

import java.util.List;

@Controller
@RequestMapping("/subGroup")
public class SubGroupController {

    @Autowired
    SubGroupService subGroupService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public String getSubGroup(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("allSubGroup", subGroupService.findByGroupUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "subGroup_list");
        return "UniversityAdmin/subGroup/allSubGroup";
    }

    @GetMapping("/detail")
    public String getSubGroup(@RequestParam("subGroupId") Long id, Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
        SubGroup subGroup = subGroupService.findBySubGroupID(id);
        model.addAttribute("subGroup", subGroup);
        model.addAttribute("currentUrl", "subGroup_detail");
        return "UniversityAdmin/subGroup/detailSubGroup";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("groups",groupService.findByUniversity(universityAdmin.getUniversity()));
            model.addAttribute("students",studentService.getStudentNotAssignedToSubGroupWithUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("subGroup", new SubGroup());
        model.addAttribute("action","Add");
        model.addAttribute("currentUrl", "subGroup_add");
        return "UniversityAdmin/subGroup/formSubGroup";
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
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("subGroups",subGroupService.findByGroupUniversity(universityAdmin.getUniversity()));
            model.addAttribute("students",studentService.getStudentNotAssignedToSubGroupWithUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "assign_subGroup_students");
        return "UniversityAdmin/subGroup/assignStudents";
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
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
        SubGroup subGroup = subGroupService.findBySubGroupID(id);
        model.addAttribute("subGroup", subGroup);
        model.addAttribute("action","Update");
        model.addAttribute("groups",groupService.findAll());
        model.addAttribute("students",studentService.findAll());
        model.addAttribute("currentUrl", "subGroup_update");
        return "UniversityAdmin/subGroup/formSubGroup";
    }

    @PostMapping("/updateSubGroup")
    public String updateSubGroup(@ModelAttribute SubGroup subGroup,
                                 @RequestParam(name = "studentIds", required = false) List<Long> studentIds) {
        subGroupService.updateSubGroup(subGroup.getSubGroupId(),subGroup);
        subGroupService.clearStudents(subGroup.getSubGroupId());
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