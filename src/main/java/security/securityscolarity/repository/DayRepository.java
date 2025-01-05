package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.securityscolarity.entity.Day;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {
    Day findByDayId(Long id);
    Day findByDayName(String dayName);
}
