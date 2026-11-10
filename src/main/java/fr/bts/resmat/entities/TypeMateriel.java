package fr.bts.resmat.entities;

import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "TypeMateriel")
public class TypeMateriel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) public UUID id;
    @Column(nullable = false) public String nom;
    @Column(length = 2000) public String description;
    public Integer dureeMaxEmprunt=14;
    public boolean auCatalogue=true;
    @ManyToOne(optional = false)
    @JoinColumn(name = "categorie_id") public Categorie categorie;
}
