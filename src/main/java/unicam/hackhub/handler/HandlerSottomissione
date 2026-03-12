package unicam.hackhub.handler;

import org.springframework.stereotype.Service;
import unicam.hackhub.model.hackathon.Hackathon;
import unicam.hackhub.model.submission.Sottomissione;
import unicam.hackhub.model.team.Team;
import unicam.hackhub.repository.HackathonRepository;
import unicam.hackhub.repository.SottomissioneRepository;

import java.util.List;
import java.util.Objects;

/**
 * Handler per la gestione delle sottomissioni.
 * Invio e aggiornamento delle sottomissioni dei team.
 */
@Service
public class HandlerSottomissione {

    private final SottomissioneRepository sottomissioneRepository;
    private final HackathonRepository hackathonRepository;

    public HandlerSottomissione(SottomissioneRepository sottomissioneRepository,
            HackathonRepository hackathonRepository) {
        this.sottomissioneRepository = sottomissioneRepository;
        this.hackathonRepository = hackathonRepository;
    }

    /**
     * Invia una nuova sottomissione per un hackathon.
     * Possibile solo quando l'hackathon è IN_CORSO.
     */
    public Sottomissione inviaSottomissione(String titolo, String contenuto,
            Team team, Long hackathonId) {
        Hackathon hackathon = hackathonRepository.findById(Objects.requireNonNull(hackathonId))
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato con ID: " + hackathonId));

        if (!hackathon.puoSottomettere()) {
            throw new IllegalStateException(
                    "Non è possibile inviare sottomissioni nello stato: " + hackathon.getStatoCorrente());
        }

        Sottomissione sottomissione = new Sottomissione(titolo, contenuto, team, hackathon);
        return sottomissioneRepository.save(sottomissione);
    }

    /**
     * Aggiorna una sottomissione esistente.
     * Possibile solo quando l'hackathon è ancora IN_CORSO.
     */
    public Sottomissione aggiornaSottomissione(Long sottomissioneId, String nuovoContenuto) {
        Sottomissione sottomissione = sottomissioneRepository.findById(Objects.requireNonNull(sottomissioneId))
                .orElseThrow(
                        () -> new IllegalArgumentException("Sottomissione non trovata con ID: " + sottomissioneId));

        if (!sottomissione.getHackathon().puoSottomettere()) {
            throw new IllegalStateException("Non è più possibile aggiornare la sottomissione.");
        }

        sottomissione.aggiorna(nuovoContenuto);
        return sottomissioneRepository.save(sottomissione);
    }

    /**
     * Restituisce tutte le sottomissioni di un hackathon.
     */
    public List<Sottomissione> getSottomissioniPerHackathon(Long hackathonId) {
        return sottomissioneRepository.findByHackathonId(hackathonId);
    }

    /**
     * Restituisce le sottomissioni di un team.
     */
    public List<Sottomissione> getSottomissioniPerTeam(Long teamId) {
        return sottomissioneRepository.findByTeamId(teamId);
    }
}
