package security.securityscolarity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.securityscolarity.entity.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByToken(String token);
}
