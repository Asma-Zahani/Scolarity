package mywebsite.scolarite.repository;

import mywebsite.scolarite.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    public Subject findBySubjectId(Long id);
}
