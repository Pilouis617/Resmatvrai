package spring.sio.resmat.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Utilisateur extends BaseEntity {
    @Column(length = 35)
    private String prenom;

    @Column(length = 35)
    private String nom;

    @Column(length = 20, unique = true)
    private String login;

    @Column(length = 255)
    private String motDePasse;

    @Column(length = 255,unique = true)
    private String email;

    @Column(nullable = false)
    private boolean gestionnaire=false;

    @OneToMany(mappedBy = "emprunteur")
    private List<Reservation> mesReservations=new ArrayList<>();

    @OneToMany(mappedBy = "gestionnaire")
    private List<Reservation> reservations=new ArrayList<>();

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isGestionnaire() {
        return gestionnaire;
    }

    public void setGestionnaire(boolean gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    public List<Reservation> getMesReservations() {
        return mesReservations;
    }

    public void setMesReservations(List<Reservation> mesReservations) {
        this.mesReservations = mesReservations;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
