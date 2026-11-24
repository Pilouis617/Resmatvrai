package spring.sio.resmat.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.sio.resmat.entities.Categorie;
import spring.sio.resmat.entities.TypeMateriel;
import spring.sio.resmat.forms.TypeMaterielForm;
import spring.sio.resmat.repositories.CategorieRepository;
import spring.sio.resmat.repositories.TypeMaterielRepository;

import java.util.List;
import java.util.UUID;

@Service
public class TypeMaterielService {

    private final TypeMaterielRepository typeMaterielRepository;
    private final CategorieRepository categorieRepository;

    public TypeMaterielService(TypeMaterielRepository typeMaterielRepository,
                               CategorieRepository categorieRepository) {
        this.typeMaterielRepository = typeMaterielRepository;
        this.categorieRepository = categorieRepository;
    }

    public List<TypeMateriel> findAll() {
        return typeMaterielRepository.findAllByOrderByNomAsc();
    }

    public List<TypeMateriel> findCatalogue() {
        return typeMaterielRepository.findByAuCatalogueTrueOrderByNomAsc();
    }

    public TypeMateriel findById(UUID id) {
        return typeMaterielRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Type de matériel introuvable."));
    }

    public List<Categorie> findCategories() {
        return categorieRepository.findAllByOrderByLibelleAsc();
    }

<<<<<<< HEAD
    private Categorie trouverOuCreerCategorie(String libelleSaisi) {
        if (libelleSaisi == null || libelleSaisi.isBlank()) {
            throw new IllegalArgumentException("Saisis une catégorie.");
        }

        String libelle = libelleSaisi.trim().replaceAll("\\s+", " ");
        if (libelle.length() > 150) {
            throw new IllegalArgumentException("La catégorie ne doit pas dépasser 150 caractères.");
        }

        return categorieRepository.findByLibelleIgnoreCase(libelle)
                .orElseGet(() -> {
                    Categorie nouvelle = new Categorie();
                    nouvelle.setLibelle(libelle);
                    return categorieRepository.save(nouvelle);
                });
    }

    /**
     * Crée un type (id null) ou modifie un type existant.
     * La catégorie saisie est réutilisée si elle existe, sinon elle est créée.
=======
    /**
     * Crée un type (id null) ou modifie un type existant.
>>>>>>> 6f9cf89f8d123ffa2d061193b02d8b6c5644442e
     */
    @Transactional
    public TypeMateriel enregistrer(UUID id, TypeMaterielForm form) {
        String nom = form.getNom().trim();
        boolean doublon = id == null
                ? typeMaterielRepository.existsByNomIgnoreCase(nom)
                : typeMaterielRepository.existsByNomIgnoreCaseAndIdNot(nom, id);
        if (doublon) {
            throw new IllegalArgumentException("Un type de matériel porte déjà ce nom.");
        }
<<<<<<< HEAD

        Categorie categorie = trouverOuCreerCategorie(form.getCategorieLibelle());
=======
        Categorie categorie = categorieRepository.findById(form.getCategorieId())
                .orElseThrow(() -> new IllegalArgumentException("Catégorie introuvable."));
>>>>>>> 6f9cf89f8d123ffa2d061193b02d8b6c5644442e

        TypeMateriel type = id == null ? new TypeMateriel() : findById(id);
        type.setNom(nom);
        type.setDescription(form.getDescription() == null ? null : form.getDescription().trim());
        type.setDureeMax(form.getDureeMax());
        type.setAuCatalogue(form.isAuCatalogue());
        type.setCategorie(categorie);
        return typeMaterielRepository.save(type);
    }

    @Transactional
    public void changerPresenceCatalogue(UUID id, boolean auCatalogue) {
        findById(id).setAuCatalogue(auCatalogue);
    }
}
