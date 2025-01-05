package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.securityscolarity.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByEmail(String email);
}
