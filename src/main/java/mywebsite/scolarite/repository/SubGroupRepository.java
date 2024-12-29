package mywebsite.scolarite.repository;

import mywebsite.scolarite.entity.SubGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubGroupRepository extends JpaRepository<SubGroup, Long> {
    public SubGroup findBySubGroupId(Long id);
}
