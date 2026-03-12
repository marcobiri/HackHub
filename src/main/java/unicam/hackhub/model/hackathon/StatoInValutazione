package unicam.hackhub.model.hackathon;

/**
 * Hackathon in fase di valutazione: il giudice valuta le sottomissioni.
 */
public class StatoInValutazione implements StatoHackathon {

    @Override
    public boolean puoIscrivere() {
        return false;
    }

    @Override
    public boolean puoSottomettere() {
        return false;
    }

    @Override
    public boolean puoValutare() {
        return true;
    }

    @Override
    public boolean puoProclamare() {
        return false;
    }

    @Override
    public boolean puoRichiedereSupporto() {
        return false;
    }

    @Override
    public StatoHackathon avanzaStato() {
        return new StatoConcluso();
    }

    @Override
    public String getNome() {
        return "IN_VALUTAZIONE";
    }
}
