package mywebsite.scolarite.service.IMPL;

import mywebsite.scolarite.entity.Day;
import mywebsite.scolarite.repository.DayRepository;
import mywebsite.scolarite.service.IDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DayService implements IDayService{
    @Autowired
    DayRepository dayRepository;

    public List<Day> findAll() {
        return dayRepository.findAll();
    }

    public Day findByDayID(Long id) {
        return dayRepository.findByDayId(id);
    }

    public Day addDay(Day Day) {
        return dayRepository.save(Day);
    }
    public void deleteDay(Long id) {
        dayRepository.deleteById(id);
    }

    public Day updateDay(Long id , Day Day) {
        Day dayToUpdate = dayRepository.findByDayId(id);
        dayToUpdate.setDayId(id);
        dayToUpdate.setDayName(Day.getDayName());
        return dayRepository.save(dayToUpdate);
    }
}
