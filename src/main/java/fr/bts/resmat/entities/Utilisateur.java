package fr.bts.resmat.entities;

import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "Utilisateur")
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) public UUID id;
    @Column(nullable = false) public String nom;
    @Column(nullable = false) public String prenom;
    @Column(nullable = false,unique = true) public String email;
    @Column(nullable = false,unique = true) public String login;
    @Column(nullable = false) public String motDePasse;
    public boolean gestionnaire=false;
}
