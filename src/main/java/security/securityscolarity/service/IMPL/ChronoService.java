package security.securityscolarity.service.IMPL;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.Building;
import security.securityscolarity.entity.Chrono;
import security.securityscolarity.entity.University;
import security.securityscolarity.repository.ChronoRepository;
import security.securityscolarity.repository.ScheduleRepository;
import security.securityscolarity.service.IChronoService;

import java.util.List;

@Service
public class ChronoService implements IChronoService{
    @Autowired
    ChronoRepository chronoRepository;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Chrono> findAll() {
        return chronoRepository.findAll();
    }

    public List<Chrono> findByUniversity(University university) {
        return chronoRepository.findByUniversity(university);
    }

    public List<Chrono> findByUniversityId(Long id) {
        return chronoRepository.findByUniversity(universityService.findByUniversityID(id));
    }

    public Chrono findByChronoID(Long id) {
        return chronoRepository.findByChronoId(id);
    }

    public Chrono addChrono(Chrono Chrono) {
        return chronoRepository.save(Chrono);
    }

    public Chrono addChronoByUniversity(Chrono chrono, Long universityID) {
        chrono.setUniversity(universityService.findByUniversityID(universityID));
        return chronoRepository.save(chrono);
    }

    @Transactional
    public void deleteChrono(Long id) {
        scheduleRepository.deleteScheduleByIdChrono(chronoRepository.findByChronoId(id));
        chronoRepository.deleteById(id);
    }

    public Chrono updateChrono(Long id , Chrono Chrono) {
        Chrono chronoToUpdate = chronoRepository.findByChronoId(id);
        chronoToUpdate.setChronoId(id);
        chronoToUpdate.setChronoName(Chrono.getChronoName());
        chronoToUpdate.setChronoDescription(Chrono.getChronoDescription());
        chronoToUpdate.setStartTime(Chrono.getStartTime());
        chronoToUpdate.setEndTime(Chrono.getEndTime());
        return chronoRepository.save(chronoToUpdate);
    }
}
