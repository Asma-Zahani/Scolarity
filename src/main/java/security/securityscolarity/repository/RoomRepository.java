package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.securityscolarity.entity.Room;
import security.securityscolarity.entity.University;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByRoomId(Long id);
    List<Room> findByBuildingUniversity(University university);
    long countByBuildingUniversity(University university);
}
