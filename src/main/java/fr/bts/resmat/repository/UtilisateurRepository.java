package fr.bts.resmat.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.bts.resmat.entities.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur,UUID> {
    Optional<Utilisateur> findByLogin(String login);
}
