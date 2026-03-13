package unicam.hackhub.model.staff;

import jakarta.persistence.*;
import unicam.hackhub.model.user.Utente;

/**
 * Classe base per i membri dello staff di un hackathon.
 * Estende Utente e aggiunge il ruolo specifico.
 */
@Entity
@DiscriminatorValue("STAFF")
public abstract class MembroStaff extends Utente {

    @Enumerated(EnumType.STRING)
    @Column(name = "ruolo_staff")
    private RuoloStaff ruolo;

    public MembroStaff() {
    }

    public MembroStaff(String username, String email, String password, RuoloStaff ruolo) {
        super(username, email, password);
        this.ruolo = ruolo;
    }

    public RuoloStaff getRuolo() {
        return ruolo;
    }

    public void setRuolo(RuoloStaff ruolo) {
        this.ruolo = ruolo;
    }
}
