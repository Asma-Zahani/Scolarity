package security.securityscolarity.controller;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import security.securityscolarity.entity.User;
import security.securityscolarity.repository.UserRepository;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JavaMailSender mailSender;

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
        System.out.println(email);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            request.setAttribute("error", "Aucun compte associé à cet e-mail.");
            return "forgetPassword";
        }

        sendResetEmail(user.get());

        request.setAttribute("message", "Un lien de réinitialisation a été envoyé à votre adresse e-mail.");
        return "login";
    }

    private void sendResetEmail(User user) {
        String subject = "Forget Password";
        String body = """
            <html>
                <body>
                    <p>You have requested to recover your password.</p>
                    <p><b>Login:</b>""" + user.getEmail() + """
                    </p>
                    <p><b>Password:</b>""" + user.getPassword() + """
                    </p>
                    <p>Please log in and change your password immediately.</p>
                    <p>Best regards,<br>The Support Team.</p>
                </body>
            </html>
            """;

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(body, true); // Enable HTML content
            mailSender.send(message);
            System.out.println("Email sent to: " + user.getEmail());
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email to: " + user.getEmail());
        }
    }

}
