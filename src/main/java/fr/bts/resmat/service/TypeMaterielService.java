package fr.bts.resmat.service;

import java.util.*;
import fr.bts.resmat.dto.*;
import fr.bts.resmat.entities.*;
import fr.bts.resmat.repository.*;
import org.springframework.stereotype.Service;

@Service
public class TypeMaterielService {
    private final TypeMaterielRepository types;
    private final CategorieRepository categories;
    public TypeMaterielService(TypeMaterielRepository types,CategorieRepository categories) {
        this.types=types;
        this.categories=categories;
    }
    public TypeMateriel enregistrer(UUID id, TypeMaterielForm f) {
        TypeMateriel t=id==null?new TypeMateriel():types.findById(id).orElseThrow();
        t.nom=f.nom.trim();
        t.description=f.description.trim();
        t.dureeMaxEmprunt=f.dureeMaxEmprunt;
        t.auCatalogue=f.auCatalogue;
        t.categorie=categories.findById(f.categorieId).orElseThrow();
        return types.save(t);
    }
    public void desactiver(UUID id) {
        TypeMateriel t=types.findById(id).orElseThrow();
        t.auCatalogue=false;
        types.save(t);
    }
}
