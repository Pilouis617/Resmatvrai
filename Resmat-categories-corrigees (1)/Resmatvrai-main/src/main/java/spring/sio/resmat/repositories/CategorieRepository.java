package spring.sio.resmat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.sio.resmat.entities.Categorie;

import java.util.List;
<<<<<<< HEAD
import java.util.Optional;
=======
>>>>>>> 6f9cf89f8d123ffa2d061193b02d8b6c5644442e
import java.util.UUID;

public interface CategorieRepository extends JpaRepository<Categorie, UUID> {
    List<Categorie> findAllByOrderByLibelleAsc();
<<<<<<< HEAD
    Optional<Categorie> findByLibelleIgnoreCase(String libelle);
=======
>>>>>>> 6f9cf89f8d123ffa2d061193b02d8b6c5644442e
}
