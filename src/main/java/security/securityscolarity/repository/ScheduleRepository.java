package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.securityscolarity.entity.*;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findScheduleByTeacher(Teacher teacher);
    void deleteScheduleByIdChronoAndIdDay(Chrono chrono, Day day);
    void deleteScheduleByIdChrono(Chrono chrono);
}
