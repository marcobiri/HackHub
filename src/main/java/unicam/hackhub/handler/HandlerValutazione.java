package unicam.hackhub.handler;

import org.springframework.stereotype.Service;
import unicam.hackhub.model.hackathon.Hackathon;
import unicam.hackhub.model.staff.Giudice;
import unicam.hackhub.model.submission.Sottomissione;
import unicam.hackhub.model.submission.Valutazione;
import unicam.hackhub.repository.SottomissioneRepository;
import unicam.hackhub.repository.ValutazioneRepository;

import java.util.List;
import java.util.Objects;

/**
 * Handler per la gestione delle valutazioni.
 * Il giudice valuta le sottomissioni con giudizio scritto e punteggio 0-10.
 */
@Service
public class HandlerValutazione {

    private final ValutazioneRepository valutazioneRepository;
    private final SottomissioneRepository sottomissioneRepository;

    public HandlerValutazione(ValutazioneRepository valutazioneRepository,
            SottomissioneRepository sottomissioneRepository) {
        this.valutazioneRepository = valutazioneRepository;
        this.sottomissioneRepository = sottomissioneRepository;
    }

    /**
     * Crea una valutazione per una sottomissione.
     * Possibile solo quando l'hackathon è IN_VALUTAZIONE.
     *
     * @param giudizio        giudizio scritto
     * @param punteggio       punteggio 0-10
     * @param sottomissioneId ID della sottomissione
     * @param giudice         il giudice che valuta
     * @return la valutazione creata
     */
    public Valutazione valutaSottomissione(String giudizio, double punteggio,
            Long sottomissioneId, Giudice giudice) {
        Sottomissione sottomissione = sottomissioneRepository.findById(Objects.requireNonNull(sottomissioneId))
                .orElseThrow(
                        () -> new IllegalArgumentException("Sottomissione non trovata con ID: " + sottomissioneId));

        Hackathon hackathon = sottomissione.getHackathon();
        if (!hackathon.puoValutare()) {
            throw new IllegalStateException(
                    "Non è possibile valutare nello stato: " + hackathon.getStatoCorrente());
        }

        Valutazione valutazione = new Valutazione(giudizio, punteggio, sottomissione, giudice);
        return valutazioneRepository.save(valutazione);
    }

    /**
     * Restituisce tutte le valutazioni di un hackathon.
     */
    public List<Valutazione> getValutazioniPerHackathon(Long hackathonId) {
        return valutazioneRepository.findBySottomissioneHackathonId(hackathonId);
    }

    /**
     * Restituisce le valutazioni fatte da un giudice.
     */
    public List<Valutazione> getValutazioniPerGiudice(Long giudiceId) {
        return valutazioneRepository.findByGiudiceId(giudiceId);
    }
}
