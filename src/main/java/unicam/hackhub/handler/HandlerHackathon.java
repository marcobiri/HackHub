package unicam.hackhub.handler;

import org.springframework.stereotype.Service;
import unicam.hackhub.model.hackathon.Hackathon;
import unicam.hackhub.model.payment.Premio;
import unicam.hackhub.model.staff.Giudice;
import unicam.hackhub.model.staff.Mentore;
import unicam.hackhub.model.staff.Organizzatore;
import unicam.hackhub.model.team.Team;
import unicam.hackhub.repository.HackathonRepository;
import unicam.hackhub.repository.SottomissioneRepository;
import unicam.hackhub.repository.ValutazioneRepository;
import unicam.hackhub.service.PaymentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Handler per la gestione degli hackathon.
 * Contiene la logica business per CRUD, transizioni di stato,
 * gestione staff e proclamazione vincitore.
 */
@Service
public class HandlerHackathon {

    private final HackathonRepository hackathonRepository;
    private final SottomissioneRepository sottomissioneRepository;
    private final ValutazioneRepository valutazioneRepository;
    private final PaymentService paymentService;

    public HandlerHackathon(HackathonRepository hackathonRepository,
            SottomissioneRepository sottomissioneRepository,
            ValutazioneRepository valutazioneRepository,
            PaymentService paymentService) {
        this.hackathonRepository = hackathonRepository;
        this.sottomissioneRepository = sottomissioneRepository;
        this.valutazioneRepository = valutazioneRepository;
        this.paymentService = paymentService;
    }

    /**
     * Crea un nuovo hackathon con le informazioni essenziali.
     */
    public Hackathon creaHackathon(String nome, String regolamento,
            LocalDateTime scadenzaIscrizioni,
            LocalDate dataInizio, LocalDate dataFine,
            String luogo, double premioDenaro,
            int maxDimensioneTeam,
            Organizzatore organizzatore, Giudice giudice) {
        Hackathon hackathon = new Hackathon(nome, regolamento, scadenzaIscrizioni,
                dataInizio, dataFine, luogo, premioDenaro, maxDimensioneTeam,
                organizzatore, giudice);
        return hackathonRepository.save(hackathon);
    }

    /**
     * Restituisce tutti gli hackathon nel sistema.
     */
    public List<Hackathon> getTuttiHackathon() {
        return hackathonRepository.findAll();
    }

    /**
     * Restituisce un hackathon per ID.
     */
    public Hackathon getHackathonById(Long id) {
        return hackathonRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato con ID: " + id));
    }

    /**
     * Avanza lo stato dell'hackathon al prossimo nel ciclo di vita.
     */
    public Hackathon avanzaStato(Long hackathonId) {
        Hackathon hackathon = getHackathonById(hackathonId);
        hackathon.avanzaStato();
        return hackathonRepository.save(hackathon);
    }

    /**
     * Aggiunge un mentore all'hackathon.
     * L'organizzatore può farlo in qualsiasi momento.
     */
    public Hackathon aggiungiMentore(Long hackathonId, Mentore mentore) {
        Hackathon hackathon = getHackathonById(hackathonId);
        hackathon.aggiungiMentore(mentore);
        return hackathonRepository.save(hackathon);
    }

    /**
     * Iscrive un team all'hackathon.
     * Possibile solo nello stato IN_ISCRIZIONE.
     */
    public Hackathon iscriviTeam(Long hackathonId, Team team) {
        Hackathon hackathon = getHackathonById(hackathonId);
        hackathon.aggiungiTeam(team);
        return hackathonRepository.save(hackathon);
    }

    /**
     * Proclama il team vincitore di un hackathon.
     * Verifica che tutte le sottomissioni siano state valutate dal giudice.
     * Possibile solo nello stato CONCLUSO.
     */
    public Hackathon proclamaVincitore(Long hackathonId, Long teamVincitoreId) {
        Hackathon hackathon = getHackathonById(hackathonId);

        // Verifica che tutte le sottomissioni siano state valutate
        var sottomissioni = sottomissioneRepository.findByHackathonId(hackathonId);
        var valutazioni = valutazioneRepository.findBySottomissioneHackathonId(hackathonId);

        if (sottomissioni.size() != valutazioni.size()) {
            throw new IllegalStateException(
                    "Non tutte le sottomissioni sono state valutate. " +
                            "Sottomissioni: " + sottomissioni.size() + ", Valutazioni: " + valutazioni.size());
        }

        Team vincitore = hackathon.getTeams().stream()
                .filter(t -> t.getId().equals(teamVincitoreId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Team non partecipante all'hackathon."));

        hackathon.proclamaVincitore(vincitore);
        hackathonRepository.save(hackathon);

        // Eroga il premio tramite il sistema di pagamento esterno
        paymentService.erogaPremio(new Premio(hackathon.getPremioDenaro(), hackathon, vincitore));

        return hackathon;
    }

    /**
     * Restituisce gli hackathon filtrati per stato.
     */
    public List<Hackathon> getHackathonPerStato(String stato) {
        return hackathonRepository.findByStatoCorrente(stato);
    }
}
