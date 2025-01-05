package security.securityscolarity.service;

import security.securityscolarity.entity.Chrono;
import security.securityscolarity.entity.University;

import java.util.List;

public interface IChronoService {

    List<Chrono> findAll();
    List<Chrono> findByUniversity(University university);
    List<Chrono> findByUniversityId(Long universityId);
    Chrono findByChronoID(Long id);
    Chrono addChrono(Chrono chrono);
    void deleteChrono(Long id);
    Chrono updateChrono(Long id , Chrono chrono);
}
