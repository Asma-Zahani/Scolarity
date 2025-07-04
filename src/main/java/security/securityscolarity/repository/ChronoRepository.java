package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.securityscolarity.entity.Chrono;
import security.securityscolarity.entity.University;

import java.util.List;

@Repository
public interface ChronoRepository extends JpaRepository<Chrono, Long> {
    Chrono findByChronoId(Long id);
    Chrono findByChronoName(String chronoName);
    List<Chrono> findByUniversity(University university);
}
