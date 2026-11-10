package fr.bts.resmat.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.bts.resmat.entities.Emprunt;

public interface EmpruntRepository extends JpaRepository<Emprunt,UUID> {
    Optional<Emprunt> findByReservationId(UUID id);
    List<Emprunt> findByReservationEmprunteurLoginOrderByDateRemiseDesc(String login);
    List<Emprunt> findByDateRetourEffectiveIsNullOrderByDateRetourPrevueAsc();
}
