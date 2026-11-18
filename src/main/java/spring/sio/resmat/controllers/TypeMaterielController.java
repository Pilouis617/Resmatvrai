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
import spring.sio.resmat.forms.TypeMaterielForm;
import spring.sio.resmat.services.TypeMaterielService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/types-materiel")
public class TypeMaterielController {

    private final TypeMaterielService typeMaterielService;

    public TypeMaterielController(TypeMaterielService typeMaterielService) {
        this.typeMaterielService = typeMaterielService;
    }

    @GetMapping
    public String index(ModelMap model) {
        var types = typeMaterielService.findAll();
        model.put("types", types);
        model.put("aucunType", types.isEmpty());
        model.put("navTypes", true);
        return "/types-materiel/index";
    }

    @GetMapping("/nouveau")
    public String nouveau(ModelMap model) {
        return formulaire(null, new TypeMaterielForm(), null, model);
    }

    @GetMapping("/{id}/modifier")
    public String modifier(@PathVariable UUID id, ModelMap model) {
        return formulaire(id, TypeMaterielForm.depuis(typeMaterielService.findById(id)), null, model);
    }

    @PostMapping
    public String enregistrer(@RequestParam(required = false) UUID id,
                              @Valid @ModelAttribute TypeMaterielForm form, BindingResult result,
                              ModelMap model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return formulaire(id, form, result.getAllErrors().get(0).getDefaultMessage(), model);
        }
        try {
            typeMaterielService.enregistrer(id, form);
        } catch (IllegalArgumentException e) {
            return formulaire(id, form, e.getMessage(), model);
        }
        redirect.addFlashAttribute("message", id == null ? "Type de matériel ajouté." : "Type de matériel modifié.");
        return "redirect:/types-materiel";
    }

    @PostMapping("/{id}/catalogue")
    public String catalogue(@PathVariable UUID id, @RequestParam boolean auCatalogue,
                            RedirectAttributes redirect) {
        typeMaterielService.changerPresenceCatalogue(id, auCatalogue);
        redirect.addFlashAttribute("message", auCatalogue ? "Remis au catalogue." : "Retiré du catalogue.");
        return "redirect:/types-materiel";
    }

    private String formulaire(UUID id, TypeMaterielForm form, String erreur, ModelMap model) {
        List<Map<String, Object>> categories = typeMaterielService.findCategories().stream()
                .map(c -> Map.<String, Object>of(
                        "id", c.getId(),
                        "libelle", c.getLibelle(),
                        "selected", c.getId().equals(form.getCategorieId())))
                .toList();
        if (id != null) {
            model.put("id", id);
        }
        model.put("modification", id != null);
        model.put("form", form);
        model.put("categories", categories);
        if (erreur != null) {
            model.put("erreur", erreur);
        }
        model.put("navTypes", true);
        return "/types-materiel/form";
    }
}
