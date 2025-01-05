package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.securityscolarity.entity.Building;
import security.securityscolarity.entity.University;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    Building findByBuildingId(Long id);
    List<Building> findByUniversity(University university);
}
