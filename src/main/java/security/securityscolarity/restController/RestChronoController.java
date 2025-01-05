package security.securityscolarity.restController;

import security.securityscolarity.entity.Building;
import security.securityscolarity.entity.Chrono;
import security.securityscolarity.entity.Student;
import security.securityscolarity.service.IMPL.ChronoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/chronos")
public class RestChronoController {

    @Autowired
    ChronoService chronoService;

    @GetMapping
    public List<Chrono> getAllChronos() {
        return chronoService.findAll();
    }

    @GetMapping("/university/{universityId}")
    public List<Chrono> getChronosByUniversity(@PathVariable("universityId") Long id) {
        return chronoService.findByUniversityId(id);
    }

    @GetMapping("/{chronoId}")
    public Chrono getChronoById(@PathVariable("chronoId") Long id) {
        return chronoService.findByChronoID(id);
    }

    @PostMapping
    public Chrono addChrono(@RequestBody Chrono chrono) {
        return chronoService.addChrono(chrono);
    }

    @PostMapping("/{universityId}")
    public Chrono addChronoByUniversity(@PathVariable("universityId") Long id, @RequestBody Chrono chrono) {
        return chronoService.addChronoByUniversity(chrono,id);
    }

    @DeleteMapping("/{chronoId}")
    public String deleteChrono(@PathVariable("chronoId") Long id) {
        chronoService.deleteChrono(id);
        return "Chrono with ID " + id + " deleted successfully";
    }

    @PutMapping("/{chronoId}")
    public Chrono updateChrono(@PathVariable("chronoId") Long id, @RequestBody Chrono chrono) {
        return chronoService.updateChrono(id, chrono);
    }
}
