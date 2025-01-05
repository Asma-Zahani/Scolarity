package security.securityscolarity.service.IMPL;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.ChronoDay;
import security.securityscolarity.entity.ChronoDayId;
import security.securityscolarity.entity.University;
import security.securityscolarity.repository.ChronoDayRepository;
import security.securityscolarity.service.IChronoDayService;

import java.util.List;

@Service
public class ChronoDayService implements IChronoDayService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    ChronoDayRepository chronoDayRepository;
    @Autowired
    private UniversityService universityService;

    public List<ChronoDay> findAll() {
        return chronoDayRepository.findAll();
    }

    @Override
    public ChronoDay findByChronoDayId(ChronoDayId id) {
        return chronoDayRepository.findChronoDayById(id);
    }

    @Override
    public ChronoDay addChronoDay(ChronoDay chronoDay) {
        return chronoDayRepository.save(chronoDay);
    }

    @Override
    public void deleteChronoDay(ChronoDayId id) {
        chronoDayRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteChronoDayByChronoId(Long id) {
        String sqlQuery = "DELETE FROM chrono_day WHERE chrono_id = ?";
        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter(1, id);
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void deleteChronoDayByDayId(Long id) {
        String sqlQuery = "DELETE FROM chrono_day WHERE day_id = ?";
        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter(1, id);
        query.executeUpdate();
    }

    @Override
    public ChronoDay updateChronoDay(ChronoDayId id, ChronoDay chronoDay) {
        ChronoDay chronoDayToUpdate = findByChronoDayId(id);
        return chronoDayRepository.save(chronoDayToUpdate);
    }

    public List<ChronoDay> findByUniversity(University university) {
        String sqlQuery = "SELECT * FROM chrono_day JOIN chrono using(chrono_id) WHERE university_id = ?";
        Query query = entityManager.createNativeQuery(sqlQuery, ChronoDay.class);
        query.setParameter(1, university.getUniversityId());

        return query.getResultList();
    }

    public List<ChronoDay> findByUniversityId(Long id) {
        String sqlQuery = "SELECT * FROM chrono_day JOIN chrono using(chrono_id) WHERE university_id = ?";
        Query query = entityManager.createNativeQuery(sqlQuery, ChronoDay.class);
        query.setParameter(1, universityService.findByUniversityID(id).getUniversityId());

        return query.getResultList();
    }
}
