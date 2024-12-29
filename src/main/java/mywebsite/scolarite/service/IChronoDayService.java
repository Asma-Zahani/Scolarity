package mywebsite.scolarite.service;

import mywebsite.scolarite.entity.Chrono;
import mywebsite.scolarite.entity.ChronoDay;
import mywebsite.scolarite.entity.ChronoDayId;
import mywebsite.scolarite.entity.Day;

import java.util.List;

public interface IChronoDayService {

    List<ChronoDay> findAll();
    ChronoDay findByChronoDayId(ChronoDayId id);
    ChronoDay addChronoDay(ChronoDay chronoDay);
    void deleteChronoDay(ChronoDayId id);
    void deleteChronoDayByChronoId(Long id);
    void deleteChronoDayByDayId(Long id);
    ChronoDay updateChronoDay(ChronoDayId id , ChronoDay chronoDay);
}
