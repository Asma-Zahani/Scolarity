package security.securityscolarity.service;

import security.securityscolarity.entity.GroupConstraint;
import security.securityscolarity.entity.RoomConstraint;
import security.securityscolarity.entity.University;

import java.util.List;

public interface IRoomConstraintService {

    List<RoomConstraint> findAll();
    List<RoomConstraint> findRoomConstraintByRoomBuildingUniversity(University university);
    List<RoomConstraint> findRoomConstraintByRoomBuildingUniversityId(Long id);
    RoomConstraint findByRoomConstraintID(Long id);
    RoomConstraint addRoomConstraint(RoomConstraint roomConstraint);
    void deleteRoomConstraint(Long id);
    RoomConstraint updateRoomConstraint(Long id , RoomConstraint roomConstraint);
}