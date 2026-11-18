package spring.sio.resmat.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Categorie extends BaseEntity {
    @Column(length = 150,nullable = false,unique = true)
    private String libelle;

    @OneToMany(mappedBy = "categorie")
    private List<TypeMateriel> typeMateriels=new ArrayList<>();

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<TypeMateriel> getTypeMateriels() {
        return typeMateriels;
    }

    public void setTypeMateriels(List<TypeMateriel> typeMateriels) {
        this.typeMateriels = typeMateriels;
    }
}
