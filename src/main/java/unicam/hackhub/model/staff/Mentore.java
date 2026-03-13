package unicam.hackhub.model.staff;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Il Mentore affianca i team durante l'hackathon.
 * Gestisce richieste di supporto, propone call e segnala violazioni del
 * regolamento.
 */
@Entity
@DiscriminatorValue("MENTORE")
public class Mentore extends MembroStaff {

    public Mentore() {
    }

    public Mentore(String username, String email, String password) {
        super(username, email, password, RuoloStaff.MENTORE);
    }
}
