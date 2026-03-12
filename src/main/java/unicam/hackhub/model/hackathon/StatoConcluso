package unicam.hackhub.model.hackathon;

/**
 * Hackathon concluso: vincitore proclamato, premio erogato.
 * Non sono permesse ulteriori operazioni di modifica.
 */
public class StatoConcluso implements StatoHackathon {

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
        return false;
    }

    @Override
    public boolean puoProclamare() {
        return true;
    }

    @Override
    public boolean puoRichiedereSupporto() {
        return false;
    }

    @Override
    public StatoHackathon avanzaStato() {
        throw new IllegalStateException("L'hackathon è già concluso, non è possibile avanzare ulteriormente.");
    }

    @Override
    public String getNome() {
        return "CONCLUSO";
    }
}
