package fr.bts.resmat.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Rend le jeton CSRF disponible dans tous les templates Mustache. */
@ControllerAdvice public class CsrfModelAdvice {
    @ModelAttribute public void addCsrf(Model model, HttpServletRequest request) {
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrf == null) {
            csrf = (CsrfToken) request.getAttribute("_csrf");
        }
        if (csrf != null) {
            model.addAttribute("csrfParameterName", csrf.getParameterName());
            model.addAttribute("csrfToken", csrf.getToken());
        }
    }
}
