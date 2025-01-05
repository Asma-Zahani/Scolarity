package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.securityscolarity.entity.Room;
import security.securityscolarity.entity.SubGroup;
import security.securityscolarity.entity.University;

import java.util.List;

@Repository
public interface SubGroupRepository extends JpaRepository<SubGroup, Long> {
    public SubGroup findBySubGroupId(Long id);
    List<SubGroup> findByGroupUniversity(University university);
}
