package unicam.hackhub.handler;

import org.springframework.stereotype.Service;
import unicam.hackhub.model.hackathon.Hackathon;
import unicam.hackhub.model.staff.Mentore;
import unicam.hackhub.model.support.RichiestaSupporto;
import unicam.hackhub.model.support.SegnalazioneViolazione;
import unicam.hackhub.model.team.Team;
import unicam.hackhub.repository.HackathonRepository;
import unicam.hackhub.repository.RichiestaSupportoRepository;
import unicam.hackhub.repository.SegnalazioneViolazioneRepository;
import unicam.hackhub.service.CalendarService;

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
    private final RichiestaSupportoRepository richiestaSupportoRepository;
    private final SegnalazioneViolazioneRepository segnalazioneViolazioneRepository;

    public HandlerSupporto(HackathonRepository hackathonRepository,
            CalendarService calendarService,
            RichiestaSupportoRepository richiestaSupportoRepository,
            SegnalazioneViolazioneRepository segnalazioneViolazioneRepository) {
        this.hackathonRepository = hackathonRepository;
        this.calendarService = calendarService;
        this.richiestaSupportoRepository = richiestaSupportoRepository;
        this.segnalazioneViolazioneRepository = segnalazioneViolazioneRepository;
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
        return richiestaSupportoRepository.save(richiesta);
    }

    /**
     * Il mentore propone una call per una richiesta di supporto.
     * La prenotazione è delegata al sistema Calendar esterno.
     */
    public RichiestaSupporto propostaCall(Long richiestaId, Mentore mentore) {
        RichiestaSupporto richiesta = richiestaSupportoRepository.findById(Objects.requireNonNull(richiestaId))
                .orElseThrow(() -> new IllegalArgumentException("Richiesta non trovata con ID: " + richiestaId));
        String calendarEventId = calendarService.prenotaCall(
                mentore.getUsername(),
                richiesta.getTeam().getNome());
        richiesta.propostaCall(mentore, calendarEventId);
        return richiestaSupportoRepository.save(richiesta);
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
        return segnalazioneViolazioneRepository.save(segnalazione);
    }

    /**
     * Restituisce le richieste di supporto per un hackathon.
     */
    public List<RichiestaSupporto> getRichiestePerHackathon(Long hackathonId) {
        return richiestaSupportoRepository.findByHackathonId(hackathonId);
    }

    /**
     * Restituisce le segnalazioni per un hackathon.
     */
    public List<SegnalazioneViolazione> getSegnalazioniPerHackathon(Long hackathonId) {
        return segnalazioneViolazioneRepository.findByHackathonId(hackathonId);
    }

    /**
     * L'organizzatore prende in carico una segnalazione.
     * Transizione: APERTA → IN_REVISIONE.
     *
     * @param segnalazioneId ID della segnalazione
     * @return la segnalazione aggiornata
     */
    public SegnalazioneViolazione prendiInCaricoSegnalazione(Long segnalazioneId) {
        SegnalazioneViolazione segnalazione = findSegnalazioneById(segnalazioneId);
        segnalazione.prendiInCarico();
        return segnalazioneViolazioneRepository.save(segnalazione);
    }

    /**
     * L'organizzatore conferma la violazione segnalata.
     * Transizione: IN_REVISIONE → CONFERMATA.
     *
     * @param segnalazioneId ID della segnalazione
     * @return la segnalazione aggiornata
     */
    public SegnalazioneViolazione confermaSegnalazione(Long segnalazioneId) {
        SegnalazioneViolazione segnalazione = findSegnalazioneById(segnalazioneId);
        segnalazione.conferma();
        return segnalazioneViolazioneRepository.save(segnalazione);
    }

    /**
     * L'organizzatore rigetta la segnalazione.
     * Transizione: IN_REVISIONE → RIGETTATA.
     *
     * @param segnalazioneId ID della segnalazione
     * @return la segnalazione aggiornata
     */
    public SegnalazioneViolazione rigettaSegnalazione(Long segnalazioneId) {
        SegnalazioneViolazione segnalazione = findSegnalazioneById(segnalazioneId);
        segnalazione.rigetta();
        return segnalazioneViolazioneRepository.save(segnalazione);
    }

    /**
     * Cerca una segnalazione per ID nel database.
     */
    private SegnalazioneViolazione findSegnalazioneById(Long id) {
        return segnalazioneViolazioneRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("Segnalazione non trovata con ID: " + id));
    }
}
