package mywebsite.scolarite.repository;

import mywebsite.scolarite.entity.ChronoDay;
import mywebsite.scolarite.entity.ChronoDayId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChronoDayRepository extends JpaRepository<ChronoDay, ChronoDayId> {
    ChronoDay findChronoDayById(ChronoDayId id);
}
