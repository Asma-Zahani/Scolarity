package security.securityscolarity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import security.securityscolarity.entity.CustomUserDetail;
import security.securityscolarity.entity.Student;
import security.securityscolarity.entity.UniversityAdmin;
import security.securityscolarity.entity.User;
import security.securityscolarity.service.IMPL.StudentService;
import security.securityscolarity.service.IMPL.SubGroupService;
import security.securityscolarity.service.IMPL.UniversityService;
import security.securityscolarity.service.IMPL.UserService;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    StudentService studentService;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private SubGroupService subGroupService;
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public String getStudent(Model model) {CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("allStudent", studentService.findStudentByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "student_list");
        return "UniversityAdmin/student/allStudent";
    }

    @GetMapping("/detail")
    public String getStudent(@RequestParam("studentId") Long id, Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
        Student student = studentService.findByStudentID(id);
        model.addAttribute("student", student);
        model.addAttribute("currentUrl", "student_detail");
        return "UniversityAdmin/student/detailStudent";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("subGroups", subGroupService.findByGroupUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("student", new Student());
        model.addAttribute("action","Add");
        model.addAttribute("currentUrl", "student_add");
        return "UniversityAdmin/student/formStudent";
    }

    @PostMapping("/addStudent")
    public String addStudent(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            student.setUniversity(universityAdmin.getUniversity());
            studentService.addStudent(student);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Student added");
        return "redirect:/students/all";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("studentId") Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students/all";
    }

    @GetMapping("/update")
    public String updateStudent(@RequestParam("studentId") Long id, Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
        }
        Student student = studentService.findByStudentID(id);
        model.addAttribute("student", student);
        model.addAttribute("action","Update");
        model.addAttribute("universities", universityService.findAll());
        model.addAttribute("subGroups", subGroupService.findAll());
        model.addAttribute("currentUrl", "student_update");
        return "UniversityAdmin/student/formStudent";
    }

    @PostMapping("/updateStudent")
    public String updateStudent(@ModelAttribute Student student) {
        studentService.updateStudent(student.getId(),student);
        return "redirect:/students/all";
    }
}