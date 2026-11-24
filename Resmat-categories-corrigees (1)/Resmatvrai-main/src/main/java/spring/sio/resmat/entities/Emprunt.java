package spring.sio.resmat.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import java.time.LocalDateTime;

@Entity
public class Emprunt extends BaseEntity {

    private LocalDateTime dateRemise=LocalDateTime.now();

    private LocalDateTime dateRetour=null;

    @Column(length = 6500)
    private String etatAvant;

    @Column(length = 6500)
    private String etatApres;

    @OneToOne(mappedBy = "emprunt",optional = false)
    private Reservation reservation;

    @ManyToOne
    private Materiel materiel;

    public LocalDateTime getDateRemise() {
        return dateRemise;
    }

    public void setDateRemise(LocalDateTime dateRemise) {
        this.dateRemise = dateRemise;
    }

    public LocalDateTime getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(LocalDateTime dateRetour) {
        this.dateRetour = dateRetour;
    }

    public String getEtatAvant() {
        return etatAvant;
    }

    public void setEtatAvant(String etatAvant) {
        this.etatAvant = etatAvant;
    }

    public String getEtatApres() {
        return etatApres;
    }

    public void setEtatApres(String etatApres) {
        this.etatApres = etatApres;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Materiel getMateriel() {
        return materiel;
    }

    public void setMateriel(Materiel materiel) {
        this.materiel = materiel;
    }

    // Le retour est prévu à la date de fin de la réservation
    public LocalDateTime getDateRetourPrevue() {
        return reservation == null ? null : reservation.getDateFin();
    }

    public boolean isEnRetard() {
        LocalDateTime prevue = getDateRetourPrevue();
        return dateRetour == null && prevue != null && LocalDateTime.now().isAfter(prevue);
    }

    public String getDateRemiseFr() {
        return Dates.fr(dateRemise);
    }

    public String getDateRetourFr() {
        return Dates.fr(dateRetour);
    }

    public String getDateRetourPrevueFr() {
        return Dates.fr(getDateRetourPrevue());
    }
}
