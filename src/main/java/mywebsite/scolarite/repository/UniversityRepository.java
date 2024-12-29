package mywebsite.scolarite.repository;

import mywebsite.scolarite.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    public University findByUniversityId(Long id);
}
