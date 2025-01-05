package security.securityscolarity.service.IMPL;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.Admin;
import security.securityscolarity.entity.Role;
import security.securityscolarity.repository.AdminRepository;
import security.securityscolarity.service.IAdminService;

import java.util.Collections;
import java.util.List;

@Service
public class AdminService implements IAdminService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    AdminRepository adminRepository;

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    public Admin findByAdminID(Long id) {
        return adminRepository.findById(id).get();
    }

    public Admin addAdmin(Admin admin) {
        admin.setRoles(Collections.singletonList(Role.GLOBAL_ADMIN));
        admin.setPassword(new BCryptPasswordEncoder().encode(admin.getPassword()));
        return adminRepository.save(admin);
    }
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    public Admin updateAdmin(Long id , Admin admin) {
        Admin adminToUpdate = adminRepository.findById(id).get();
        adminToUpdate.setId(id);
        adminToUpdate.setFirstName(admin.getFirstName());
        adminToUpdate.setLastName(admin.getLastName());
        adminToUpdate.setEmail(admin.getEmail());
        adminToUpdate.setPassword(admin.getPassword());
        adminToUpdate.setActive(admin.isActive());
        return adminRepository.save(adminToUpdate);
    }

    public List<Admin> getAdminNotAssigned() {
        String sqlQuery = "SELECT * FROM `user` WHERE dtype = \"admin\" and university_id is null";
        Query query = entityManager.createNativeQuery(sqlQuery, Admin.class);
        return query.getResultList();
    }
}
