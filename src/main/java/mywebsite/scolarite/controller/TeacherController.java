package mywebsite.scolarite.controller;

import mywebsite.scolarite.entity.Specialite;
import mywebsite.scolarite.entity.Subject;
import mywebsite.scolarite.entity.Teacher;
import mywebsite.scolarite.service.IMPL.SubjectService;
import mywebsite.scolarite.service.IMPL.TeacherService;
import mywebsite.scolarite.service.IMPL.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    TeacherService teacherService;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private SubjectService subjectService;

    @GetMapping("/all")
    public String getTeacher(Model model) {
        List<Teacher> allTeacher = teacherService.findAll();
        model.addAttribute("allTeacher", allTeacher);
        model.addAttribute("currentUrl", "teacher_list");
        return "teacher/allTeacher";
    }

    @GetMapping("/detail")
    public String getTeacher(@RequestParam("teacherId") Long id, Model model) {
        Teacher teacher = teacherService.findByTeacherID(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("currentUrl", "teacher_detail");
        return "teacher/detailTeacher";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("action","Add");
        model.addAttribute("specialite", Specialite.values());
        model.addAttribute("universities", universityService.findAll());
        model.addAttribute("subjects",subjectService.findAll());
        model.addAttribute("currentUrl", "teacher_add");
        return "teacher/formTeacher";
    }

    @PostMapping("/addTeacher")
    public String addTeacher(@ModelAttribute Teacher teacher,
                             @RequestParam(name = "subjectIds", required = false) List<Long> subjectIds,
                             RedirectAttributes redirectAttributes) {
        teacherService.addTeacher(teacher);
        if (subjectIds != null && !subjectIds.isEmpty()) {
            Set<Subject> subjects = new HashSet<>();
            for (Long id : subjectIds) {
                Subject subject = subjectService.findBySubjectID(id);
                if (subject != null) {
                    subjects.add(subject);
                }
            }
            if (!subjects.isEmpty()) {
                teacherService.assignSubjectsToTeacher(subjects, teacher);
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "Teacher added");
        return "redirect:/teacher/all";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("teacherId") Long id) {
        teacherService.deleteTeacher(id);
        return "redirect:/teacher/all";
    }

    @GetMapping("/assignSubjects")
    public String assignSubjects(Model model) {
        model.addAttribute("subjects",subjectService.findAll());
        model.addAttribute("teachers",teacherService.findAll());
        model.addAttribute("currentUrl", "assign_subject_teacher");
        return "teacher/assignSubjects";
    }

    @PostMapping("/assignTeachersToSubjects")
    public String assignTeachersToSubjects(@RequestParam(name = "teacherIds", required = false) List<Long> teacherIds,
                                           @RequestParam(name = "subjectIds", required = false) List<Long> subjectIds,
                                           RedirectAttributes redirectAttributes) {
        if (teacherIds == null || teacherIds.isEmpty() || subjectIds == null || subjectIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Teachers or subjects cannot be empty.");
            return "redirect:/teacher/all";
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
        redirectAttributes.addFlashAttribute("successMessage", "Teachers successfully assigned to subjects.");
        return "redirect:/teacher/all";
    }

    @GetMapping("/update")
    public String updateTeacher(@RequestParam("teacherId") Long id, Model model) {
        Teacher teacher = teacherService.findByTeacherID(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("action","Update");
        model.addAttribute("specialite", Specialite.values());
        model.addAttribute("universities", universityService.findAll());
        model.addAttribute("subjects",subjectService.findAll());
        model.addAttribute("currentUrl", "teacher_update");
        return "teacher/formTeacher";
    }

    @PostMapping("/updateTeacher")
    public String updateTeacher(@ModelAttribute Teacher teacher,
                           @RequestParam(name = "subjectIds", required = false) List<Long> subjectIds) {
        teacherService.updateTeacher(teacher.getId(),teacher);
        if (subjectIds != null && !subjectIds.isEmpty()) {
            Set<Subject> subjects = new HashSet<>();
            for (Long id : subjectIds) {
                Subject subject = subjectService.findBySubjectID(id);
                if (subject != null) {
                    subjects.add(subject);
                }
            }
            if (!subjects.isEmpty()) {
                teacherService.assignSubjectsToTeacher(subjects, teacher);
            }
        }
        return "redirect:/teacher/all";
    }
}