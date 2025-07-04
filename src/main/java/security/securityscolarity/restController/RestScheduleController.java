package security.securityscolarity.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import security.securityscolarity.entity.Group;
import security.securityscolarity.entity.Schedule;
import security.securityscolarity.service.IMPL.GroupService;
import security.securityscolarity.service.IMPL.TeacherService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/schedule")
public class RestScheduleController {

    @Autowired
    GroupService groupService;
    @Autowired
    private TeacherService teacherService;

    @GetMapping("group/{groupId}")
    public Map<String, Map<String, Map<String, List<Schedule>>>> findScheduleByGroup(@PathVariable("groupId") Long groupId) {
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

        return scheduleMap;
    }

    @GetMapping("teacher/{teacherId}")
    public Map<String, Map<String, Schedule>> findScheduleByTeacher(@PathVariable("teacherId") Long teacherId) {
        List<Schedule> schedules = teacherService.findByTeacherID(teacherId).getSchedules();
        Map<String, Map<String, Schedule>> scheduleMap = new HashMap<>();

        for (Schedule schedule : schedules) {
            String chronoName = schedule.getId().getChrono().getChronoName();
            String dayName = schedule.getId().getDay().getDayName();
            scheduleMap.putIfAbsent(chronoName, new HashMap<>());
            scheduleMap.get(chronoName).put(dayName, schedule);
        }

        return scheduleMap;
    }
}