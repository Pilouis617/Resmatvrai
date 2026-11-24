package spring.sio.resmat.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.sio.resmat.entities.Categorie;
import spring.sio.resmat.entities.Materiel;
import spring.sio.resmat.entities.TypeMateriel;
import spring.sio.resmat.forms.TypeMaterielForm;
import spring.sio.resmat.repositories.CategorieRepository;
import spring.sio.resmat.repositories.EmpruntRepository;
import spring.sio.resmat.repositories.MaterielRepository;
import spring.sio.resmat.repositories.TypeMaterielRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TypeMaterielService {

    private final TypeMaterielRepository typeMaterielRepository;
    private final CategorieRepository categorieRepository;
    private final MaterielRepository materielRepository;
    private final EmpruntRepository empruntRepository;

    public TypeMaterielService(TypeMaterielRepository typeMaterielRepository,
                               CategorieRepository categorieRepository,
                               MaterielRepository materielRepository,
                               EmpruntRepository empruntRepository) {
        this.typeMaterielRepository = typeMaterielRepository;
        this.categorieRepository = categorieRepository;
        this.materielRepository = materielRepository;
        this.empruntRepository = empruntRepository;
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

        Categorie categorie = trouverOuCreerCategorie(form.getCategorieLibelle());

        TypeMateriel type = id == null ? new TypeMateriel() : findById(id);
        type.setNom(nom);
        type.setDescription(form.getDescription() == null ? null : form.getDescription().trim());
        type.setDureeMax(form.getDureeMax());
        type.setAuCatalogue(form.isAuCatalogue());
        type.setCategorie(categorie);
        type = typeMaterielRepository.save(type);
        ajusterExemplaires(type, form.getNbExemplaires() == null ? 0 : form.getNbExemplaires());
        return type;
    }

    /**
     * Amène le nombre d'exemplaires utilisables au nombre demandé :
     * crée les exemplaires manquants, ou retire (suppression logique) des exemplaires
     * qui ne sont pas actuellement sortis.
     */
    private void ajusterExemplaires(TypeMateriel type, int voulu) {
        List<Materiel> actuels = materielRepository
                .findByTypeMaterielIdAndDisponibleTrueAndDeletedFalse(type.getId());
        int difference = voulu - actuels.size();

        if (difference > 0) {
            String base = type.getNom().length() > 60 ? type.getNom().substring(0, 60) : type.getNom();
            int numero = actuels.size();
            for (int i = 0; i < difference; i++) {
                String nomExemplaire;
                do {
                    numero++;
                    nomExemplaire = base + " n°" + numero;
                } while (materielRepository.existsByNomIgnoreCase(nomExemplaire));

                Materiel materiel = new Materiel();
                materiel.setNom(nomExemplaire);
                materiel.setTypeMateriel(type);
                materiel.setDisponible(true);
                materielRepository.save(materiel);
            }
        } else if (difference < 0) {
            Set<UUID> sortis = empruntRepository.findByDateRetourIsNull().stream()
                    .map(e -> e.getMateriel().getId())
                    .collect(Collectors.toSet());
            List<Materiel> libres = actuels.stream().filter(m -> !sortis.contains(m.getId())).toList();
            int aRetirer = -difference;
            if (libres.size() < aRetirer) {
                throw new IllegalArgumentException("Impossible de descendre à " + voulu
                        + " exemplaire(s) : certains sont actuellement empruntés.");
            }
            for (int i = 0; i < aRetirer; i++) {
                libres.get(i).setDeleted(true);
            }
        }
    }

    @Transactional
    public void changerPresenceCatalogue(UUID id, boolean auCatalogue) {
        findById(id).setAuCatalogue(auCatalogue);
    }
}
