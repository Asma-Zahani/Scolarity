package security.securityscolarity.restController;

import security.securityscolarity.entity.Chrono;
import security.securityscolarity.entity.ChronoDay;
import security.securityscolarity.entity.ChronoDayId;
import security.securityscolarity.entity.Group;
import security.securityscolarity.service.IMPL.ChronoDayService;
import security.securityscolarity.service.IMPL.ChronoService;
import security.securityscolarity.service.IMPL.DayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/chronoDays")
public class RestChronoDayController {

    @Autowired
    ChronoDayService chronoDayService;
    @Autowired
    private ChronoService chronoService;
    @Autowired
    private DayService dayService;

    @GetMapping
    public List<ChronoDay> getAllChronoDays() {
        return chronoDayService.findAll();
    }

    @GetMapping("/university/{universityId}")
    public List<ChronoDay> getGroupsByUniversity(@PathVariable("universityId") Long id) {
        return chronoDayService.findByUniversityId(id);
    }

    @GetMapping("/{chronoId}/{dayId}")
    public ChronoDay getChronoDayById(@PathVariable("chronoId") Long chronoId,@PathVariable("dayId") Long dayId) {
        return chronoDayService.findByChronoDayId(new ChronoDayId(
                chronoService.findByChronoID(chronoId),
                dayService.findByDayID(dayId)
        ));
    }

    @PostMapping
    public ChronoDay addChronoDay(@RequestBody ChronoDay chronoDay) {
        return chronoDayService.addChronoDay(chronoDay);
    }

    @DeleteMapping("/{chronoId}/{dayId}")
    public String deleteChronoDay(@PathVariable("chronoId") Long chronoId,@PathVariable("dayId") Long dayId) {
        chronoDayService.deleteChronoDay(new ChronoDayId(
                chronoService.findByChronoID(chronoId),
                dayService.findByDayID(dayId)
        ));
        return "ChronoDay with chrono ID " + chronoId + " and day ID " + dayId + " deleted successfully";
    }

    @DeleteMapping("chrono/{chronoId}")
    public String deleteChronoDayByChronoId(@PathVariable("chronoId") Long chronoId) {
        chronoDayService.deleteChronoDayByChronoId(chronoId);
        return "ChronoDay with chrono ID " + chronoId + " deleted successfully";
    }

    @DeleteMapping("day/{dayId}")
    public String deleteChronoDayByDayId(@PathVariable("dayId") Long dayId) {
        chronoDayService.deleteChronoDayByDayId(dayId);
        return "ChronoDay with day ID " + dayId + " deleted successfully";
    }

    @PutMapping("/{chronoId}/{dayId}")
    public ChronoDay updateChronoDay(@PathVariable("chronoId") Long chronoId,@PathVariable("dayId") Long dayId, @RequestBody ChronoDay chronoDay) {
        return chronoDayService.updateChronoDay(new ChronoDayId(
                chronoService.findByChronoID(chronoId),
                dayService.findByDayID(dayId)
        ), chronoDay);
    }
}
