package unicam.hackhub.model.hackathon;

/**
 * Stato iniziale: l'hackathon è aperto alle iscrizioni dei team.
 * Operazioni permesse: iscrizione team.
 */
public class StatoInIscrizione implements StatoHackathon {

    @Override
    public boolean puoIscrivere() {
        return true;
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
        return false;
    }

    @Override
    public boolean puoRichiedereSupporto() {
        return false;
    }

    @Override
    public StatoHackathon avanzaStato() {
        return new StatoInCorso();
    }

    @Override
    public String getNome() {
        return "IN_ISCRIZIONE";
    }
}
