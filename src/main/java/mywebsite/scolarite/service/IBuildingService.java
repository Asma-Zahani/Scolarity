package mywebsite.scolarite.service;

import mywebsite.scolarite.entity.Building;

import java.util.List;

public interface IBuildingService {

    public List<Building> findAll();
    public Building findByBuildingID(Long id);
    public Building addBuilding(Building building);
    public void deleteBuilding(Long id);
    public Building updateBuilding(Long id , Building building);

}
