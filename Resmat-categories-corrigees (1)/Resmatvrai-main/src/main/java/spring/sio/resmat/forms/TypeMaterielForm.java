package spring.sio.resmat.forms;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
<<<<<<< HEAD
import jakarta.validation.constraints.Size;
import spring.sio.resmat.entities.TypeMateriel;

=======
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import spring.sio.resmat.entities.TypeMateriel;

import java.util.UUID;

>>>>>>> 6f9cf89f8d123ffa2d061193b02d8b6c5644442e
public class TypeMaterielForm {

    @NotBlank(message = "Le nom est obligatoire.")
    @Size(max = 70, message = "Le nom ne doit pas dépasser 70 caractères.")
    private String nom;

    @Size(max = 6500)
    private String description;

<<<<<<< HEAD
=======
    @NotNull(message = "La durée maximale est obligatoire.")
>>>>>>> 6f9cf89f8d123ffa2d061193b02d8b6c5644442e
    @Min(value = 1, message = "La durée maximale doit être d'au moins 1 jour.")
    @Max(value = 365, message = "La durée maximale ne peut pas dépasser 365 jours.")
    private Integer dureeMax = 30;

<<<<<<< HEAD
    @NotBlank(message = "Saisis une catégorie.")
    @Size(max = 150, message = "La catégorie ne doit pas dépasser 150 caractères.")
    private String categorieLibelle;

    private boolean auCatalogue = true;

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getDureeMax() { return dureeMax; }
    public void setDureeMax(Integer dureeMax) { this.dureeMax = dureeMax; }

    public String getCategorieLibelle() { return categorieLibelle; }
    public void setCategorieLibelle(String categorieLibelle) { this.categorieLibelle = categorieLibelle; }

    public boolean isAuCatalogue() { return auCatalogue; }
    public void setAuCatalogue(boolean auCatalogue) { this.auCatalogue = auCatalogue; }
=======
    @NotNull(message = "Choisis une catégorie.")
    private UUID categorieId;

    private boolean auCatalogue = true;

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

    public Integer getDureeMax() {
        return dureeMax;
    }

    public void setDureeMax(Integer dureeMax) {
        this.dureeMax = dureeMax;
    }

    public UUID getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(UUID categorieId) {
        this.categorieId = categorieId;
    }

    public boolean isAuCatalogue() {
        return auCatalogue;
    }

    public void setAuCatalogue(boolean auCatalogue) {
        this.auCatalogue = auCatalogue;
    }
>>>>>>> 6f9cf89f8d123ffa2d061193b02d8b6c5644442e

    public static TypeMaterielForm depuis(TypeMateriel type) {
        TypeMaterielForm form = new TypeMaterielForm();
        form.setNom(type.getNom());
        form.setDescription(type.getDescription());
        form.setDureeMax(type.getDureeMax());
<<<<<<< HEAD
        form.setCategorieLibelle(type.getCategorie().getLibelle());
=======
        form.setCategorieId(type.getCategorie().getId());
>>>>>>> 6f9cf89f8d123ffa2d061193b02d8b6c5644442e
        form.setAuCatalogue(type.isAuCatalogue());
        return form;
    }
}
