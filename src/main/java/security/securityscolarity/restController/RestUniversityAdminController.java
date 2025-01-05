package security.securityscolarity.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import security.securityscolarity.entity.UniversityAdmin;
import security.securityscolarity.service.IMPL.UniversityAdminService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/universityAdmins")
public class RestUniversityAdminController {

    @Autowired
    UniversityAdminService universityAdminService;

    @GetMapping
    public List<UniversityAdmin> getAllUniversityAdmins() {
        return universityAdminService.findAll();
    }

    @GetMapping("/{universityAdminId}")
    public UniversityAdmin getUniversityAdminById(@PathVariable("universityAdminId") Long id) {
        return universityAdminService.findByUniversityAdminID(id);
    }

    @PostMapping
    public UniversityAdmin addUniversityAdmin(@RequestBody UniversityAdmin universityAdmin) {
        return universityAdminService.addUniversityAdmin(universityAdmin);
    }

    @DeleteMapping("/{universityAdminId}")
    public String deleteUniversityAdmin(@PathVariable("universityAdminId") Long id) {
        universityAdminService.deleteUniversityAdmin(id);
        return "UniversityAdmin with ID " + id + " deleted successfully";
    }

    @PutMapping("/{universityAdminId}")
    public UniversityAdmin updateUniversityAdmin(@PathVariable("universityAdminId") Long id, @RequestBody UniversityAdmin universityAdmin) {
        return universityAdminService.updateUniversityAdmin(id, universityAdmin);
    }
}
