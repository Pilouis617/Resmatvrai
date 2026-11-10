package fr.bts.resmat.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.bts.resmat.entities.TypeMateriel;

public interface TypeMaterielRepository extends JpaRepository<TypeMateriel,UUID> {
    List<TypeMateriel> findByAuCatalogueTrueOrderByNomAsc();
}
