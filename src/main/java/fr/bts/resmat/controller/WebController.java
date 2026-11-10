package fr.bts.resmat.controller;

import java.security.Principal;
import java.util.*;
import jakarta.validation.Valid;
import fr.bts.resmat.dto.*;
import fr.bts.resmat.repository.*;
import fr.bts.resmat.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {
    private final TypeMaterielRepository types;
    private final CategorieRepository categories;
    private final ReservationRepository reservations;
    private final EmpruntRepository emprunts;
    private final TypeMaterielService typeService;
    private final ReservationService reservationService;
    public WebController(TypeMaterielRepository types,CategorieRepository categories,ReservationRepository reservations,EmpruntRepository emprunts,TypeMaterielService typeService,ReservationService reservationService) {
        this.types=types;
        this.categories=categories;
        this.reservations=reservations;
        this.emprunts=emprunts;
        this.typeService=typeService;
        this.reservationService = reservationService;
    }

    @GetMapping("/") String home() {
        return "redirect:/catalogue";
    }

    @GetMapping("/login") String login(@RequestParam(required = false) String error,Model m) {
        m.addAttribute("error",error!=null);
        return "login";
    }

    @GetMapping("/catalogue") String catalogue(Model m) {
        m.addAttribute("types",types.findByAuCatalogueTrueOrderByNomAsc());
        return "catalogue";
    }

    @GetMapping("/mes-reservations") String mesReservations(Model m,Principal p) {
        m.addAttribute("reservations",reservations.findByEmprunteurLoginOrderByDateResaDesc(p.getName()));
        m.addAttribute("emprunts",emprunts.findByReservationEmprunteurLoginOrderByDateRemiseDesc(p.getName()));
        return "mes-reservations";
    }

    @PostMapping("/reserver") String reserver(@Valid @ModelAttribute ReservationForm form,BindingResult result,Principal p,Model m) {
        if (result.hasErrors()) {
            m.addAttribute("erreur","Vérifie les champs et les dates.");
            m.addAttribute("types",types.findByAuCatalogueTrueOrderByNomAsc());
            return "catalogue";
        }
        try {
            reservationService.demander(p.getName(),form);
            return "redirect:/mes-reservations?ok";
        }
        catch (IllegalArgumentException ex) {
            m.addAttribute("erreur",ex.getMessage());
            m.addAttribute("types",types.findByAuCatalogueTrueOrderByNomAsc());
            return "catalogue";
        }
    }

    @PostMapping("/reservation/{id}/annuler") String annuler(@PathVariable UUID id,Principal p) {
        reservationService.annuler(id,p.getName());
        return "redirect:/mes-reservations";
    }

    @GetMapping("/gestion") String gestion(Model m) {
        m.addAttribute("attentes",reservations.findByAvisIsNullAndAnnuleeFalseOrderByDateResaAsc());
        m.addAttribute("emprunts",emprunts.findByDateRetourEffectiveIsNullOrderByDateRetourPrevueAsc());
        m.addAttribute("acceptees",reservations.findByAvisTrueAndAnnuleeFalseOrderByDateDebutAsc());
        return "gestion";
    }

    @PostMapping("/gestion/reservation/{id}/decision") String decision(@PathVariable UUID id,@RequestParam boolean accepter,@RequestParam(defaultValue = "") String motif,Principal p,Model m) {
        try {
            reservationService.decider(id,p.getName(),accepter,motif);
            return "redirect:/gestion";
        }
        catch (IllegalArgumentException ex) {
            m.addAttribute("erreur",ex.getMessage());
            return gestion(m);
        }
    }

    @PostMapping("/gestion/reservation/{id}/remettre") String remettre(@PathVariable UUID id,Model m) {
        try {
            reservationService.remettre(id);
            return "redirect:/gestion";
        }
        catch (IllegalArgumentException ex) {
            m.addAttribute("erreur",ex.getMessage());
            return gestion(m);
        }
    }

    @PostMapping("/gestion/emprunt/{id}/rendre") String rendre(@PathVariable UUID id,@RequestParam(defaultValue = "false") boolean degradations,@RequestParam(defaultValue = "") String remarque) {
        reservationService.rendre(id,degradations,remarque);
        return "redirect:/gestion";
    }

    @GetMapping("/gestion/types") String types(Model m) {
        m.addAttribute("types",types.findAll());
        m.addAttribute("categories",categories.findAll());
        return "types";
    }

    @PostMapping("/gestion/types") String enregistrerType(@Valid @ModelAttribute TypeMaterielForm form,BindingResult errors,@RequestParam(required = false) UUID id,Model m) {
        if (errors.hasErrors()) {
            m.addAttribute("erreur","Formulaire invalide");
            return types(m);
        }
        typeService.enregistrer(id,form);
        return "redirect:/gestion/types";
    }

    @PostMapping("/gestion/types/{id}/desactiver") String desactiver(@PathVariable UUID id) {
        typeService.desactiver(id);
        return "redirect:/gestion/types";
    }
}
