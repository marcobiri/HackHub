package unicam.hackhub.model.staff;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Il Giudice valuta le sottomissioni dei team alla conclusione dell'hackathon.
 * Per ogni sottomissione rilascia un giudizio scritto e un punteggio 0-10.
 */
@Entity
@DiscriminatorValue("GIUDICE")
public class Giudice extends MembroStaff {

    public Giudice() {
    }

    public Giudice(String username, String email, String password) {
        super(username, email, password, RuoloStaff.GIUDICE);
    }
}
