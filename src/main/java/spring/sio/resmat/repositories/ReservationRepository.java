package spring.sio.resmat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.sio.resmat.entities.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    List<Reservation> findByEmprunteurLoginOrderByCreatedAtDesc(String login);

    List<Reservation> findByAccepteeIsNullAndAnnuleeFalseOrderByCreatedAtAsc();

    // Réservations acceptées dont le matériel n'a pas encore été remis
    List<Reservation> findByAccepteeTrueAndAnnuleeFalseAndEmpruntIsNullOrderByDateDebutAsc();

    // Réservations acceptées qui chevauchent la période demandée
    @Query("select count(r) from Reservation r where r.typeMateriel.id = :typeId and r.acceptee = true "
            + "and r.annulee = false and r.dateDebut < :fin and r.dateFin > :debut")
    long countConflits(@Param("typeId") UUID typeId, @Param("debut") LocalDateTime debut,
                       @Param("fin") LocalDateTime fin);
}
