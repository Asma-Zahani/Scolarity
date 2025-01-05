package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.securityscolarity.entity.UniversityAdmin;

public interface UniversityAdminRepository extends JpaRepository<UniversityAdmin, Long> {
}
