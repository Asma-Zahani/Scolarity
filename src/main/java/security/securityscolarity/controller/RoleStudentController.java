package security.securityscolarity.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import security.securityscolarity.entity.*;
import security.securityscolarity.service.IMPL.*;

import java.util.*;

@Controller
@RequestMapping("/student")
public class RoleStudentController {
    @Autowired
    StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private UserService userService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private ChronoService chronoService;
    @Autowired
    private DayService dayService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String studentIndex(Model model, HttpServletRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        int subjectCount = 0;
        int studentCount = 0;
        List<Schedule> listSchedules = new ArrayList<>();
        if (user instanceof Student student) {
            model.addAttribute("user", student);
            if (student.getSubGroup() != null) {
                subjectCount = subjectService.getSubjectCountByGroup(student.getSubGroup().getGroup().getGroupId());
                studentCount = studentService.countStudentsByGroupExcludingCurrentUser(student.getSubGroup().getGroup().getGroupId(), user.getId());
                List<Subject> allSubjects = subjectService.findByGroup(student.getSubGroup().getGroup());
                List<Subject> limitedSubjects = allSubjects.size() > 5 ? allSubjects.subList(0, 5) : allSubjects;
                model.addAttribute("listSubjects", limitedSubjects);
                listSchedules = student.getSubGroup().getGroup().getSchedules();
            }
        }
        String currentUrl = request.getRequestURI();
        model.addAttribute("students", studentCount);
        model.addAttribute("subjects", subjectCount);
        model.addAttribute("schedules", listSchedules);
        model.addAttribute("currentUrl", currentUrl);
        return "Student/index";
    }

    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public String Schedule(Model model, HttpServletRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        int subjectCount = 0;
        int studentCount = 0;
        if (user instanceof Student student) {
            model.addAttribute("user", student);
            subjectCount = subjectService.getSubjectCountByGroup(student.getSubGroup().getGroup().getGroupId());
            studentCount = studentService.countStudentsByGroupExcludingCurrentUser(student.getSubGroup().getGroup().getGroupId(), user.getId());

            List<Schedule> schedules = student.getSubGroup().getGroup().getSchedules();
            Map<String, Map<String, Map<String, List<Schedule>>>> scheduleMap = new HashMap<>();
            boolean toggleGroup = true;

            for (Schedule schedule : schedules) {
                String chronoName = schedule.getId().getChrono().getChronoName();
                String dayName = schedule.getId().getDay().getDayName();

                scheduleMap.putIfAbsent(chronoName, new HashMap<>());
                scheduleMap.get(chronoName).putIfAbsent(dayName, new HashMap<>());

                String groupSession;
                if ("CI".equals(schedule.getId().getSubject().getSession())) {
                    groupSession = "T";
                } else if ("TP".equals(schedule.getId().getSubject().getSession())) {
                    groupSession = toggleGroup ? "Gp1" : "Gp2";
                    toggleGroup = !toggleGroup;
                } else {
                    groupSession = "";
                }

                scheduleMap.get(chronoName).get(dayName).putIfAbsent(groupSession, new ArrayList<>());
                scheduleMap.get(chronoName).get(dayName).get(groupSession).add(schedule);
            }

            model.addAttribute("schedules", scheduleMap);
            model.addAttribute("chronos", chronoService.findByUniversity(student.getUniversity()));
            model.addAttribute("days", dayService.findAll().stream().sorted(Comparator.comparingInt(Day::getDayNumber)).toList());

        }
        String currentUrl = request.getRequestURI();

        model.addAttribute("students", studentCount);
        model.addAttribute("subjects", subjectCount);
        model.addAttribute("currentUrl", currentUrl);
        return "Student/schedule";
    }

    @RequestMapping(value = "/teachers", method = RequestMethod.GET)
    public String Teachers(Model model, HttpServletRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        int subjectCount = 0;
        int studentCount = 0;
        List<Schedule> listSchedules = new ArrayList<>();
        if (user instanceof Student student) {
            model.addAttribute("user", student);
            if (student.getSubGroup() != null) {
                model.addAttribute("teachers", teacherService.findBySchedules_IdGroup(student.getSubGroup().getGroup()));
                subjectCount = subjectService.getSubjectCountByGroup(student.getSubGroup().getGroup().getGroupId());
                studentCount = studentService.countStudentsByGroupExcludingCurrentUser(student.getSubGroup().getGroup().getGroupId(), user.getId());
                listSchedules = student.getSubGroup().getGroup().getSchedules();
            }
        }
        model.addAttribute("students", studentCount);
        model.addAttribute("subjects", subjectCount);
        model.addAttribute("schedules", listSchedules);
        model.addAttribute("currentUrl", request.getRequestURI());
        return "Student/teachers";
    }

    @RequestMapping(value = "/subjects", method = RequestMethod.GET)
    public String Subjects(Model model, HttpServletRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        int subjectCount = 0;
        int studentCount = 0;
        List<Schedule> listSchedules = new ArrayList<>();
        if (user instanceof Student student) {
            model.addAttribute("user", student);
            if (student.getSubGroup() != null) {
                subjectCount = subjectService.getSubjectCountByGroup(student.getSubGroup().getGroup().getGroupId());
                studentCount = studentService.countStudentsByGroupExcludingCurrentUser(student.getSubGroup().getGroup().getGroupId(), user.getId());
                model.addAttribute("listSubjects", subjectService.findByGroup(student.getSubGroup().getGroup()));
                listSchedules = student.getSubGroup().getGroup().getSchedules();
            }
        }
        String currentUrl = request.getRequestURI();
        model.addAttribute("students", studentCount);
        model.addAttribute("subjects", subjectCount);
        model.addAttribute("schedules", listSchedules);
        model.addAttribute("currentUrl", currentUrl);
        return "Student/subjects";
    }

    @RequestMapping(value = "/subject/{subjectName}", method = RequestMethod.GET)
    public String Subject(Model model, HttpServletRequest request,  @RequestParam("subjectId") Long id) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        int subjectCount = 0;
        int studentCount = 0;
        List<Schedule> listSchedules = new ArrayList<>();
        if (user instanceof Student student) {
            model.addAttribute("user", student);
            if (student.getSubGroup() != null) {
                subjectCount = subjectService.getSubjectCountByGroup(student.getSubGroup().getGroup().getGroupId());
                studentCount = studentService.countStudentsByGroupExcludingCurrentUser(student.getSubGroup().getGroup().getGroupId(), user.getId());
                listSchedules = student.getSubGroup().getGroup().getSchedules();
                model.addAttribute("subject", subjectService.findBySubjectID(id));

            }
        }
        String currentUrl = request.getRequestURI();
        model.addAttribute("students", studentCount);
        model.addAttribute("subjects", subjectCount);
        model.addAttribute("schedules", listSchedules);
        model.addAttribute("currentUrl", currentUrl);
        return "Student/subject";
    }

    @RequestMapping(value = "/classmates", method = RequestMethod.GET)
    public String Classmates(Model model, HttpServletRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        int subjectCount = 0;
        int studentCount = 0;
        List<Student> studentsInSameGroup = new ArrayList<>();
        List<Schedule> listSchedules = new ArrayList<>();
        if (user instanceof Student student) {
            model.addAttribute("user", student);
            if (student.getSubGroup() != null) {
                subjectCount = subjectService.getSubjectCountByGroup(student.getSubGroup().getGroup().getGroupId());
                studentCount = studentService.countStudentsByGroupExcludingCurrentUser(student.getSubGroup().getGroup().getGroupId(), user.getId());
                studentsInSameGroup = studentService.findStudentsByGroupExcludingCurrentUser(student.getSubGroup().getGroup().getGroupId(), user.getId());
                listSchedules = student.getSubGroup().getGroup().getSchedules();
            }
        }
        String currentUrl = request.getRequestURI();
        model.addAttribute("classmates", studentsInSameGroup);
        model.addAttribute("students", studentCount);
        model.addAttribute("subjects", subjectCount);
        model.addAttribute("schedules", listSchedules);
        model.addAttribute("currentUrl", currentUrl);
        return "Student/classmates";
    }
}
