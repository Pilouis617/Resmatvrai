package spring.sio.resmat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.sio.resmat.entities.TypeMateriel;

import java.util.List;
import java.util.UUID;

public interface TypeMaterielRepository extends JpaRepository<TypeMateriel, UUID> {
    List<TypeMateriel> findByAuCatalogueTrueOrderByNomAsc();

    List<TypeMateriel> findAllByOrderByNomAsc();

    boolean existsByNomIgnoreCase(String nom);

    boolean existsByNomIgnoreCaseAndIdNot(String nom, UUID id);
}
