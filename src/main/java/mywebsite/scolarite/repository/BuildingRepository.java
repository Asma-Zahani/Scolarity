package mywebsite.scolarite.repository;

import mywebsite.scolarite.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    public Building findByBuildingId(Long id);
}
