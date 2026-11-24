package spring.sio.resmat.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.sio.resmat.entities.TypeMateriel;
import spring.sio.resmat.forms.ReservationForm;
import spring.sio.resmat.services.ReservationService;
import spring.sio.resmat.services.TypeMaterielService;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final TypeMaterielService typeMaterielService;

    public ReservationController(ReservationService reservationService,
                                 TypeMaterielService typeMaterielService) {
        this.reservationService = reservationService;
        this.typeMaterielService = typeMaterielService;
    }

    @GetMapping
    public String index(ModelMap model, Principal principal) {
        var reservations = reservationService.mesReservations(principal.getName());
        model.put("reservations", reservations);
        model.put("aDesReservations", !reservations.isEmpty());
        model.put("navReservations", true);
        return "/reservations/index";
    }

    @GetMapping("/nouvelle")
    public String nouvelle(@RequestParam UUID type, ModelMap model) {
        return formulaire(typeMaterielService.findById(type), new ReservationForm(), null, model);
    }

    @PostMapping
    public String enregistrer(@Valid @ModelAttribute ReservationForm form, BindingResult result,
                              Principal principal, ModelMap model, RedirectAttributes redirect) {
        if (form.getTypeMaterielId() == null) {
            return "redirect:/catalogue";
        }
        TypeMateriel type = typeMaterielService.findById(form.getTypeMaterielId());
        if (result.hasErrors()) {
            return formulaire(type, form, "Vérifie les dates saisies.", model);
        }
        try {
            reservationService.demander(principal.getName(), form);
        } catch (IllegalArgumentException e) {
            return formulaire(type, form, e.getMessage(), model);
        }
        redirect.addFlashAttribute("message", "Ta demande de réservation a bien été envoyée.");
        return "redirect:/reservations";
    }

    @PostMapping("/{id}/annuler")
    public String annuler(@PathVariable UUID id, Principal principal, RedirectAttributes redirect) {
        try {
            reservationService.annuler(id, principal.getName());
            redirect.addFlashAttribute("message", "La demande a été annulée.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/reservations";
    }

    private String formulaire(TypeMateriel type, ReservationForm form, String erreur, ModelMap model) {
        model.put("type", type);
        model.put("form", form);
        model.put("dateDebut", form.getDateDebut() == null ? "" : form.getDateDebut().toString());
        model.put("dateFin", form.getDateFin() == null ? "" : form.getDateFin().toString());
        if (erreur != null) {
            model.put("erreur", erreur);
        }
        model.put("navCatalogue", true);
        return "/reservations/form";
    }
}
