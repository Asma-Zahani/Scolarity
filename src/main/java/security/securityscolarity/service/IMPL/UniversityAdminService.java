package security.securityscolarity.service.IMPL;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.Role;
import security.securityscolarity.entity.UniversityAdmin;
import security.securityscolarity.repository.UniversityAdminRepository;
import security.securityscolarity.service.IUniversityAdminService;

import java.util.Collections;
import java.util.List;

@Service
public class UniversityAdminService implements IUniversityAdminService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    UniversityAdminRepository universityAdminRepository;

    public List<UniversityAdmin> findAll() {
        return universityAdminRepository.findAll();
    }

    public UniversityAdmin findByUniversityAdminID(Long id) {
        return universityAdminRepository.findById(id).get();
    }

    public UniversityAdmin addUniversityAdmin(UniversityAdmin universityAdmin) {
        universityAdmin.setRoles(Collections.singletonList(Role.UNIVERSITY_ADMIN));
        universityAdmin.setPassword(new BCryptPasswordEncoder().encode(universityAdmin.getPassword()));
        return universityAdminRepository.save(universityAdmin);
    }
    public void deleteUniversityAdmin(Long id) {
        universityAdminRepository.deleteById(id);
    }

    public UniversityAdmin updateUniversityAdmin(Long id , UniversityAdmin universityAdmin) {
        UniversityAdmin universityAdminToUpdate = universityAdminRepository.findById(id).get();
        universityAdminToUpdate.setId(id);
        universityAdminToUpdate.setFirstName(universityAdmin.getFirstName());
        universityAdminToUpdate.setLastName(universityAdmin.getLastName());
        universityAdminToUpdate.setEmail(universityAdmin.getEmail());
        universityAdminToUpdate.setPassword(universityAdmin.getPassword());
        universityAdminToUpdate.setActive(universityAdmin.isActive());
        universityAdminToUpdate.setUniversity(universityAdmin.getUniversity());
        return universityAdminRepository.save(universityAdminToUpdate);
    }
}
