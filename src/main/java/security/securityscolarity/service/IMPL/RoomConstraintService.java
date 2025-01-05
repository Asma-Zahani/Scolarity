package security.securityscolarity.service.IMPL;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.*;
import security.securityscolarity.repository.RoomConstraintRepository;
import security.securityscolarity.repository.RoomRepository;
import security.securityscolarity.service.IRoomConstraintService;

import java.util.List;
import java.util.Optional;

@Service
public class RoomConstraintService implements IRoomConstraintService {

    @Autowired
    private RoomConstraintRepository roomConstraintRepository;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<RoomConstraint> findAll() {
        return roomConstraintRepository.findAll();
    }

    public  RoomConstraint findRoomConstraintById(Room room){
        return roomConstraintRepository.findRoomConstraintByRoom(room);
    }
    public List<RoomConstraint> findRoomConstraintByRoomBuildingUniversity(University university) {
        return roomConstraintRepository.findRoomConstraintByRoomBuildingUniversity(university);
    }

    public List<RoomConstraint> findRoomConstraintByRoomBuildingUniversityId(Long id) {
        return roomConstraintRepository.findRoomConstraintByRoomBuildingUniversity(universityService.findByUniversityID(id));
    }

    @Override
    public RoomConstraint findByRoomConstraintID(Long id) {
        Optional<RoomConstraint> constraint = roomConstraintRepository.findById(id);
        if (constraint.isPresent()) {
            return constraint.get();
        } else {
            throw new RuntimeException("RoomConstraint with ID " + id + " not found.");
        }
    }

    @Override
    public RoomConstraint addRoomConstraint(RoomConstraint roomConstraint) {
        return roomConstraintRepository.save(roomConstraint);
    }

    @Override
    public void deleteRoomConstraint(Long id) {
        RoomConstraint constraint = roomConstraintRepository.findRoomConstraintById(id);

        Room room = constraint.getRoom();
        if (room != null) {
            room.setConstraint(null);
            roomRepository.save(room);
        }
        roomConstraintRepository.deleteById(id);
    }

    @Override
    public RoomConstraint updateRoomConstraint(Long id, RoomConstraint roomConstraint) {
        if (roomConstraintRepository.existsById(id)) {
            roomConstraint.setId(id);
            return roomConstraintRepository.save(roomConstraint);
        } else {
            throw new RuntimeException("RoomConstraint with ID " + id + " not found.");
        }
    }
}