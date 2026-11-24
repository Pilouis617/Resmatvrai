package spring.sio.resmat.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.sio.resmat.entities.Emprunt;
import spring.sio.resmat.entities.Materiel;
import spring.sio.resmat.entities.Reservation;
import spring.sio.resmat.entities.TypeMateriel;
import spring.sio.resmat.entities.Utilisateur;
import spring.sio.resmat.forms.ReservationForm;
import spring.sio.resmat.repositories.EmpruntRepository;
import spring.sio.resmat.repositories.MaterielRepository;
import spring.sio.resmat.repositories.ReservationRepository;
import spring.sio.resmat.repositories.TypeMaterielRepository;
import spring.sio.resmat.repositories.UtilisateurRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Règles métier du cycle de vie d'une réservation :
 * demande, annulation, décision, remise puis retour du matériel.
 */
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final TypeMaterielRepository typeMaterielRepository;
    private final MaterielRepository materielRepository;
    private final EmpruntRepository empruntRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              UtilisateurRepository utilisateurRepository,
                              TypeMaterielRepository typeMaterielRepository,
                              MaterielRepository materielRepository,
                              EmpruntRepository empruntRepository) {
        this.reservationRepository = reservationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.typeMaterielRepository = typeMaterielRepository;
        this.materielRepository = materielRepository;
        this.empruntRepository = empruntRepository;
    }

    public List<Reservation> mesReservations(String login) {
        return reservationRepository.findByEmprunteurLoginOrderByCreatedAtDesc(login);
    }

    public List<Reservation> demandesEnAttente() {
        return reservationRepository.findByAccepteeIsNullAndAnnuleeFalseOrderByCreatedAtAsc();
    }

    public List<Reservation> reservationsARemettre() {
        return reservationRepository.findByAccepteeTrueAndAnnuleeFalseAndEmpruntIsNullOrderByDateDebutAsc();
    }

    public List<Emprunt> empruntsEnCours() {
        List<Emprunt> emprunts = new ArrayList<>(empruntRepository.findByDateRetourIsNull());
        emprunts.sort((a, b) -> {
            LocalDateTime da = a.getDateRetourPrevue();
            LocalDateTime db = b.getDateRetourPrevue();
            if (da == null) return db == null ? 0 : 1;
            if (db == null) return -1;
            return da.compareTo(db);
        });
        return emprunts;
    }

    @Transactional
    public Reservation demander(String login, ReservationForm form) {
        if (!form.getDateFin().isAfter(form.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début.");
        }
        if (form.getDateDebut().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La date de début doit être dans le futur.");
        }
        TypeMateriel type = typeMaterielRepository.findById(form.getTypeMaterielId())
                .orElseThrow(() -> new IllegalArgumentException("Type de matériel introuvable."));
        if (!type.isAuCatalogue()) {
            throw new IllegalArgumentException("Ce matériel a été retiré du catalogue.");
        }
        long jours = Duration.between(form.getDateDebut(), form.getDateFin()).toDays();
        if (jours > type.getDureeMax()) {
            throw new IllegalArgumentException("La durée maximale d'emprunt pour ce matériel est de "
                    + type.getDureeMaxTexte() + ".");
        }
        if (exemplairesUtilisables(type).isEmpty()) {
            throw new IllegalArgumentException("Aucun exemplaire de ce matériel n'est disponible.");
        }

        Reservation reservation = new Reservation();
        reservation.setEmprunteur(utilisateur(login));
        reservation.setTypeMateriel(type);
        reservation.setDateDebut(form.getDateDebut());
        reservation.setDateFin(form.getDateFin());
        reservation.setMotif(form.getMotif() == null || form.getMotif().isBlank() ? null : form.getMotif().trim());
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void annuler(UUID id, String login) {
        Reservation reservation = reservation(id);
        if (!reservation.getEmprunteur().getLogin().equals(login)) {
            throw new IllegalArgumentException("Tu ne peux annuler que tes propres demandes.");
        }
        if (!reservation.isEnAttente()) {
            throw new IllegalArgumentException("Seule une demande en attente peut être annulée.");
        }
        reservation.setAnnulee(true);
    }

    @Transactional
    public void decider(UUID id, String loginGestionnaire, boolean accepter, String motifDecision) {
        Reservation reservation = reservation(id);
        if (!reservation.isEnAttente()) {
            throw new IllegalArgumentException("Cette demande a déjà été traitée.");
        }
        if (accepter) {
            int capacite = exemplairesUtilisables(reservation.getTypeMateriel()).size();
            long conflits = reservationRepository.countConflits(reservation.getTypeMateriel().getId(),
                    reservation.getDateDebut(), reservation.getDateFin());
            if (conflits >= capacite) {
                throw new IllegalArgumentException("Conflit : aucun exemplaire n'est libre sur cette période.");
            }
        } else if (motifDecision == null || motifDecision.isBlank()) {
            throw new IllegalArgumentException("Indique le motif du refus.");
        }
        reservation.setAcceptee(accepter);
        reservation.setMotifDecision(motifDecision == null || motifDecision.isBlank() ? null : motifDecision.trim());
        reservation.setGestionnaire(utilisateur(loginGestionnaire));
    }

    /**
     * Remise du matériel : un exemplaire libre est attribué automatiquement.
     */
    @Transactional
    public Emprunt remettre(UUID id, String etatAvant) {
        Reservation reservation = reservation(id);
        if (!reservation.isAcceptable() || reservation.getEmprunt() != null) {
            throw new IllegalArgumentException("Le matériel ne peut pas être remis pour cette réservation.");
        }
        Set<UUID> sortis = empruntRepository.findByDateRetourIsNull().stream()
                .map(e -> e.getMateriel().getId())
                .collect(Collectors.toSet());
        Materiel materiel = exemplairesUtilisables(reservation.getTypeMateriel()).stream()
                .filter(m -> !sortis.contains(m.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tous les exemplaires sont déjà sortis."));

        Emprunt emprunt = new Emprunt();
        emprunt.setMateriel(materiel);
        emprunt.setReservation(reservation);
        emprunt.setDateRemise(LocalDateTime.now());
        emprunt.setEtatAvant(etatAvant == null || etatAvant.isBlank() ? "État vérifié au départ." : etatAvant.trim());
        emprunt = empruntRepository.save(emprunt);
        reservation.setEmprunt(emprunt);
        return emprunt;
    }

    @Transactional
    public void rendre(UUID empruntId, String etatApres) {
        Emprunt emprunt = empruntRepository.findById(empruntId)
                .orElseThrow(() -> new IllegalArgumentException("Emprunt introuvable."));
        if (emprunt.getDateRetour() != null) {
            throw new IllegalArgumentException("Ce matériel a déjà été rendu.");
        }
        emprunt.setDateRetour(LocalDateTime.now());
        emprunt.setEtatApres(etatApres == null || etatApres.isBlank() ? "Rendu sans remarque." : etatApres.trim());
    }

    private List<Materiel> exemplairesUtilisables(TypeMateriel type) {
        return materielRepository.findByTypeMaterielIdAndDisponibleTrueAndDeletedFalse(type.getId());
    }

    private Reservation reservation(UUID id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable."));
    }

    private Utilisateur utilisateur(String login) {
        return utilisateurRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
    }
}
