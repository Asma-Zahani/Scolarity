package security.securityscolarity.service.IMPL;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.*;
import security.securityscolarity.repository.BuildingRepository;
import security.securityscolarity.repository.RoomRepository;
import security.securityscolarity.service.IRoomService;

import java.util.List;

@Service
public class RoomService implements IRoomService{

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    private UniversityService universityService;

    public long getRoomCount() {
        return roomRepository.count();
    }

    public long countByUniversity(University university) {
        return roomRepository.countByBuildingUniversity(university);
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public List<Room> findByBuildingUniversity(University university) {
        return roomRepository.findByBuildingUniversity(university);
    }

    public List<Room> findByBuildingUniversityId(Long id) {
        return roomRepository.findByBuildingUniversity(universityService.findByUniversityID(id));
    }

    public Room findByRoomID(Long id) {
        return roomRepository.findByRoomId(id);
    }

    public Room addRoom(Room Room) {
        return roomRepository.save(Room);
    }
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    public Room updateRoom(Long id , Room room) {
        Room roomToUpdate = roomRepository.findByRoomId(id);
        roomToUpdate.setRoomName(room.getRoomName());
        roomToUpdate.setRoomDescription(room.getRoomDescription());
        roomToUpdate.setCapacity(room.getCapacity());
        roomToUpdate.setBuilding(room.getBuilding());
        return roomRepository.save(roomToUpdate);
    }

    @Override
    public void assignRoomToBuilding(Room room, Long id) {
        Building building = buildingRepository.findByBuildingId(id);
        room.setBuilding(building);
        roomRepository.save(room);
    }
}
