package mywebsite.scolarite.repository;

import mywebsite.scolarite.entity.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {
    public Day findByDayId(Long id);
}
