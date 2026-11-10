package fr.bts.resmat.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

public class ReservationForm {
    @NotNull public UUID typeMaterielId;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) public LocalDateTime dateDebut;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) public LocalDateTime dateFin;
    public String commentaire;
}
