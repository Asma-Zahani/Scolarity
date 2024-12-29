package mywebsite.scolarite.entity;

import jakarta.persistence.*;
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