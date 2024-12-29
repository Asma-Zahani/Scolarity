package mywebsite.scolarite.restController;

import mywebsite.scolarite.entity.Chrono;
import mywebsite.scolarite.entity.ChronoDay;
import mywebsite.scolarite.entity.ChronoDayId;
import mywebsite.scolarite.service.IMPL.ChronoDayService;
import mywebsite.scolarite.service.IMPL.ChronoService;
import mywebsite.scolarite.service.IMPL.DayService;
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

    @PutMapping("/{chronoId}/{dayId}")
    public ChronoDay updateChronoDay(@PathVariable("chronoId") Long chronoId,@PathVariable("dayId") Long dayId, @RequestBody ChronoDay chronoDay) {
        return chronoDayService.updateChronoDay(new ChronoDayId(
                chronoService.findByChronoID(chronoId),
                dayService.findByDayID(dayId)
        ), chronoDay);
    }
}
