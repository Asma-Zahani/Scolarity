package mywebsite.scolarite.service;

import mywebsite.scolarite.entity.Room;
import mywebsite.scolarite.entity.Teacher;

import java.util.List;

public interface IRoomService {

    List<Room> findAll();
    Room findByRoomID(Long id);
    Room addRoom(Room room);
    void deleteRoom(Long id);
    Room updateRoom(Long id , Room room);
    void assignRoomToBuilding(Room room, Long id);
    List<Room> getRoomNotAssigned();
}
