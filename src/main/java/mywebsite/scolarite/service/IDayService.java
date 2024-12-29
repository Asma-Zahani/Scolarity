package mywebsite.scolarite.service;

import mywebsite.scolarite.entity.Day;

import java.util.List;

public interface IDayService {

    public List<Day> findAll();
    public Day findByDayID(Long id);
    public Day addDay(Day day);
    public void deleteDay(Long id);
    public Day updateDay(Long id , Day day);

}
