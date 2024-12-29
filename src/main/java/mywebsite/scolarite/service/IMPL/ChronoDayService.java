package mywebsite.scolarite.service.IMPL;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import mywebsite.scolarite.entity.*;
import mywebsite.scolarite.repository.ChronoDayRepository;
import mywebsite.scolarite.service.IChronoDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChronoDayService implements IChronoDayService{
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    ChronoDayRepository chronoDayRepository;

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
}
