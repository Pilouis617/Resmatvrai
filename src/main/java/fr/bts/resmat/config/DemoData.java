package fr.bts.resmat.config;

import fr.bts.resmat.entities.*;
import fr.bts.resmat.repository.*;
import org.springframework.boot.*;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DemoData {

    @Bean CommandLineRunner init(CategorieRepository categories,TypeMaterielRepository types,MaterielRepository materiels,UtilisateurRepository users,PasswordEncoder encoder) {
        return args-> {
            if (categories.count()==0) {
                Categorie c=categories.save(new Categorie("Informatique"));
                Categorie a=categories.save(new Categorie("Audiovisuel"));
                TypeMateriel pc=new TypeMateriel();
                pc.nom="PC portable";
                pc.description="Portable pour les cours";
                pc.categorie=c;
                types.save(pc);
                TypeMateriel camera=new TypeMateriel();
                camera.nom="Caméra";
                camera.description="Caméra vidéo HD";
                camera.categorie=a;
                types.save(camera);
                for (TypeMateriel t:types.findAll()) {
                    Materiel m=new Materiel();
                    m.nom=t.nom+" n°1";
                    m.type=t;
                    materiels.save(m);
                }
            }
            if (users.count()==0) {
                Utilisateur e=new Utilisateur();
                e.nom="Demo";
                e.prenom="Etudiant";
                e.email="etudiant@resmat.local";
                e.login="etudiant";
                e.motDePasse=encoder.encode("Demo123!");
                users.save(e);
                Utilisateur g=new Utilisateur();
                g.nom="Demo";
                g.prenom="Gestionnaire";
                g.email="gestionnaire@resmat.local";
                g.login="gestionnaire";
                g.motDePasse=encoder.encode("Demo123!");
                g.gestionnaire=true;
                users.save(g);
            }
        }
        ;
    }
}
