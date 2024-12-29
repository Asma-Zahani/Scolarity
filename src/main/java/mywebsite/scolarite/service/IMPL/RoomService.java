package mywebsite.scolarite.service.IMPL;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import mywebsite.scolarite.entity.Building;
import mywebsite.scolarite.entity.Room;
import mywebsite.scolarite.repository.BuildingRepository;
import mywebsite.scolarite.repository.RoomRepository;
import mywebsite.scolarite.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService implements IRoomService{
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    BuildingRepository buildingRepository;

    public List<Room> findAll() {
        return roomRepository.findAll();
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

    public List<Room> getRoomNotAssigned() {
        String sqlQuery = "SELECT * FROM `room` where building_id is null";
        System.out.println("SQL Query: " + sqlQuery);
        Query query = entityManager.createNativeQuery(sqlQuery, Room.class);
        return query.getResultList();
    }
}
