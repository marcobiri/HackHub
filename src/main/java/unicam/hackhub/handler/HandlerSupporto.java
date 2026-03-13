package unicam.hackhub.handler;

import org.springframework.stereotype.Service;
import unicam.hackhub.model.hackathon.Hackathon;
import unicam.hackhub.model.staff.Mentore;
import unicam.hackhub.model.support.RichiestaSupporto;
import unicam.hackhub.model.support.SegnalazioneViolazione;
import unicam.hackhub.model.team.Team;
import unicam.hackhub.repository.HackathonRepository;
import unicam.hackhub.service.CalendarService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Handler per la gestione delle richieste di supporto e segnalazioni.
 * Il mentore gestisce le richieste dei team e propone call via Calendar.
 */
@Service
public class HandlerSupporto {

    private final HackathonRepository hackathonRepository;
    private final CalendarService calendarService;

    // In un'applicazione reale si userebbe un repository dedicato
    private final List<RichiestaSupporto> richieste = new ArrayList<>();
    private final List<SegnalazioneViolazione> segnalazioni = new ArrayList<>();

    public HandlerSupporto(HackathonRepository hackathonRepository,
            CalendarService calendarService) {
        this.hackathonRepository = hackathonRepository;
        this.calendarService = calendarService;
    }

    /**
     * Crea una richiesta di supporto dal team al mentore.
     * Possibile solo quando l'hackathon è IN_CORSO.
     */
    public RichiestaSupporto creaRichiestaSupporto(String messaggio, Team team, Long hackathonId) {
        Hackathon hackathon = hackathonRepository.findById(Objects.requireNonNull(hackathonId))
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato con ID: " + hackathonId));

        if (!hackathon.puoRichiedereSupporto()) {
            throw new IllegalStateException(
                    "Non è possibile richiedere supporto nello stato: " + hackathon.getStatoCorrente());
        }

        RichiestaSupporto richiesta = new RichiestaSupporto(messaggio, team, hackathon);
        richieste.add(richiesta);
        return richiesta;
    }

    /**
     * Il mentore propone una call per una richiesta di supporto.
     * La prenotazione è delegata al sistema Calendar esterno.
     */
    public RichiestaSupporto propostaCall(RichiestaSupporto richiesta, Mentore mentore) {
        String calendarEventId = calendarService.prenotaCall(
                mentore.getUsername(),
                richiesta.getTeam().getNome());
        richiesta.propostaCall(mentore, calendarEventId);
        return richiesta;
    }

    /**
     * Crea una segnalazione di violazione del regolamento.
     * Il mentore segnala un team all'organizzatore.
     */
    public SegnalazioneViolazione segnalaViolazione(String descrizione, Team team,
            Mentore mentore, Long hackathonId) {
        Hackathon hackathon = hackathonRepository.findById(Objects.requireNonNull(hackathonId))
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato con ID: " + hackathonId));

        SegnalazioneViolazione segnalazione = new SegnalazioneViolazione(descrizione, team, mentore, hackathon);
        segnalazioni.add(segnalazione);
        return segnalazione;
    }

    /**
     * Restituisce le richieste di supporto per un hackathon.
     */
    public List<RichiestaSupporto> getRichiestePerHackathon(Long hackathonId) {
        return richieste.stream()
                .filter(r -> r.getHackathon().getId().equals(hackathonId))
                .toList();
    }

    /**
     * Restituisce le segnalazioni per un hackathon.
     */
    public List<SegnalazioneViolazione> getSegnalazioniPerHackathon(Long hackathonId) {
        return segnalazioni.stream()
                .filter(s -> s.getHackathon().getId().equals(hackathonId))
                .toList();
    }
}
