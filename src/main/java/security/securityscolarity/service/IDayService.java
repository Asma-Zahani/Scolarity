package security.securityscolarity.service;

import security.securityscolarity.entity.Day;

import java.util.List;

public interface IDayService {

    List<Day> findAll();
    Day findByDayID(Long id);
    Day addDay(Day day);
    void deleteDay(Long id);
    Day updateDay(Long id , Day day);

}
