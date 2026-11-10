package fr.bts.resmat.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.bts.resmat.entities.Categorie;

public interface CategorieRepository extends JpaRepository<Categorie,UUID> {
}
