package spring.sio.resmat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.sio.resmat.services.TypeMaterielService;

@Controller
@RequestMapping("/catalogue")
public class CatalogueController {

    private final TypeMaterielService typeMaterielService;

    public CatalogueController(TypeMaterielService typeMaterielService) {
        this.typeMaterielService = typeMaterielService;
    }

    @GetMapping
    public String index(ModelMap model) {
        model.put("types", typeMaterielService.findCatalogue());
        model.put("navCatalogue", true);
        return "/catalogue/index";
    }
}
