package security.securityscolarity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.PasswordResetToken;
import security.securityscolarity.entity.User;
import security.securityscolarity.repository.PasswordResetTokenRepository;
import security.securityscolarity.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Valider le token et réinitialiser le mot de passe
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isPresent()) {
            PasswordResetToken resetToken = optionalToken.get();

            // Vérifier si le token est expiré
            if (resetToken.getExpirationTime().isAfter(LocalDateTime.now())) {
                Optional<User> userOptional = userRepository.findById(resetToken.getUserId());

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    user.setPassword(passwordEncoder.encode(newPassword));  // Hachage du mot de passe
                    userRepository.save(user);

                    // Supprimer le token après utilisation
                    tokenRepository.deleteByToken(token);
                    return true;  // Succès
                }
            }
        }
        return false;  // Échec (token invalide ou expiré)
    }
}
