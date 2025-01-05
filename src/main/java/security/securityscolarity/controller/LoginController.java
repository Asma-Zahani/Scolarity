package security.securityscolarity.controller;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import security.securityscolarity.entity.PasswordResetToken;
import security.securityscolarity.entity.User;
import security.securityscolarity.repository.PasswordResetTokenRepository;
import security.securityscolarity.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
public class LoginController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JavaMailSender emailSender;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/forgetPassword")
    public String forgetPassword(){
        return "forgetPassword";
    }

    @PostMapping("/forgetPassword")
    public String handleForgetPassword(@ModelAttribute("email") String email, HttpServletRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            request.setAttribute("error", "Aucun compte associé à cet e-mail.");
            return "forgetPassword";
        }
        User user = userOptional.get();

        String token = generateResetToken();

        PasswordResetToken resetToken = new PasswordResetToken(token, user.getId(), LocalDateTime.now().plusHours(1));
        passwordResetTokenRepository.save(resetToken);

        sendResetEmail(user, token);

        request.setAttribute("message", "Un lien de réinitialisation a été envoyé à votre adresse e-mail.");
        return "login";
    }

    public void sendResetEmail(User user, String token) {
        String subject = "Réinitialisation de mot de passe";
        String body = "Cliquez sur ce lien pour réinitialiser votre mot de passe : "
                + "http://localhost:8080/resetPassword?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);

        emailSender.send(message);
    }

    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }
}
