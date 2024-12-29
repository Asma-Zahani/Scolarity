package mywebsite.scolarite.controller;

import mywebsite.scolarite.service.IMPL.ChronoService;
import mywebsite.scolarite.service.IMPL.DayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import mywebsite.scolarite.entity.Schedule;
import mywebsite.scolarite.service.IMPL.ScheduleService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;
    @Autowired
    DayService dayService;
    @Autowired
    ChronoService chronoService;

    @GetMapping("/schedule")
    public String getSchedule(Model model) {
        List<Schedule> schedules = scheduleService.getScheduleForGroup(1L);
        Map<String, Map<String, Schedule>> scheduleMap = new HashMap<>();

        for (Schedule schedule : schedules) {
            String chronoName = schedule.getId().getChrono().getChronoName();
            String dayName = schedule.getId().getDay().getDayName();
            scheduleMap.putIfAbsent(chronoName, new HashMap<>());
            scheduleMap.get(chronoName).put(dayName, schedule);
        }

        model.addAttribute("scheduleMap", scheduleMap);
        model.addAttribute("chronos", chronoService.findAll());
        model.addAttribute("days", dayService.findAll());
        model.addAttribute("currentUrl", "schedule");

        return "schedules";
    }
}
