package security.securityscolarity.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import security.securityscolarity.entity.*;
import security.securityscolarity.service.IMPL.*;

import java.util.List;

@Controller
public class IndexController {

	@Autowired
	StudentService studentService;
	@Autowired
	TeacherService teacherService;
	@Autowired
	SubjectService subjectService;
	@Autowired
	RoomService roomService;
    @Autowired
    private UserService userService;
    @Autowired
    private UniversityService universityService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userService.findByUserID(userDetail.getId());
		if (user instanceof Admin) {
			return "redirect:/index";
		}
		if (user instanceof UniversityAdmin) {
			return "redirect:/universityAdmin/index";
		}
		if (user instanceof Teacher) {
			return "redirect:/teacher/index";
		}
		if (user instanceof Student) {
			return "redirect:/student/index";
		}
		return "redirect:/login";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String adminIndex(Model model, HttpServletRequest request)
	 {
		 String currentUrl = request.getRequestURI();
		 List<University> allUniversities = universityService.findAll();
		 List<University> limitedUniversities = allUniversities.size() > 5 ? allUniversities.subList(0, 5) : allUniversities;
		 model.addAttribute("universities",limitedUniversities);
		 model.addAttribute("nbUniversities", universityService.getUniversityCount());
		 model.addAttribute("nbStudents", studentService.getStudentCount());
		 model.addAttribute("nbTeachers", teacherService.getTeacherCount());
		 model.addAttribute("currentUrl", currentUrl);
		 return "GlobalAdmin/index";
	 }

	@RequestMapping(value = "universityAdmin/index", method = RequestMethod.GET)
	public String adminUniversityIndex(Model model, HttpServletRequest request) {
		CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userService.findByUserID(userDetail.getId());
		if (user instanceof UniversityAdmin universityAdmin) {
			model.addAttribute("students", studentService.countByUniversity(universityAdmin.getUniversity()));
			model.addAttribute("teachers", teacherService.countByUniversity(universityAdmin.getUniversity()));
			model.addAttribute("subjects", subjectService.countByUniversity(universityAdmin.getUniversity()));
			model.addAttribute("rooms", roomService.countByUniversity(universityAdmin.getUniversity()));
		}
		String currentUrl = request.getRequestURI();
		model.addAttribute("currentUrl", currentUrl);
		return "UniversityAdmin/index";
	}
}
