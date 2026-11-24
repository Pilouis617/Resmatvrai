package spring.sio.resmat.forms;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import spring.sio.resmat.entities.TypeMateriel;

public class TypeMaterielForm {

    @NotBlank(message = "Le nom est obligatoire.")
    @Size(max = 70, message = "Le nom ne doit pas dépasser 70 caractères.")
    private String nom;

    @Size(max = 6500)
    private String description;

    @NotNull(message = "Indique la durée maximale.")
    @Min(value = 1, message = "La durée maximale doit être d'au moins 1 jour.")
    @Max(value = 365, message = "La durée maximale ne peut pas dépasser 365 jours.")
    private Integer dureeMax = 30;

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

    public static TypeMaterielForm depuis(TypeMateriel type) {
        TypeMaterielForm form = new TypeMaterielForm();
        form.setNom(type.getNom());
        form.setDescription(type.getDescription());
        form.setDureeMax(type.getDureeMax());
        form.setCategorieLibelle(type.getCategorie().getLibelle());
        form.setAuCatalogue(type.isAuCatalogue());
        return form;
    }
}
