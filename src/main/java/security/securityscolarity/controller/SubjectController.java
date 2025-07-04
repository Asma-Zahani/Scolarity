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
import security.securityscolarity.service.IMPL.TeacherService;
import security.securityscolarity.service.IMPL.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    SubjectService subjectService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public String getSubject(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("allSubject", subjectService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "subject_list");
        return "UniversityAdmin/subject/allSubject";
    }

    @GetMapping("/detail")
    public String getSubject(@RequestParam("subjectId") Long id, Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
        Subject subject = subjectService.findBySubjectID(id);
        model.addAttribute("subject", subject);
        model.addAttribute("currentUrl", "subject_detail");
        return "UniversityAdmin/subject/detailSubject";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("teachers",teacherService.findTeacherByUniversity(universityAdmin.getUniversity()));
            model.addAttribute("groups", groupService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("subject", new Subject());
        model.addAttribute("action","Add");
        model.addAttribute("currentUrl", "subject_add");
        return "UniversityAdmin/subject/formSubject";
    }

    @PostMapping("/addSubject")
    public String addSubject(@RequestParam(name = "groupIds", required = false) List<Long> groupIds,
                             @RequestParam(name = "teacherIds", required = false) List<Long> teacherIds,
                             @ModelAttribute Subject subject, RedirectAttributes redirectAttributes) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            subject.setUniversity(universityAdmin.getUniversity());
            subjectService.addSubject(subject);
            if (groupIds != null) {
                for (Long groupId : groupIds) {
                    Group group = groupService.findByGroupID(groupId);
                    if (group == null) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Invalid group ID: " + groupId);
                        continue;
                    }
                    subjectService.assignGroup(subject.getSubjectId(), group.getGroupId());
                }
            }

            if (teacherIds != null) {
                for (Long teacherId : teacherIds) {
                    Teacher teacher = teacherService.findByTeacherID(teacherId);
                    if (teacher == null) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Invalid teacher ID: " + teacherId);
                        continue;
                    }
                    subjectService.assignTeacher(subject.getSubjectId(), teacher.getId());
                }
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "Subject added");
        return "redirect:/subject/all";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("subjectId") Long id) {
        subjectService.deleteSubject(id);
        return "redirect:/subject/all";
    }

    @GetMapping("/update")
    public String updateSubject(@RequestParam("subjectId") Long id, Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
        Subject subject = subjectService.findBySubjectID(id);
        model.addAttribute("subject", subject);
        model.addAttribute("action","Update");
        model.addAttribute("teachers",teacherService.findAll());
        model.addAttribute("groups",groupService.findAll());
        model.addAttribute("currentUrl", "subject_update");
        return "UniversityAdmin/subject/formSubject";
    }

    @PostMapping("/updateSubject")
    public String updateSubject(@RequestParam(name = "groupIds", required = false) List<Long> groupIds,
                                @RequestParam(name = "teacherIds", required = false) List<Long> teacherIds,
                                @ModelAttribute Subject subject, RedirectAttributes redirectAttributes) {
        // Mettre à jour les informations principales du sujet
        subjectService.updateSubject(subject.getSubjectId(), subject);

        // Effacer les groupes et enseignants existants
        subjectService.clearGroupsAndTeachers(subject.getSubjectId());

        // Réassigner les groupes si fournis
        if (groupIds != null) {
            for (Long groupId : groupIds) {
                Group group = groupService.findByGroupID(groupId);
                if (group == null) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Invalid group ID: " + groupId);
                    continue;
                }
                subjectService.assignGroup(subject.getSubjectId(), group.getGroupId());
            }
        }

        // Réassigner les enseignants si fournis
        if (teacherIds != null) {
            for (Long teacherId : teacherIds) {
                Teacher teacher = teacherService.findByTeacherID(teacherId);
                if (teacher == null) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Invalid teacher ID: " + teacherId);
                    continue;
                }
                subjectService.assignTeacher(subject.getSubjectId(), teacher.getId());
            }
        }

        return "redirect:/subject/all";
    }

    @GetMapping("/assignTeachers")
    public String assignTeachers(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("subjects", subjectService.findByUniversity(universityAdmin.getUniversity()));
            model.addAttribute("teachers", teacherService.findTeacherByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "assign_subject_teacher");
        return "UniversityAdmin/subject/assignTeachers";
    }

    @GetMapping("/assignGroups")
    public String assignGroups(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("subjects", subjectService.findByUniversity(universityAdmin.getUniversity()));
            model.addAttribute("groups", groupService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "assign_subject_group");
        return "UniversityAdmin/subject/assignGroups";
    }

    @PostMapping("/assignSubjectsToGroups")
    public String assignSubjectsToGroups(@RequestParam(name = "groupIds", required = false) List<Long> groupIds,
                                       @RequestParam(name = "subjectIds", required = false) List<Long> subjectIds,
                                       RedirectAttributes redirectAttributes) {
        if (groupIds == null || groupIds.isEmpty() || subjectIds == null || subjectIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Groups or subjects cannot be empty.");
            return "redirect:/subject/all";
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
        return "redirect:/subject/all";
    }

    @PostMapping("/assignSubjectsToTeachers")
    public String assignSubjectsToTeachers(@RequestParam(name = "teacherIds", required = false) List<Long> teacherIds,
                                         @RequestParam(name = "subjectIds", required = false) List<Long> subjectIds,
                                         RedirectAttributes redirectAttributes) {
        if (teacherIds == null || teacherIds.isEmpty() || subjectIds == null || subjectIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Groups or subjects cannot be empty.");
            return "redirect:/subject/all";
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

        for (Long teacherId : teacherIds) {
            Teacher teacher = teacherService.findByTeacherID(teacherId);
            if (teacher == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid teacher ID: " + teacherId);
                continue;
            }
            if (!subjects.isEmpty()) {
                teacherService.assignSubjectsToTeacher(subjects, teacher);
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "Subjects successfully assigned to groups.");
        return "redirect:/subject/all";
    }
}