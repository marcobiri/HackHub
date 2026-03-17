package unicam.hackhub.model.staff;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * L'Organizzatore crea hackathon, definisce le informazioni essenziali,
 * assegna giudice e mentori, e proclama il vincitore.
 */
@Entity
@DiscriminatorValue("ORGANIZZATORE")
public class Organizzatore extends MembroStaff {

    public Organizzatore() {
    }

    public Organizzatore(String username, String email, String password) {
        super(username, email, password, RuoloStaff.ORGANIZZATORE);
    }
}
