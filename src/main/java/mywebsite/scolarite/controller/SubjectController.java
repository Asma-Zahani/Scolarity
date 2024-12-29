package mywebsite.scolarite.controller;

import mywebsite.scolarite.entity.Group;
import mywebsite.scolarite.entity.Subject;
import mywebsite.scolarite.entity.Teacher;
import mywebsite.scolarite.service.IMPL.GroupService;
import mywebsite.scolarite.service.IMPL.SubjectService;
import mywebsite.scolarite.service.IMPL.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/all")
    public String getSubject(Model model) {
        List<Subject> allSubject = subjectService.findAll();
        model.addAttribute("allSubject", allSubject);
        model.addAttribute("currentUrl", "subject_list");
        return "subject/allSubject";
    }

    @GetMapping("/detail")
    public String getSubject(@RequestParam("subjectId") Long id, Model model) {
        Subject subject = subjectService.findBySubjectID(id);
        model.addAttribute("subject", subject);
        model.addAttribute("currentUrl", "subject_detail");
        return "subject/detailSubject";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("subject", new Subject());
        model.addAttribute("action","Add");
        model.addAttribute("currentUrl", "subject_add");
        return "subject/formSubject";
    }

    @PostMapping("/addSubject")
    public String addSubject(@ModelAttribute Subject subject, RedirectAttributes redirectAttributes) {
        subjectService.addSubject(subject);
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
        Subject subject = subjectService.findBySubjectID(id);
        model.addAttribute("subject", subject);
        model.addAttribute("action","Update");
        model.addAttribute("currentUrl", "subject_update");
        return "subject/formSubject";
    }

    @PostMapping("/updateSubject")
    public String updateSubject(@ModelAttribute Subject subject) {
        subjectService.updateSubject(subject.getSubjectId(),subject);
        return "redirect:/subject/all";
    }

    @GetMapping("/assignTeachers")
    public String assignTeachers(Model model) {
        model.addAttribute("subjects",subjectService.findAll());
        model.addAttribute("teachers",teacherService.findAll());
        model.addAttribute("currentUrl", "assign_subject_teacher");
        return "subject/assignTeachers";
    }

    @GetMapping("/assignGroups")
    public String assignGroups(Model model) {
        model.addAttribute("subjects",subjectService.findAll());
        model.addAttribute("groups",groupService.findAll());
        model.addAttribute("currentUrl", "assign_subject_group");
        return "subject/assignGroups";
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