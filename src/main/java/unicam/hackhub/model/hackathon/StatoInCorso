package unicam.hackhub.model.hackathon;

/**
 * Hackathon in corso: i team lavorano, inviano sottomissioni e possono
 * richiedere supporto al mentore.
 */
public class StatoInCorso implements StatoHackathon {

    @Override
    public boolean puoIscrivere() {
        return false;
    }

    @Override
    public boolean puoSottomettere() {
        return true;
    }

    @Override
    public boolean puoValutare() {
        return false;
    }

    @Override
    public boolean puoProclamare() {
        return false;
    }

    @Override
    public boolean puoRichiedereSupporto() {
        return true;
    }

    @Override
    public StatoHackathon avanzaStato() {
        return new StatoInValutazione();
    }

    @Override
    public String getNome() {
        return "IN_CORSO";
    }
}
