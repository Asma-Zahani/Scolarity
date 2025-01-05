package security.securityscolarity.service;

import security.securityscolarity.entity.Room;
import security.securityscolarity.entity.University;

import java.util.List;

public interface IRoomService {

    List<Room> findAll();
    List<Room> findByBuildingUniversity(University university);
    List<Room> findByBuildingUniversityId(Long universityId);
    Room findByRoomID(Long id);
    Room addRoom(Room room);
    void deleteRoom(Long id);
    Room updateRoom(Long id , Room room);
    void assignRoomToBuilding(Room room, Long id);
}
