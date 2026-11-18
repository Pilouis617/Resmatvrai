package spring.sio.resmat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.sio.resmat.services.ReservationService;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/gestion")
public class GestionController {

    private final ReservationService reservationService;

    public GestionController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String index(ModelMap model) {
        var demandes = reservationService.demandesEnAttente();
        var aRemettre = reservationService.reservationsARemettre();
        var emprunts = reservationService.empruntsEnCours();
        model.put("demandes", demandes);
        model.put("aDesDemandes", !demandes.isEmpty());
        model.put("nbDemandes", demandes.size());
        model.put("aRemettre", aRemettre);
        model.put("aDesRemises", !aRemettre.isEmpty());
        model.put("nbRemises", aRemettre.size());
        model.put("emprunts", emprunts);
        model.put("aDesEmprunts", !emprunts.isEmpty());
        model.put("nbEmprunts", emprunts.size());
        model.put("navGestion", true);
        return "/gestion/index";
    }

    @PostMapping("/reservations/{id}/decision")
    public String decision(@PathVariable UUID id, @RequestParam boolean accepter,
                           @RequestParam(defaultValue = "") String motif,
                           Principal principal, RedirectAttributes redirect) {
        try {
            reservationService.decider(id, principal.getName(), accepter, motif);
            redirect.addFlashAttribute("message", accepter ? "Demande acceptée." : "Demande refusée.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/gestion";
    }

    @PostMapping("/reservations/{id}/remise")
    public String remise(@PathVariable UUID id, @RequestParam(defaultValue = "") String etatAvant,
                         RedirectAttributes redirect) {
        try {
            var emprunt = reservationService.remettre(id, etatAvant);
            redirect.addFlashAttribute("message", "Matériel remis : " + emprunt.getMateriel().getNom() + ".");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/gestion";
    }

    @PostMapping("/emprunts/{id}/retour")
    public String retour(@PathVariable UUID id, @RequestParam(defaultValue = "") String etatApres,
                         RedirectAttributes redirect) {
        try {
            reservationService.rendre(id, etatApres);
            redirect.addFlashAttribute("message", "Retour enregistré.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/gestion";
    }
}
