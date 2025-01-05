package security.securityscolarity.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import security.securityscolarity.entity.Room;
import security.securityscolarity.entity.RoomConstraint;
import security.securityscolarity.entity.University;

import java.util.List;

public interface RoomConstraintRepository extends JpaRepository<RoomConstraint, Long> {
    RoomConstraint findRoomConstraintById(Long id);
    RoomConstraint findRoomConstraintByRoom(Room room);
    List<RoomConstraint> findRoomConstraintByRoomBuildingUniversity(University university);
}