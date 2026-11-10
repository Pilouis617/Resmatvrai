package fr.bts.resmat.repository;

import java.time.*;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.bts.resmat.entities.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation,UUID> {
    List<Reservation> findByEmprunteurLoginOrderByDateResaDesc(String login);
    List<Reservation> findByAvisIsNullAndAnnuleeFalseOrderByDateResaAsc();
    List<Reservation> findByAvisTrueAndAnnuleeFalseOrderByDateDebutAsc();
    @Query("select r from Reservation r where r.typeMateriel.id = :typeId and r.avis = true and r.annulee = false and r.dateDebut < :fin and r.dateFin > :debut") List<Reservation> findConflits(UUID typeId, LocalDateTime debut, LocalDateTime fin);
}
