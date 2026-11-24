package spring.sio.resmat.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
public class Reservation extends BaseEntity {

    @Column(nullable = true)
    private Boolean acceptee=null;

    @Column(length = 6500)
    private String motif;

    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;

    @ManyToOne(optional = true)
    private Utilisateur gestionnaire;

    @ManyToOne(optional = false)
    private Utilisateur emprunteur;

    @ManyToOne(optional = false)
    private TypeMateriel typeMateriel;

    @OneToOne(optional = true)
    private Emprunt emprunt;

    // Ajouts : annulation par l'emprunteur et motif donné par le gestionnaire
    @Column(nullable = false)
    @ColumnDefault("0")
    private boolean annulee = false;

    @Column(length = 6500)
    private String motifDecision;

    public Boolean getAcceptee() {
        return acceptee;
    }

    public void setAcceptee(Boolean acceptee) {
        this.acceptee = acceptee;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public Utilisateur getGestionnaire() {
        return gestionnaire;
    }

    public void setGestionnaire(Utilisateur gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    public Utilisateur getEmprunteur() {
        return emprunteur;
    }

    public void setEmprunteur(Utilisateur emprunteur) {
        this.emprunteur = emprunteur;
    }

    public TypeMateriel getTypeMateriel() {
        return typeMateriel;
    }

    public void setTypeMateriel(TypeMateriel typeMateriel) {
        this.typeMateriel = typeMateriel;
    }

    public Emprunt getEmprunt() {
        return emprunt;
    }

    public void setEmprunt(Emprunt emprunt) {
        this.emprunt = emprunt;
    }

    public boolean isAnnulee() {
        return annulee;
    }

    public void setAnnulee(boolean annulee) {
        this.annulee = annulee;
    }

    public String getMotifDecision() {
        return motifDecision;
    }

    public void setMotifDecision(String motifDecision) {
        this.motifDecision = motifDecision;
    }

    public boolean isEnAttente() {
        return acceptee == null && !annulee;
    }

    public boolean isAcceptable() {
        return Boolean.TRUE.equals(acceptee) && !annulee;
    }

    public String getStatut() {
        if (annulee) return "Annulée";
        if (acceptee == null) return "En attente";
        if (!acceptee) return "Refusée";
        if (emprunt == null) return "Acceptée";
        return emprunt.getDateRetour() == null ? "En cours" : "Rendue";
    }

    public String getStatutClasse() {
        return switch (getStatut()) {
            case "Annulée" -> "badge-cancelled";
            case "En attente" -> "badge-pending";
            case "Refusée" -> "badge-refused";
            case "En cours" -> "badge-in-progress";
            case "Rendue" -> "badge-returned";
            default -> "badge-accepted";
        };
    }

    public String getDateDebutFr() {
        return Dates.fr(dateDebut);
    }

    public String getDateFinFr() {
        return Dates.fr(dateFin);
    }

    public String getDateDemandeFr() {
        return Dates.fr(getCreatedAt());
    }
}
