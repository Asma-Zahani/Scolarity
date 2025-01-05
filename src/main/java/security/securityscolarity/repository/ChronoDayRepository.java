package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.securityscolarity.entity.ChronoDay;
import security.securityscolarity.entity.ChronoDayId;
import security.securityscolarity.entity.University;

import java.util.List;

public interface ChronoDayRepository extends JpaRepository<ChronoDay, ChronoDayId> {
    ChronoDay findChronoDayById(ChronoDayId id);
}
