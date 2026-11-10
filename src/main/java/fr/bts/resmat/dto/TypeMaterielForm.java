package fr.bts.resmat.dto;

import java.util.UUID;
import jakarta.validation.constraints.*;

public class TypeMaterielForm {
    @NotBlank public String nom;
    @NotBlank public String description;
    @NotNull
    @Min(1)
    @Max(365) public Integer dureeMaxEmprunt;
    @NotNull public UUID categorieId;
    public boolean auCatalogue=true;
}
