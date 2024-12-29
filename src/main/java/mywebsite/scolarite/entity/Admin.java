package mywebsite.scolarite.entity;

import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;

@Entity
public class Admin extends User implements Serializable {
}
