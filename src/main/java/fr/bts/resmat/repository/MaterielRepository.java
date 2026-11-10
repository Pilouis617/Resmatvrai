package fr.bts.resmat.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.bts.resmat.entities.Materiel;

public interface MaterielRepository extends JpaRepository<Materiel,UUID> {
    List<Materiel> findByTypeIdAndDisponibleTrue(UUID id);
}
