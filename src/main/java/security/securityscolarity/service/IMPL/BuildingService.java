package security.securityscolarity.service.IMPL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.Building;
import security.securityscolarity.entity.University;
import security.securityscolarity.repository.BuildingRepository;
import security.securityscolarity.repository.UniversityRepository;
import security.securityscolarity.service.IBuildingService;

import java.util.List;

@Service
public class BuildingService implements IBuildingService{
    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private UniversityRepository universityRepository;

    public List<Building> findAll() {
        return buildingRepository.findAll();
    }

    public List<Building> findByUniversity(University university) {
        return buildingRepository.findByUniversity(university);
    }

    public List<Building> findByUniversityId(Long id) {
        return buildingRepository.findByUniversity(universityService.findByUniversityID(id));
    }

    public Building findByBuildingID(Long id) {
        return buildingRepository.findByBuildingId(id);
    }

    public Building addBuilding(Building building) {
        return buildingRepository.save(building);
    }
    public Building addBuildingByUniversity(Building building, Long universityID) {
        building.setUniversity(universityService.findByUniversityID(universityID));
        return buildingRepository.save(building);
    }
    public void deleteBuilding(Long id) {
        buildingRepository.deleteById(id);
    }

    public Building updateBuilding(Long id , Building building) {
        Building buildingToUpdate = buildingRepository.findByBuildingId(id);
        buildingToUpdate.setBuildingId(id);
        buildingToUpdate.setBuildingName(building.getBuildingName());
        buildingToUpdate.setBuildingDescription(building.getBuildingDescription());
        return buildingRepository.save(buildingToUpdate);
    }
}
