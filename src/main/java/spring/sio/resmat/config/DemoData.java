package spring.sio.resmat.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.sio.resmat.entities.Utilisateur;
import spring.sio.resmat.repositories.UtilisateurRepository;

/**
 * Crée deux comptes de démonstration s'ils n'existent pas encore.
 * Ils n'entrent pas en conflit avec le jeu de données data/seed-resmat.sql.
 */
@Configuration
public class DemoData {

    @Bean
    public CommandLineRunner comptesDemo(UtilisateurRepository utilisateurRepository, PasswordEncoder encoder) {
        return args -> {
            creer(utilisateurRepository, encoder, "etudiant", "Étudiant", false);
            creer(utilisateurRepository, encoder, "gestionnaire", "Gestionnaire", true);
        };
    }

    private void creer(UtilisateurRepository repository, PasswordEncoder encoder,
                       String login, String prenom, boolean gestionnaire) {
        if (repository.findByLogin(login).isPresent()) {
            return;
        }
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setLogin(login);
        utilisateur.setPrenom(prenom);
        utilisateur.setNom("Démo");
        utilisateur.setEmail(login + "@resmat.local");
        utilisateur.setMotDePasse(encoder.encode("Demo123!"));
        utilisateur.setGestionnaire(gestionnaire);
        repository.save(utilisateur);
    }
}
