package fr.bts.resmat.entities;

import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "Categorie")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) public UUID id;
    @Column(nullable = false) public String nom;
    public Categorie() {
    }
    public Categorie(String nom) {
        this.nom=nom;
    }
}
