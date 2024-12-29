package mywebsite.scolarite.repository;

import mywebsite.scolarite.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findScheduleByIdGroup(Group group);
    List<Schedule> findScheduleByTeacher(Teacher teacher);
    List<Schedule> findScheduleByIdChronoAndIdDay(Chrono chrono, Day day);
}
