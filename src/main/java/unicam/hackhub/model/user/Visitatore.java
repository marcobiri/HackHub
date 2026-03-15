package unicam.hackhub.model.user;

/**
 * Rappresenta un visitatore non autenticato.
 * Non è un'entità JPA perché non viene persistito.
 * Può consultare solo le informazioni pubbliche degli hackathon.
 */
public class Visitatore {

    private static final String MESSAGGIO_NON_AUTENTICATO = "Operazione non permessa: effettuare registrazione e accesso.";

    /**
     * Il visitatore può consultare la lista hackathon.
     *
     * @return true
     */
    public boolean puoConsultareHackathon() {
        return true;
    }

    /**
     * Il visitatore non può accedere a funzionalità protette.
     *
     * @throws UnsupportedOperationException sempre
     */
    public void operazioneProtetta() {
        throw new UnsupportedOperationException(MESSAGGIO_NON_AUTENTICATO);
    }
}
