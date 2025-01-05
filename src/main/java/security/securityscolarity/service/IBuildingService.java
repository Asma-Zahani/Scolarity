package security.securityscolarity.service;

import security.securityscolarity.entity.Building;
import security.securityscolarity.entity.University;

import java.util.List;

public interface IBuildingService {

    List<Building> findAll();
    List<Building> findByUniversity(University university);
    List<Building> findByUniversityId(Long id);
    Building findByBuildingID(Long id);
    Building addBuilding(Building building);
    void deleteBuilding(Long id);
    Building updateBuilding(Long id , Building building);

}
