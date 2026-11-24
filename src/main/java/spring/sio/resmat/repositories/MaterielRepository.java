package spring.sio.resmat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.sio.resmat.entities.Materiel;

import java.util.List;
import java.util.UUID;

public interface MaterielRepository extends JpaRepository<Materiel, UUID> {
    // Exemplaires utilisables : en service et non supprimés
    List<Materiel> findByTypeMaterielIdAndDisponibleTrueAndDeletedFalse(UUID typeMaterielId);

    // Le nom d'un exemplaire est unique dans toute la table
    boolean existsByNomIgnoreCase(String nom);
}
