package security.securityscolarity.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import security.securityscolarity.entity.PasswordResetToken;
import security.securityscolarity.entity.User;
import security.securityscolarity.repository.PasswordResetTokenRepository;
import security.securityscolarity.repository.UserRepository;
import security.securityscolarity.service.PasswordResetService;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class PasswordResetController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @GetMapping("/resetPassword")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model, HttpServletRequest request) {
        Optional<PasswordResetToken> optionalToken = passwordResetTokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            request.setAttribute("error", "Token invalide.");
            return "resetPassword";
        }

        PasswordResetToken resetToken = optionalToken.get();

        if (resetToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            request.setAttribute("error", "Le token a expiré.");
            return "resetPassword";
        }

        model.addAttribute("token", token);
        return "resetPassword";
    }


    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword, HttpServletRequest request) {
        Optional<PasswordResetToken> optionalToken = passwordResetTokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            request.setAttribute("error", "Token invalide.");
            return "resetPassword";
        }

        PasswordResetToken resetToken = optionalToken.get();

        if (resetToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            request.setAttribute("error", "Le token a expiré.");
            return "resetPassword";
        }

        Optional<User> userOptional = userRepository.findById(resetToken.getUserId());
        if (userOptional.isEmpty()) {
            request.setAttribute("error", "Utilisateur non trouvé.");
            return "resetPassword";
        }

        User user = userOptional.get();

        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);

        request.setAttribute("message", "Mot de passe réinitialisé avec succès.");
        return "login";
    }

}
