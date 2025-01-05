package security.securityscolarity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String token;

    @NonNull
    @Column(nullable = false)
    private Long userId;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime expirationTime;

}
