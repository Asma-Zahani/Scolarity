package security.securityscolarity.restController;

import security.securityscolarity.entity.Day;
import security.securityscolarity.service.IMPL.DayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/days")
public class RestDayController {

    @Autowired
    DayService dayService;

    @GetMapping
    public List<Day> getAllDays() {
        return dayService.findAll();
    }

    @GetMapping("/{dayId}")
    public Day getDayById(@PathVariable("dayId") Long id) {
        return dayService.findByDayID(id);
    }

    @PostMapping
    public Day addDay(@RequestBody Day day) {
        return dayService.addDay(day);
    }

    @DeleteMapping("/{dayId}")
    public String deleteDay(@PathVariable("dayId") Long id) {
        dayService.deleteDay(id);
        return "Day with ID " + id + " deleted successfully";
    }

    @PutMapping("/{dayId}")
    public Day updateDay(@PathVariable("dayId") Long id, @RequestBody Day day) {
        return dayService.updateDay(id, day);
    }
}
