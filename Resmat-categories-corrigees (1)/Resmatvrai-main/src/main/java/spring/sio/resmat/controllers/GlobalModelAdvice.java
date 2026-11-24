package spring.sio.resmat.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import spring.sio.resmat.repositories.UtilisateurRepository;

/**
 * Données communes à toutes les vues : jeton CSRF et utilisateur connecté.
 */
@ControllerAdvice
public class GlobalModelAdvice {

    private final UtilisateurRepository utilisateurRepository;

    public GlobalModelAdvice(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @ModelAttribute
    public void ajouter(Model model, HttpServletRequest request) {
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrf != null) {
            model.addAttribute("csrfParameterName", csrf.getParameterName());
            model.addAttribute("csrfToken", csrf.getToken());
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            utilisateurRepository.findByLogin(auth.getName()).ifPresent(u -> {
                model.addAttribute("utilisateur", u);
                model.addAttribute("estGestionnaire", u.isGestionnaire());
            });
        }
    }
}
