package security.securityscolarity.service.IMPL;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.Student;
import security.securityscolarity.entity.Teacher;
import security.securityscolarity.entity.University;
import security.securityscolarity.repository.UniversityRepository;
import security.securityscolarity.service.IUniversityService;

import java.util.List;

@Service
public class UniversityService implements IUniversityService{

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    UniversityRepository universityRepository;

    public long getUniversityCount() {
        return universityRepository.count();
    }
    public List<University> findAll() {
        return universityRepository.findAll();
    }

    public University findByUniversityID(Long id) {
        return universityRepository.findByUniversityId(id);
    }

    public University addUniversity(University university) {
        return universityRepository.save(university);
    }
    public void deleteUniversity(Long id) {
        University university = universityRepository.findByUniversityId(id);
        if (university.getTeachers() != null) {
            for (Teacher teacher : university.getTeachers()) {
                teacher.setUniversity(null);
            }
        }
        if (university.getStudents() != null) {
            for (Student student : university.getStudents()) {
                student.setUniversity(null);
            }
        }
        if (university.getUniversityAdmin() != null) {
            university.getUniversityAdmin().setUniversity(null);
        }
        universityRepository.deleteById(id);
    }

    public University updateUniversity(Long id , University university) {
        University universityToUpdate = this.findByUniversityID(id);
        universityToUpdate.setUniversityName(university.getUniversityName());
        universityToUpdate.setUniversityDescription(university.getUniversityDescription());
        universityToUpdate.setUniversityAdmin(university.getUniversityAdmin());
        return universityRepository.save(universityToUpdate);
    }

    public List<University> getUniversityNotAssignedToAdminUniversity() {
        String sqlQuery = """
        SELECT u.* 
        FROM university u
        LEFT JOIN user us ON u.university_id = us.university_id
        WHERE us.university_id IS NULL
    """;
        Query query = entityManager.createNativeQuery(sqlQuery, University.class);
        return query.getResultList();
    }
}
