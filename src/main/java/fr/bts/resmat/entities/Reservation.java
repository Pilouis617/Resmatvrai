package fr.bts.resmat.entities;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "Reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) public UUID id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "emprunteur_id") public Utilisateur emprunteur;
    @ManyToOne
    @JoinColumn(name = "gestionnaire_id") public Utilisateur gestionnaire;
    @ManyToOne(optional = false)
    @JoinColumn(name = "typeMateriel_id") public TypeMateriel typeMateriel;
    @Column(nullable = false) public LocalDateTime dateDebut;
    @Column(nullable = false) public LocalDateTime dateFin;
    public LocalDateTime dateResa=LocalDateTime.now();
    @Column(length = 2000) public String commentaire;
    public Boolean avis=null;
    @Column(length = 2000) public String motivationAvis;
    public boolean annulee=false;
    public String getStatut() {
        return annulee ?"Annulée":avis==null?"En attente":avis?"Acceptée":"Refusée";
    }
}
