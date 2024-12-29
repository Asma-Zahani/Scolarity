package mywebsite.scolarite.controller;

import mywebsite.scolarite.entity.Student;
import mywebsite.scolarite.entity.Teacher;
import mywebsite.scolarite.entity.University;
import mywebsite.scolarite.service.IMPL.StudentService;
import mywebsite.scolarite.service.IMPL.TeacherService;
import mywebsite.scolarite.service.IMPL.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/university")
public class UniversityController {

    @Autowired
    UniversityService universityService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;

    @GetMapping("/all")
    public String getUniversities(Model model) {
        List<University> allUniversity = universityService.findAll();
        model.addAttribute("allUniversity", allUniversity);
        model.addAttribute("currentUrl", "university_list");
        return "university/allUniversity";
    }

    @GetMapping("/grid")
    public String getUniversitiesGrid(Model model) {
        List<University> allUniversity = universityService.findAll();
        model.addAttribute("allUniversity", allUniversity);
        model.addAttribute("currentUrl", "university_grid");
        return "university/gridUniversity";
    }

    @GetMapping("/detail")
    public String getUniversity(@RequestParam("universityId") Long id, Model model) {
        University university = universityService.findByUniversityID(id);
        model.addAttribute("university", university);
        model.addAttribute("teachers", new ArrayList<>(university.getTeachers()));
        model.addAttribute("currentUrl", "university_detail");
        return "university/detailUniversity";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("university", new University());
        model.addAttribute("action","Add");
        model.addAttribute("listTeachers", teacherService.getTeacherNotAssigned());
        model.addAttribute("listStudents", studentService.getStudentNotAssignedToUniversity());
        model.addAttribute("currentUrl", "university_add");
        return "university/formUniversity";
    }

    @PostMapping("/addUniversity")
    public String addUniversity(@ModelAttribute University university,
                                @RequestParam(name = "teacherIds", required = false) List<Long> teacherIds,
                                @RequestParam(name = "studentIds", required = false) List<Long> studentIds,
                                RedirectAttributes redirectAttributes) {
        university = universityService.addUniversity(university);
        System.out.println(teacherIds);
        System.out.println(studentIds);
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
        return "redirect:/university/all";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("universityId") Long id) {
        universityService.deleteUniversity(id);
        return "redirect:/university/all";
    }

    @GetMapping("/assignTeachers")
    public String assignTeachers(Model model) {
        model.addAttribute("universities",universityService.findAll());
        model.addAttribute("teachers",teacherService.getTeacherNotAssigned());
        model.addAttribute("currentUrl", "assign_subject_teacher");
        return "university/assignTeachers";
    }

    @GetMapping("/assignStudents")
    public String assignStudents(Model model) {
        model.addAttribute("universities",universityService.findAll());
        model.addAttribute("students",studentService.getStudentNotAssignedToUniversity());
        model.addAttribute("currentUrl", "assign_subject_teacher");
        return "university/assignStudents";
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
        return "redirect:/university/all";
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
        return "redirect:/university/all";
    }

    @GetMapping("/update")
    public String updateUniversity(@RequestParam("universityId") Long id, Model model) {
       University university = universityService.findByUniversityID(id);
       model.addAttribute("university", university);
       model.addAttribute("action","Update");
        model.addAttribute("listTeachers", teacherService.getTeacherNotAssigned());
        model.addAttribute("listStudents", studentService.getStudentNotAssignedToUniversity());
       model.addAttribute("currentUrl", "university_update");
       return "university/formUniversity";
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
        return "redirect:/university/all";
    }
}
