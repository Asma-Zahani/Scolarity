package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.securityscolarity.entity.Chrono;
import security.securityscolarity.entity.ChronoDay;
import security.securityscolarity.entity.ChronoDayId;

import java.util.List;

public interface ChronoDayRepository extends JpaRepository<ChronoDay, ChronoDayId> {
    ChronoDay findChronoDayById(ChronoDayId id);
    List<ChronoDay> findChronoDaysByIdChrono(Chrono chrono);
}
