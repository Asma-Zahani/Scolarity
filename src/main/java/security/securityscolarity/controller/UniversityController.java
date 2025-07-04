package security.securityscolarity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import security.securityscolarity.entity.*;
import security.securityscolarity.service.IMPL.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/universities")
public class UniversityController {

    @Autowired
    UniversityService universityService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private UniversityAdminService universityAdminService;
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public String getUniversities(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof Admin) {
            model.addAttribute("user", user);
        }
        List<University> allUniversity = universityService.findAll();
        model.addAttribute("allUniversity", allUniversity);
        model.addAttribute("currentUrl", "university_list");
        return "GlobalAdmin/university/allUniversity";
    }

    @GetMapping("/detail")
    public String getUniversity(@RequestParam("universityId") Long id, Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof Admin) {
            model.addAttribute("user", user);
        }
        University university = universityService.findByUniversityID(id);
        model.addAttribute("university", university);
        model.addAttribute("teachers", new ArrayList<>(university.getTeachers()));
        model.addAttribute("currentUrl", "university_detail");
        return "GlobalAdmin/university/detailUniversity";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof Admin) {
            model.addAttribute("user", user);
        }
        model.addAttribute("university", new University());
        model.addAttribute("action","Add");
        model.addAttribute("listTeachers", teacherService.getTeacherNotAssigned());
        model.addAttribute("listStudents", studentService.getStudentNotAssignedToUniversity());
        model.addAttribute("currentUrl", "university_add");
        return "GlobalAdmin/university/formUniversity";
    }

    @PostMapping("/addUniversity")
    public String addUniversity(@ModelAttribute University university,
                                @RequestParam(name = "teacherIds", required = false) List<Long> teacherIds,
                                @RequestParam(name = "studentIds", required = false) List<Long> studentIds,
                                RedirectAttributes redirectAttributes) {
        university = universityService.addUniversity(university);
        if (teacherIds != null && !teacherIds.isEmpty()) {
            for (Long id : teacherIds) {
                Teacher teacher = teacherService.findByTeacherID(id);
                if (teacher != null) {
                    teacher.setUniversity(university);
                    teacherService.updateTeacher(teacher.getId(), teacher);
                }
            }
        }
        if (studentIds != null && !studentIds.isEmpty()) {
            for (Long id : studentIds) {
                Student student = studentService.findByStudentID(id);
                if (student != null) {
                    student.setUniversity(university);
                    studentService.updateStudent(student.getId(), student);
                }
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "Université ajoutée avec succès!");
        return "redirect:/universities";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("universityId") Long id) {
        universityService.deleteUniversity(id);
        return "redirect:/universities";
    }

    @GetMapping("/assignTeachers")
    public String assignTeachers(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof Admin) {
            model.addAttribute("user", user);
        }
        model.addAttribute("universities",universityService.findAll());
        model.addAttribute("teachers",teacherService.getTeacherNotAssigned());
        model.addAttribute("currentUrl", "assign_university_teachers");
        return "GlobalAdmin/university/assignTeachers";
    }

    @GetMapping("/assignStudents")
    public String assignStudents(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof Admin) {
            model.addAttribute("user", user);
        }
        model.addAttribute("universities",universityService.findAll());
        model.addAttribute("students",studentService.getStudentNotAssignedToUniversity());
        model.addAttribute("currentUrl", "assign_university_students");
        return "GlobalAdmin/university/assignStudents";
    }

    @PostMapping("/assignTeachersToUniversity")
    public String assignTeachersToUniversity(@RequestParam Long universityId,
                                             @RequestParam(name = "teacherIds", required = false) List<Long> teacherIds,
                                             RedirectAttributes redirectAttributes) {
        University university = universityService.findByUniversityID(universityId);
        if (teacherIds != null && !teacherIds.isEmpty()) {
            for (Long id : teacherIds) {
                Teacher teacher = teacherService.findByTeacherID(id);
                if (teacher != null) {
                    teacher.setUniversity(university);
                    teacherService.updateTeacher(teacher.getId(), teacher);
                }
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "Subject assigned");
        return "redirect:/universities";
    }

    @PostMapping("/assignStudentsToUniversity")
    public String assignStudentsToUniversity(@RequestParam Long universityId,
                                             @RequestParam(name = "studentIds", required = false) List<Long> studentIds,
                                             RedirectAttributes redirectAttributes) {
        University university = universityService.findByUniversityID(universityId);
        if (studentIds != null && !studentIds.isEmpty()) {
            for (Long id : studentIds) {
                Student student = studentService.findByStudentID(id);
                if (student != null) {
                    student.setUniversity(university);
                    studentService.updateStudent(student.getId(), student);
                }
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "Subject assigned");
        return "redirect:/universities";
    }

    @GetMapping("/update")
    public String updateUniversity(@RequestParam("universityId") Long id, Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof Admin) {
            model.addAttribute("user", user);
        }
       University university = universityService.findByUniversityID(id);
       model.addAttribute("university", university);
       model.addAttribute("action","Update");
        model.addAttribute("listTeachers", teacherService.getTeacherNotAssigned());
        model.addAttribute("listStudents", studentService.getStudentNotAssignedToUniversity());
       model.addAttribute("currentUrl", "university_update");
       return "GlobalAdmin/university/formUniversity";
    }

    @PostMapping("/updateUniversity")
    public String updateUniversity(@ModelAttribute University university,
                                   @RequestParam(name = "teacherIds", required = false) List<Long> teacherIds,
                                   @RequestParam(name = "studentIds", required = false) List<Long> studentIds) {
        universityService.updateUniversity(university.getUniversityId(),university);
        if (teacherIds != null && !teacherIds.isEmpty()) {
            for (Long id : teacherIds) {
                Teacher teacher = teacherService.findByTeacherID(id);
                if (teacher != null) {
                    teacher.setUniversity(university);
                    teacherService.updateTeacher(teacher.getId(), teacher);
                }
            }
        }
        if (studentIds != null && !studentIds.isEmpty()) {
            for (Long id : studentIds) {
                Student student = studentService.findByStudentID(id);
                if (student != null) {
                    student.setUniversity(university);
                    studentService.updateStudent(student.getId(), student);
                }
            }
        }
        return "redirect:/universities";
    }
}
