package security.securityscolarity.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import security.securityscolarity.entity.*;
import security.securityscolarity.service.IMPL.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
public class RoleTeacherController {
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
    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String teacherIndex(Model model, HttpServletRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        int subjectCount = 0;
        int studentCount = 0;
        int distinctGroupCount = 0;
        List<Schedule> listSchedules = new ArrayList<>();
        List<Group> distinctGroups = new ArrayList<>();
        List<Subject> allSubjects = new ArrayList<>();
        if (user instanceof Teacher teacher) {
            model.addAttribute("user", teacher);
            if (teacher.getSubjects() != null) {
                subjectCount = subjectService.getSubjectCountByTeacher(teacher.getId());
                allSubjects = subjectService.findByTeacher(teacher).size() > 5 ? subjectService.findByTeacher(teacher).subList(0, 5) : subjectService.findByTeacher(teacher);
                listSchedules = teacher.getSchedules();
                distinctGroups = listSchedules.stream()
                        .map(schedule -> schedule.getId().getGroup()).distinct().toList();
                distinctGroupCount = (int) listSchedules.stream()
                        .map(schedule -> schedule.getId().getGroup()).distinct().count();
                studentCount = listSchedules.stream().map(schedule -> schedule.getId().getGroup())
                        .distinct().mapToInt(group -> studentService.countStudentsByGroup(group.getGroupId())).sum();
            }
        }
        String currentUrl = request.getRequestURI();
        model.addAttribute("listSubjects", allSubjects);
        model.addAttribute("distinctGroups", distinctGroups);
        model.addAttribute("distinctGroupCount", distinctGroupCount);
        model.addAttribute("students", studentCount);
        model.addAttribute("subjects", subjectCount);
        model.addAttribute("schedules", listSchedules);
        model.addAttribute("currentUrl", currentUrl);
        return "Teacher/index";
    }

    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public String Schedule(Model model, HttpServletRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        int subjectCount = 0;
        if (user instanceof Teacher teacher) {
            model.addAttribute("user", teacher);
            subjectCount = subjectService.getSubjectCountByTeacher(teacher.getId());
            List<Schedule> schedules = teacher.getSchedules();
            Map<String, Map<String, Schedule>> scheduleMap = new HashMap<>();

            for (Schedule schedule : schedules) {
                String chronoName = schedule.getId().getChrono().getChronoName();
                String dayName = schedule.getId().getDay().getDayName();
                scheduleMap.putIfAbsent(chronoName, new HashMap<>());
                scheduleMap.get(chronoName).put(dayName, schedule);
            }
            model.addAttribute("teacher", teacher);
            model.addAttribute("schedules", scheduleMap);
            model.addAttribute("chronos", chronoService.findByUniversity(teacher.getUniversity()));
            model.addAttribute("days", dayService.findAll().stream().sorted(Comparator.comparingInt(Day::getDayNumber)).toList());
        }
        String currentUrl = request.getRequestURI();
        model.addAttribute("subjects", subjectCount);
        model.addAttribute("currentUrl", currentUrl);
        return "Teacher/schedule";
    }

    @RequestMapping(value = "/teachers", method = RequestMethod.GET)
    public String Teachers(Model model, HttpServletRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        int subjectCount = 0;
        int studentCount = 0;
        List<Schedule> listSchedules = new ArrayList<>();
        if (user instanceof Teacher teacher) {
            model.addAttribute("user", teacher);
            model.addAttribute("teachers", teacherService.findTeacherByUniversity(teacher.getUniversity()));
            if (teacher.getSubjects() != null) {
                subjectCount = subjectService.getSubjectCountByTeacher(teacher.getId());
                List<Subject> allSubjects = subjectService.findByTeacher(teacher);
                List<Subject> limitedSubjects = allSubjects.size() > 5 ? allSubjects.subList(0, 5) : allSubjects;
                model.addAttribute("listSubjects", limitedSubjects);
                listSchedules = teacher.getSchedules();
            }
        }
        model.addAttribute("students", studentCount);
        model.addAttribute("subjects", subjectCount);
        model.addAttribute("schedules", listSchedules);
        model.addAttribute("currentUrl", request.getRequestURI());
        return "Teacher/teachers";
    }

    @RequestMapping(value = "/subjects", method = RequestMethod.GET)
    public String Subjects(Model model, HttpServletRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        int subjectCount = 0;
        int studentCount = 0;
        List<Schedule> listSchedules = new ArrayList<>();
        if (user instanceof Teacher teacher) {
            model.addAttribute("user", teacher);
            if (teacher.getSubjects() != null) {
                subjectCount = subjectService.getSubjectCountByTeacher(teacher.getId());
                List<Subject> allSubjects = subjectService.findByTeacher(teacher);
                model.addAttribute("listSubjects", allSubjects);
                listSchedules = teacher.getSchedules();
            }
        }
        String currentUrl = request.getRequestURI();
        model.addAttribute("students", studentCount);
        model.addAttribute("subjects", subjectCount);
        model.addAttribute("schedules", listSchedules);
        model.addAttribute("currentUrl", currentUrl);
        return "Teacher/subjects";
    }

    @RequestMapping(value = "/subject/{subjectName}", method = RequestMethod.GET)
    public String Subject(Model model, HttpServletRequest request,  @RequestParam("subjectId") Long id) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        int subjectCount = 0;
        int studentCount = 0;
        List<Schedule> listSchedules = new ArrayList<>();
        if (user instanceof Teacher teacher) {
            model.addAttribute("user", teacher);
            if (teacher.getSubjects() != null) {
                subjectCount = subjectService.getSubjectCountByTeacher(teacher.getId());
                listSchedules = teacher.getSchedules();
                studentCount = listSchedules.stream().map(schedule -> schedule.getId().getGroup())
                        .distinct().mapToInt(group -> studentService.countStudentsByGroup(group.getGroupId())).sum();
                model.addAttribute("subject", subjectService.findBySubjectID(id));
            }
        }
        String currentUrl = request.getRequestURI();
        model.addAttribute("students", studentCount);
        model.addAttribute("subjects", subjectCount);
        model.addAttribute("schedules", listSchedules);
        model.addAttribute("currentUrl", currentUrl);
        return "Teacher/subject";
    }

    @RequestMapping(value = "/subject/addFile", method = RequestMethod.GET)
    public String addFile(Model model, HttpServletRequest request, @RequestParam("subjectId") Long id) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        int subjectCount = 0;
        int studentCount = 0;
        List<Schedule> listSchedules = new ArrayList<>();
        if (user instanceof Teacher teacher) {
            model.addAttribute("user", teacher);
            if (teacher.getSubjects() != null) {
                subjectCount = subjectService.getSubjectCountByTeacher(teacher.getId());
                listSchedules = teacher.getSchedules();
                studentCount = listSchedules.stream().map(schedule -> schedule.getId().getGroup())
                        .distinct().mapToInt(group -> studentService.countStudentsByGroup(group.getGroupId())).sum();
                model.addAttribute("subject", subjectService.findBySubjectID(id));
            }
        }
        String currentUrl = request.getRequestURI();
        model.addAttribute("students", studentCount);
        model.addAttribute("subjects", subjectCount);
        model.addAttribute("schedules", listSchedules);
        model.addAttribute("currentUrl", currentUrl);
        return "Teacher/addFile";
    }

    @RequestMapping(value = {"/selectGroup", "/schedule/selectGroup"}, method = RequestMethod.GET)
    public String Students(Model model, HttpServletRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        int subjectCount = 0;
        List<Schedule> listSchedules = new ArrayList<>();
        List<Group> distinctGroups = new ArrayList<>();
        if (user instanceof Teacher teacher) {
            model.addAttribute("user", teacher);
            if (teacher.getSubjects() != null) {
                subjectCount = subjectService.getSubjectCountByTeacher(teacher.getId());
                listSchedules = teacher.getSchedules();
                distinctGroups = listSchedules.stream()
                        .map(schedule -> schedule.getId().getGroup()).distinct().toList();
            }
        }
        model.addAttribute("distinctGroups", distinctGroups);
        model.addAttribute("subjects", subjectCount);
        model.addAttribute("schedules", listSchedules);
        model.addAttribute("currentUrl", request.getRequestURI());
        return "Teacher/selectGroup";
    }


    @PostMapping("/schedule/findScheduleByGroup")
    public String findScheduleByGroup(Model model, @RequestParam(name = "groupId") Long groupId, HttpServletRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        int subjectCount = 0;
        if (user instanceof Teacher teacher) {
            model.addAttribute("user", teacher);
            subjectCount = subjectService.getSubjectCountByTeacher(teacher.getId());
            List<Schedule> schedules = groupService.findByGroupID(groupId).getSchedules();
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
            model.addAttribute("subjects", subjectCount);
            model.addAttribute("group",groupService.findByGroupID(groupId));
            model.addAttribute("chronos", chronoService.findByUniversity(teacher.getUniversity()));
            model.addAttribute("days", dayService.findAll().stream().sorted(Comparator.comparingInt(Day::getDayNumber)).toList());
        }
        model.addAttribute("currentUrl", request.getRequestURI());
        return "Teacher/scheduleForGroup";
    }

    @RequestMapping(value = "/students")
    public String SelectGroup(@RequestParam("groupId") Long groupId, Model model, HttpServletRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        int subjectCount = 0;
        List<Schedule> listSchedules = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        if (user instanceof Teacher teacher) {
            model.addAttribute("user", teacher);
            subjectCount = subjectService.getSubjectCountByTeacher(teacher.getId());
            listSchedules = teacher.getSchedules();
            students = studentService.findStudentsByGroup(groupId);
        }
        model.addAttribute("students", students);
        model.addAttribute("subjects", subjectCount);
        model.addAttribute("schedules", listSchedules);
        model.addAttribute("currentUrl", request.getRequestURI());
        return "Teacher/students";
    }

}
