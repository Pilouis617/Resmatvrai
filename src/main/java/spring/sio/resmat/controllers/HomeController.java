package spring.sio.resmat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String accueil() {
        return "redirect:/catalogue";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        ModelMap model) {
        model.put("erreurConnexion", error != null);
        model.put("deconnecte", logout != null);
        return "/login";
    }
}
