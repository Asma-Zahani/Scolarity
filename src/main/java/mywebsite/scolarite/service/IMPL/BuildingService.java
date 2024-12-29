package mywebsite.scolarite.service.IMPL;

import jakarta.persistence.Query;
import mywebsite.scolarite.entity.Building;
import mywebsite.scolarite.entity.Room;
import mywebsite.scolarite.entity.Teacher;
import mywebsite.scolarite.repository.BuildingRepository;
import mywebsite.scolarite.service.IBuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingService implements IBuildingService{
    @Autowired
    BuildingRepository buildingRepository;

    public List<Building> findAll() {
        return buildingRepository.findAll();
    }

    public Building findByBuildingID(Long id) {
        return buildingRepository.findByBuildingId(id);
    }

    public Building addBuilding(Building Building) {
        return buildingRepository.save(Building);
    }
    public void deleteBuilding(Long id) {
        buildingRepository.deleteById(id);
    }

    public Building updateBuilding(Long id , Building Building) {
        Building buildingToUpdate = buildingRepository.findByBuildingId(id);
        buildingToUpdate.setBuildingId(id);
        buildingToUpdate.setBuildingName(Building.getBuildingName());
        buildingToUpdate.setBuildingDescription(Building.getBuildingDescription());
        return buildingRepository.save(buildingToUpdate);
    }
}
