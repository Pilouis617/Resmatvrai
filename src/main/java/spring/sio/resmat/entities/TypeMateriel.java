package spring.sio.resmat.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class TypeMateriel extends BaseEntity {
    @Column(length = 70,nullable = false,unique = true)
    private String nom;

    @Column(length = 6500)
    private String description;

    private int dureeMax=30;

    private boolean auCatalogue=true;

    @ManyToOne(optional = false)
    private Categorie categorie;

    @OneToMany(mappedBy = "typeMateriel")
    private List<Materiel> materiels=new ArrayList<>();

    @OneToMany(mappedBy = "typeMateriel")
    private List<Reservation> reservations=new ArrayList<>();

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDureeMax() {
        return dureeMax;
    }

    public void setDureeMax(int dureeMax) {
        this.dureeMax = dureeMax;
    }

    public boolean isAuCatalogue() {
        return auCatalogue;
    }

    public void setAuCatalogue(boolean auCatalogue) {
        this.auCatalogue = auCatalogue;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public List<Materiel> getMateriels() {
        return materiels;
    }

    public void setMateriels(List<Materiel> materiels) {
        this.materiels = materiels;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    // Nombre d'exemplaires utilisables (en service et non supprimés)
    public long getNbExemplaires() {
        return materiels.stream().filter(m -> m.isDisponible() && !m.isDeleted()).count();
    }

    // Textes accordés au singulier ou au pluriel pour l'affichage
    public String getDureeMaxTexte() {
        return dureeMax + (dureeMax > 1 ? " jours" : " jour");
    }

    public String getNbExemplairesTexte() {
        long nb = getNbExemplaires();
        return nb + (nb > 1 ? " exemplaires" : " exemplaire");
    }
}
