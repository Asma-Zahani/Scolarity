package mywebsite.scolarite.repository;

import mywebsite.scolarite.entity.Chrono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChronoRepository extends JpaRepository<Chrono, Long> {
    public Chrono findByChronoId(Long id);
}
