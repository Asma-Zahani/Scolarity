package mywebsite.scolarite.restController;

import mywebsite.scolarite.entity.Chrono;
import mywebsite.scolarite.service.IMPL.ChronoService;
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

    @GetMapping("/{chronoId}")
    public Chrono getChronoById(@PathVariable("chronoId") Long id) {
        return chronoService.findByChronoID(id);
    }

    @PostMapping
    public Chrono addChrono(@RequestBody Chrono chrono) {
        return chronoService.addChrono(chrono);
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
