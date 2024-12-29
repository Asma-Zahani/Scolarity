package mywebsite.scolarite.controller;

import mywebsite.scolarite.entity.Student;
import mywebsite.scolarite.service.IMPL.StudentService;
import mywebsite.scolarite.service.IMPL.SubGroupService;
import mywebsite.scolarite.service.IMPL.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private SubGroupService subGroupService;

    @GetMapping("/all")
    public String getStudent(Model model) {
        List<Student> allStudent = studentService.findAll();
        model.addAttribute("allStudent", allStudent);
        model.addAttribute("currentUrl", "student_list");
        return "student/allStudent";
    }

    @GetMapping("/detail")
    public String getStudent(@RequestParam("studentId") Long id, Model model) {
        Student student = studentService.findByStudentID(id);
        model.addAttribute("student", student);
        model.addAttribute("currentUrl", "student_detail");
        return "student/detailStudent";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("action","Add");
        model.addAttribute("universities", universityService.findAll());
        model.addAttribute("subGroups", subGroupService.findAll());
        model.addAttribute("currentUrl", "student_add");
        return "student/formStudent";
    }

    @PostMapping("/addStudent")
    public String addStudent(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        studentService.addStudent(student);
        redirectAttributes.addFlashAttribute("successMessage", "Student added");
        return "redirect:/student/all";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("studentId") Long id) {
        studentService.deleteStudent(id);
        return "redirect:/student/all";
    }

    @GetMapping("/update")
    public String updateStudent(@RequestParam("studentId") Long id, Model model) {
        Student student = studentService.findByStudentID(id);
        model.addAttribute("student", student);
        model.addAttribute("action","Update");
        model.addAttribute("universities", universityService.findAll());
        model.addAttribute("subGroups", subGroupService.findAll());
        model.addAttribute("currentUrl", "student_update");
        return "student/formStudent";
    }

    @PostMapping("/updateStudent")
    public String updateStudent(@ModelAttribute Student student) {
        studentService.updateStudent(student.getId(),student);
        return "redirect:/student/all";
    }
}