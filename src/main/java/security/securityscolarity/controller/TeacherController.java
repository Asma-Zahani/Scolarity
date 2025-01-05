package security.securityscolarity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import security.securityscolarity.entity.*;
import security.securityscolarity.service.IMPL.SubjectService;
import security.securityscolarity.service.IMPL.TeacherService;
import security.securityscolarity.service.IMPL.UniversityService;
import security.securityscolarity.service.IMPL.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    TeacherService teacherService;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public String getTeacher(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("allTeacher", teacherService.findTeacherByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "teacher_list");
        return "UniversityAdmin/teacher/allTeacher";
    }

    @GetMapping("/detail")
    public String getTeacher(@RequestParam("teacherId") Long id, Model model) {
        Teacher teacher = teacherService.findByTeacherID(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("currentUrl", "teacher_detail");
        return "UniversityAdmin/teacher/detailTeacher";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("subjects",subjectService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("action","Add");
        model.addAttribute("specialite", Specialite.values());
        model.addAttribute("currentUrl", "teacher_add");
        return "UniversityAdmin/teacher/formTeacher";
    }

    @PostMapping("/addTeacher")
    public String addTeacher(@ModelAttribute Teacher teacher,
                             @RequestParam(name = "subjectIds", required = false) List<Long> subjectIds,
                             RedirectAttributes redirectAttributes) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            teacher.setUniversity(universityAdmin.getUniversity());
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
        }

        redirectAttributes.addFlashAttribute("successMessage", "Teacher added");
        return "redirect:/teachers/all";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("teacherId") Long id) {
        teacherService.deleteTeacher(id);
        return "redirect:/teacher/all";
    }

    @GetMapping("/assignSubjects")
    public String assignSubjects(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("teachers",teacherService.findTeacherByUniversity(universityAdmin.getUniversity()));
            model.addAttribute("subjects", subjectService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "assign_teacher_subjects");
        return "UniversityAdmin/teacher/assignSubjects";
    }

    @PostMapping("/assignTeachersToSubjects")
    public String assignTeachersToSubjects(@RequestParam(name = "teacherIds", required = false) List<Long> teacherIds,
                                           @RequestParam(name = "subjectIds", required = false) List<Long> subjectIds,
                                           RedirectAttributes redirectAttributes) {
        if (teacherIds == null || teacherIds.isEmpty() || subjectIds == null || subjectIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Teachers or subjects cannot be empty.");
            return "redirect:/teachers/all";
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
        return "redirect:/teachers/all";
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
        return "UniversityAdmin/teacher/formTeacher";
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
        return "redirect:/teachers/all";
    }
}