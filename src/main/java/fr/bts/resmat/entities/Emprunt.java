package fr.bts.resmat.entities;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "Emprunt")
public class Emprunt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) public UUID id;
    @OneToOne(optional = false)
    @JoinColumn(name = "reservation_id",unique = true) public Reservation reservation;
    @ManyToOne(optional = false)
    @JoinColumn(name = "materiel_id") public Materiel materiel;
    public LocalDateTime dateRemise;
    public LocalDateTime dateRetourPrevue;
    public LocalDateTime dateRetourEffective;
    public boolean degradations=false;
    @Column(length = 2000) public String commentaire;
    public boolean getEnRetard() {
        return dateRetourPrevue!=null&&dateRetourEffective==null&&LocalDateTime.now().isAfter(dateRetourPrevue);
    }
}
