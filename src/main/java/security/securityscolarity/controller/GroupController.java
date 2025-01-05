package security.securityscolarity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import security.securityscolarity.entity.*;
import security.securityscolarity.service.IMPL.GroupService;
import security.securityscolarity.service.IMPL.SubjectService;
import security.securityscolarity.service.IMPL.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/group")
public class GroupController {

    @Autowired
    GroupService groupService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public String getGroup(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("allGroup", groupService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "group_list");
        return "UniversityAdmin/group/allGroup";
    }

    @GetMapping("/detail")
    public String getGroup(@RequestParam("groupId") Long id, Model model) {
        Group Group = groupService.findByGroupID(id);
        model.addAttribute("group", Group);
        model.addAttribute("currentUrl", "group_detail");
        return "UniversityAdmin/group/detailGroup";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("subjects",subjectService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("group", new Group());
        model.addAttribute("action","Add");
        model.addAttribute("department", Department.values());
        model.addAttribute("currentUrl", "group_add");
        return "UniversityAdmin/group/formGroup";
    }

    @PostMapping("/addGroup")
    public String addGroup(@ModelAttribute Group group, RedirectAttributes redirectAttributes,
                           @RequestParam(name = "subjectIds", required = false) List<Long> subjectIds) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            group.setUniversity(universityAdmin.getUniversity());
            groupService.addGroup(group);
            if (subjectIds != null && !subjectIds.isEmpty()) {
                Set<Subject> subjects = new HashSet<>();
                for (Long id : subjectIds) {
                    Subject subject = subjectService.findBySubjectID(id);
                    if (subject != null) {
                        subjects.add(subject);
                    }
                }
                if (!subjects.isEmpty()) {
                    groupService.assignSubjectsToGroup(subjects, group);
                }
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "Group added");
        return "redirect:/group/all";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("groupId") Long id) {
        groupService.deleteGroup(id);
        return "redirect:/group/all";
    }

    @GetMapping("/assignSubjects")
    public String assignSubjects(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("groups",groupService.findByUniversity(universityAdmin.getUniversity()));
            model.addAttribute("subjects",subjectService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "assign_group_subjects");
        return "UniversityAdmin/group/assignSubjects";
    }

    @PostMapping("/assignGroupsToSubjects")
    public String assignGroupsToSubjects(@RequestParam(name = "groupIds", required = false) List<Long> groupIds,
                                       @RequestParam(name = "subjectIds", required = false) List<Long> subjectIds,
                                       RedirectAttributes redirectAttributes) {
        if (groupIds == null || groupIds.isEmpty() || subjectIds == null || subjectIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Groups or subjects cannot be empty.");
            return "redirect:/group/all";
        }

        Set<Subject> subjects = new HashSet<>();

        for (Long subjectId : subjectIds) {
            Subject subject = subjectService.findBySubjectID(subjectId);
            if (subject == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid subject ID: " + subjectId);
                continue;
            }
            subjects.add(subject);
        }

        for (Long groupId : groupIds) {
            Group group = groupService.findByGroupID(groupId);
            if (group == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid group ID: " + groupId);
                continue;
            }
            if (!subjects.isEmpty()) {
                groupService.assignSubjectsToGroup(subjects, group);
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "Subjects successfully assigned to groups.");
        return "redirect:/group/all";
    }

    @GetMapping("/update")
    public String updateGroup(@RequestParam("groupId") Long id, Model model) {
        Group group = groupService.findByGroupID(id);
        model.addAttribute("group", group);
        model.addAttribute("action","Update");
        model.addAttribute("department", Department.values());
        model.addAttribute("subjects",subjectService.findAll());
        model.addAttribute("currentUrl", "group_update");
        return "UniversityAdmin/group/formGroup";
    }

    @PostMapping("/updateGroup")
    public String updateGroup(@ModelAttribute Group group,
                              @RequestParam(name = "subjectIds", required = false) List<Long> subjectIds) {
        if (subjectIds != null && !subjectIds.isEmpty()) {
            Set<Subject> subjects = new HashSet<>();
            for (Long id : subjectIds) {
                Subject subject = subjectService.findBySubjectID(id);
                if (subject != null) {
                    subjects.add(subject);
                }
            }
            group.setSubjects(subjects);
        }
        groupService.updateGroup(group.getGroupId(),group);
        return "redirect:/group/all";
    }
}