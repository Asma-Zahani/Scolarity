package mywebsite.scolarite.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class IndexController {

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Model model, HttpServletRequest request)
	 {
		 String currentUrl = request.getRequestURI();
		 model.addAttribute("currentUrl", currentUrl);
		 return "index";
	 }

	@RequestMapping(value = "/university", method = RequestMethod.GET)
	public String viewUniversityPage() {
		return "redirect:/university/all";
	}

	@RequestMapping(value = "/building", method = RequestMethod.GET)
	public String viewBuildingPage() {
		return "redirect:/building/all";
	}

	@RequestMapping(value = "/chrono", method = RequestMethod.GET)
	public String viewChronoPage() {
		return "redirect:/chrono/all";
	}

	@RequestMapping(value = "/chronoDay", method = RequestMethod.GET)
	public String viewChronoDayPage() {
		return "redirect:/chronoDay/all";
	}

	@RequestMapping(value = "/day", method = RequestMethod.GET)
	public String viewDayPage() {
		return "redirect:/day/all";
	}

	@RequestMapping(value = "/group", method = RequestMethod.GET)
	public String viewGroupPage() {
		return "redirect:/group/all";
	}

	@RequestMapping(value = "/room", method = RequestMethod.GET)
	public String viewRoomPage() {
		return "redirect:/room/all";
	}

	@RequestMapping(value = "/session", method = RequestMethod.GET)
	public String viewSessionPage() {
		return "redirect:/sessions/all";
	}

	@RequestMapping(value = "/student", method = RequestMethod.GET)
	public String viewStudentPage() {
		return "redirect:/student/all";
	}

	@RequestMapping(value = "/subGroup", method = RequestMethod.GET)
	public String viewSubGroupPage() {
		return "redirect:/subGroup/all";
	}

	@RequestMapping(value = "/subject", method = RequestMethod.GET)
	public String viewSubjectPage() {
		return "redirect:/subject/all";
	}

	@RequestMapping(value = "/teacher", method = RequestMethod.GET)
	public String viewTeacherPage() {
		return "redirect:/teacher/all";
	}

}
