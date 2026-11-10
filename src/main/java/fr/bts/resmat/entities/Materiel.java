package fr.bts.resmat.entities;

import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "Materiel")
public class Materiel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) public UUID id;
    @Column(nullable = false) public String nom;
    @Column(length = 2000) public String description;
    public boolean disponible=true;
    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id") public TypeMateriel type;
}
