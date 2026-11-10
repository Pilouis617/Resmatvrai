package fr.bts.resmat.service;

import java.time.*;
import java.util.*;
import fr.bts.resmat.dto.*;
import fr.bts.resmat.entities.*;
import fr.bts.resmat.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {
    private final ReservationRepository reservations;
    private final UtilisateurRepository users;
    private final TypeMaterielRepository types;
    private final MaterielRepository materiels;
    private final EmpruntRepository emprunts;
    public ReservationService(ReservationRepository reservations,UtilisateurRepository users,TypeMaterielRepository types,MaterielRepository materiels,EmpruntRepository emprunts) {
        this.reservations=reservations;
        this.users=users;
        this.types=types;
        this.materiels=materiels;
        this.emprunts=emprunts;
    }

    @Transactional public Reservation demander(String login,ReservationForm f) {
        if (f.dateDebut==null||f.dateFin==null||!f.dateFin.isAfter(f.dateDebut))throw new IllegalArgumentException("Dates invalides");
        if (f.dateDebut.isBefore(LocalDateTime.now()))throw new IllegalArgumentException("La date de début doit être future");
        TypeMateriel t=types.findById(f.typeMaterielId).orElseThrow();
        if (!t.auCatalogue)throw new IllegalArgumentException("Matériel retiré du catalogue");
        if (t.dureeMaxEmprunt!=null&&Duration.between(f.dateDebut,f.dateFin).toDays()>t.dureeMaxEmprunt)throw new IllegalArgumentException("Durée maximale dépassée");
        if (materiels.findByTypeIdAndDisponibleTrue(t.id).isEmpty())throw new IllegalArgumentException("Matériel indisponible");
        Reservation r=new Reservation();
        r.emprunteur=users.findByLogin(login).orElseThrow();
        r.typeMateriel=t;
        r.dateDebut=f.dateDebut;
        r.dateFin=f.dateFin;
        r.commentaire=f.commentaire;
        return reservations.save(r);
    }

    @Transactional public void annuler(UUID id,String login) {
        Reservation r=reservations.findById(id).orElseThrow();
        if (!r.emprunteur.login.equals(login)||r.avis!=null||r.annulee)throw new IllegalArgumentException("Annulation interdite");
        r.annulee=true;
    }

    @Transactional public void decider(UUID id,String gestionnaire,boolean accepter,String motif) {
        Reservation r=reservations.findById(id).orElseThrow();
        if (r.avis!=null||r.annulee)throw new IllegalArgumentException("Demande déjà traitée");
        if (accepter) {
            long capacite=materiels.findByTypeIdAndDisponibleTrue(r.typeMateriel.id).size();
            long conflits=reservations.findConflits(r.typeMateriel.id,r.dateDebut,r.dateFin).size();
            if (conflits>=capacite)throw new IllegalArgumentException("Conflit : aucun exemplaire libre pour cette période");
        }
        r.avis=accepter;
        r.motivationAvis=motif;
        r.gestionnaire=users.findByLogin(gestionnaire).orElseThrow();
    }

    @Transactional public void remettre(UUID id) {
        Reservation r=reservations.findById(id).orElseThrow();
        if (!Boolean.TRUE.equals(r.avis)||r.annulee||emprunts.findByReservationId(id).isPresent())throw new IllegalArgumentException("Remise impossible");
        Set<UUID> occupes=new HashSet<>();
        for (Emprunt e:emprunts.findByDateRetourEffectiveIsNullOrderByDateRetourPrevueAsc())occupes.add(e.materiel.id);
        Materiel dispo=materiels.findByTypeIdAndDisponibleTrue(r.typeMateriel.id).stream().filter(m->!occupes.contains(m.id)).findFirst().orElseThrow(()->new IllegalArgumentException("Aucun exemplaire disponible"));
        Emprunt e=new Emprunt();
        e.reservation=r;
        e.materiel=dispo;
        e.dateRemise=LocalDateTime.now();
        e.dateRetourPrevue=r.dateFin;
        emprunts.save(e);
    }

    @Transactional public void rendre(UUID id,boolean degradations,String remarque) {
        Emprunt e=emprunts.findById(id).orElseThrow();
        if (e.dateRetourEffective!=null)throw new IllegalArgumentException("Déjà rendu");
        e.dateRetourEffective=LocalDateTime.now();
        e.degradations=degradations;
        e.commentaire=remarque;
    }
}
