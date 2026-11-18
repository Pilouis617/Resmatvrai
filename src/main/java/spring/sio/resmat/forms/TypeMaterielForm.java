package spring.sio.resmat.forms;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import spring.sio.resmat.entities.TypeMateriel;

import java.util.UUID;

public class TypeMaterielForm {

    @NotBlank(message = "Le nom est obligatoire.")
    @Size(max = 70, message = "Le nom ne doit pas dépasser 70 caractères.")
    private String nom;

    @Size(max = 6500)
    private String description;

    @NotNull(message = "La durée maximale est obligatoire.")
    @Min(value = 1, message = "La durée maximale doit être d'au moins 1 jour.")
    @Max(value = 365, message = "La durée maximale ne peut pas dépasser 365 jours.")
    private Integer dureeMax = 30;

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

    public static TypeMaterielForm depuis(TypeMateriel type) {
        TypeMaterielForm form = new TypeMaterielForm();
        form.setNom(type.getNom());
        form.setDescription(type.getDescription());
        form.setDureeMax(type.getDureeMax());
        form.setCategorieId(type.getCategorie().getId());
        form.setAuCatalogue(type.isAuCatalogue());
        return form;
    }
}
