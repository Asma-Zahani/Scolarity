package security.securityscolarity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RedirectController {

	@RequestMapping(value = "/universities", method = RequestMethod.GET)
	public String viewUniversityPage() {
		return "redirect:/universities/all";
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

	@RequestMapping(value = "/students", method = RequestMethod.GET)
	public String viewStudentPage() {
		return "redirect:/students/all";
	}

	@RequestMapping(value = "/subGroup", method = RequestMethod.GET)
	public String viewSubGroupPage() {
		return "redirect:/subGroup/all";
	}

	@RequestMapping(value = "/subject", method = RequestMethod.GET)
	public String viewSubjectPage() {
		return "redirect:/subject/all";
	}

	@RequestMapping(value = "/teachers", method = RequestMethod.GET)
	public String viewTeacherPage() {
		return "redirect:/teachers/all";
	}

	@RequestMapping(value = "/universityAdmins", method = RequestMethod.GET)
	public String viewUniversityAdminsPage() {
		return "redirect:/universityAdmins/all";
	}

	@RequestMapping(value = "/teacherConstraints", method = RequestMethod.GET)
	public String viewTeacherConstraintPage() {
		return "redirect:/teacherConstraints/all";
	}

	@RequestMapping(value = "/roomConstraints", method = RequestMethod.GET)
	public String viewRoomConstraintPage() {
		return "redirect:/roomConstraints/all";
	}

	@RequestMapping(value = "/groupConstraints", method = RequestMethod.GET)
	public String viewGroupConstraintPage() {
		return "redirect:/groupConstraints/all";
	}
}
