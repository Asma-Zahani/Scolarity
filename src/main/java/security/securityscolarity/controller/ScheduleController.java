package security.securityscolarity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import security.securityscolarity.entity.*;
import security.securityscolarity.repository.ScheduleRepository;
import security.securityscolarity.service.IMPL.*;

import java.util.*;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;
    @Autowired
    DayService dayService;
    @Autowired
    ChronoService chronoService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;
    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/all")
    public String generate(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("schedules",scheduleService.testGenerateSchedule(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "schedule_list_");
        return "UniversityAdmin/schedule/allSchedule";
    }

    @GetMapping("/generateSchedule")
    public String generateSchedule(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            scheduleService.generateSchedule(universityAdmin.getUniversity());
            model.addAttribute("schedules", scheduleService.testGenerateSchedule(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "schedule_list_generate");
        return "redirect:/schedule/all";
    }

    @GetMapping("/regenerateSchedule")
    public String regenerateSchedule(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            scheduleRepository.deleteAll();
            scheduleService.generateSchedule(universityAdmin.getUniversity());
            model.addAttribute("schedules", scheduleService.testGenerateSchedule(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "schedule_list_generate");
        return "redirect:/schedule/all";
    }

    @GetMapping("/selectTeacher")
    public String selectTeacher(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("teachers",teacherService.findTeacherByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "schedule_select_teacher");
        return "UniversityAdmin/schedule/selectTeacher";
    }

    @PostMapping("/findScheduleByTeacher")
    public String findScheduleByTeacher(Model model, @RequestParam(name = "teacherId") Long teacherId) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            Teacher teacher = teacherService.findByTeacherID(teacherId);
            List<Schedule> schedules = teacher.getSchedules();
            Map<String, Map<String, Schedule>> scheduleMap = new HashMap<>();

            for (Schedule schedule : schedules) {
                String chronoName = schedule.getId().getChrono().getChronoName();
                String dayName = schedule.getId().getDay().getDayName();
                scheduleMap.putIfAbsent(chronoName, new HashMap<>());
                scheduleMap.get(chronoName).put(dayName, schedule);
            }
            model.addAttribute("teacher",teacherService.findByTeacherID(teacherId));
            model.addAttribute("scheduleMap", scheduleMap);
            model.addAttribute("chronos", chronoService.findByUniversity(universityAdmin.getUniversity()).stream().sorted(Comparator.comparingInt(chrono -> Integer.parseInt(chrono.getChronoName().substring(1)))).toList());
            model.addAttribute("days", dayService.findAll().stream().sorted(Comparator.comparingInt(Day::getDayNumber)).toList());
        }
        model.addAttribute("currentUrl", "schedule_select_teacher");
        return "UniversityAdmin/schedule/scheduleForTeacher";
    }

    @GetMapping("/selectGroup")
    public String selectGroup(Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
            model.addAttribute("groups",groupService.findByUniversity(universityAdmin.getUniversity()));
        }
        model.addAttribute("currentUrl", "schedule_select_group");
        return "UniversityAdmin/schedule/selectGroup";
    }

    @PostMapping("/findScheduleByGroup")
    public String findScheduleByGroup(Model model, @RequestParam(name = "groupId") Long groupId) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUserID(userDetail.getId());
        if (user instanceof UniversityAdmin universityAdmin) {
            model.addAttribute("user", universityAdmin);
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

            model.addAttribute("scheduleMap", scheduleMap);
            model.addAttribute("group",groupService.findByGroupID(groupId));
            model.addAttribute("chronos", chronoService.findByUniversity(universityAdmin.getUniversity()).stream().sorted(Comparator.comparingInt(chrono -> Integer.parseInt(chrono.getChronoName().substring(1)))).toList());
            model.addAttribute("days", dayService.findAll().stream().sorted(Comparator.comparingInt(Day::getDayNumber)).toList());
        }
        model.addAttribute("currentUrl", "schedule_select_group");
        return "UniversityAdmin/schedule/scheduleForGroup";
    }

}
