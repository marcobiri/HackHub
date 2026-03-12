package unicam.hackhub.model.hackathon;

/**
 * Interfaccia State Pattern per il ciclo di vita dell'Hackathon.
 * Definisce le operazioni permesse in ogni stato.
 */
public interface StatoHackathon {

    /** @return true se in questo stato è possibile iscrivere team */
    boolean puoIscrivere();

    /** @return true se in questo stato è possibile inviare sottomissioni */
    boolean puoSottomettere();

    /** @return true se in questo stato è possibile valutare le sottomissioni */
    boolean puoValutare();

    /** @return true se in questo stato è possibile proclamare il vincitore */
    boolean puoProclamare();

    /**
     * @return true se in questo stato è possibile richiedere supporto al mentore
     */
    boolean puoRichiedereSupporto();

    /**
     * Avanza allo stato successivo nel ciclo di vita.
     * 
     * @return il prossimo stato
     * @throws IllegalStateException se non è possibile avanzare
     */
    StatoHackathon avanzaStato();

    /** @return il nome dello stato corrente */
    String getNome();
}
