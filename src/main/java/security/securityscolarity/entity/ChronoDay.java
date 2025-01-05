package security.securityscolarity.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChronoDay implements Serializable {

    @EmbeddedId
    private ChronoDayId id;
}