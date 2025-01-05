package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.securityscolarity.entity.University;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    public University findByUniversityId(Long id);
}
