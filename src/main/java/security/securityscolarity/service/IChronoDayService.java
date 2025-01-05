package security.securityscolarity.service;

import security.securityscolarity.entity.ChronoDay;
import security.securityscolarity.entity.ChronoDayId;
import security.securityscolarity.entity.University;

import java.util.List;

public interface IChronoDayService {
    List<ChronoDay> findAll();
    ChronoDay findByChronoDayId(ChronoDayId id);
    ChronoDay addChronoDay(ChronoDay chronoDay);
    void deleteChronoDay(ChronoDayId id);
    void deleteChronoDayByChronoId(Long id);
    void deleteChronoDayByDayId(Long id);
    ChronoDay updateChronoDay(ChronoDayId id , ChronoDay chronoDay);
    List<ChronoDay> findByUniversity(University university);
    List<ChronoDay> findByUniversityId(Long universityId);
}
