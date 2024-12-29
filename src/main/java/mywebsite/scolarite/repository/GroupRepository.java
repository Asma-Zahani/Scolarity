package mywebsite.scolarite.repository;

import mywebsite.scolarite.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    public Group findByGroupId(Long id);
}
