package spring.sio.resmat.forms;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReservationForm {

    @NotNull
    private UUID typeMaterielId;

    @NotNull(message = "Indique la date de début.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateDebut;

    @NotNull(message = "Indique la date de fin.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateFin;

    @Size(max = 6500)
    private String motif;

    public UUID getTypeMaterielId() {
        return typeMaterielId;
    }

    public void setTypeMaterielId(UUID typeMaterielId) {
        this.typeMaterielId = typeMaterielId;
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

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }
}
