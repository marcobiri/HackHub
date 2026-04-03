package unicam.hackhub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.handler.HandlerSupporto;
import unicam.hackhub.model.staff.Mentore;
import unicam.hackhub.model.support.RichiestaSupporto;
import unicam.hackhub.model.support.SegnalazioneViolazione;
import unicam.hackhub.model.team.Team;
import unicam.hackhub.repository.TeamRepository;
import unicam.hackhub.repository.UtenteRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Controller REST per la gestione del supporto e delle segnalazioni.
 * Espone le funzionalità di HandlerSupporto via API REST.
 */
@RestController
@RequestMapping("/api/supporto")
public class SupportoController {

    private final HandlerSupporto handlerSupporto;
    private final TeamRepository teamRepository;
    private final UtenteRepository utenteRepository;

    public SupportoController(HandlerSupporto handlerSupporto,
                              TeamRepository teamRepository,
                              UtenteRepository utenteRepository) {
        this.handlerSupporto = handlerSupporto;
        this.teamRepository = teamRepository;
        this.utenteRepository = utenteRepository;
    }

    /**
     * POST /api/supporto/richiesta — crea una richiesta di supporto.
     * Body: messaggio, teamId, hackathonId
     */
    @PostMapping("/richiesta")
    public ResponseEntity<RichiestaSupporto> creaRichiesta(@RequestBody Map<String, Object> body) {
        Team team = teamRepository
                .findById(Long.parseLong(body.get("teamId").toString()))
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));

        RichiestaSupporto richiesta = handlerSupporto.creaRichiestaSupporto(
                body.get("messaggio").toString(),
                team,
                Long.parseLong(body.get("hackathonId").toString()));
        return ResponseEntity.ok(richiesta);
    }

    /**
     * GET /api/supporto/richieste/{hackathonId} — lista richieste per hackathon.
     */
    @GetMapping("/richieste/{hackathonId}")
    public ResponseEntity<List<RichiestaSupporto>> getRichieste(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(handlerSupporto.getRichiestePerHackathon(hackathonId));
    }

    /**
     * POST /api/supporto/richiesta/{id}/call — il mentore propone una call.
     * Body: mentoreId
     */
    @PostMapping("/richiesta/{id}/call")
    public ResponseEntity<RichiestaSupporto> propostaCall(@PathVariable Long id,
                                                          @RequestBody Map<String, Long> body) {
        RichiestaSupporto richiesta = handlerSupporto.getRichiestePerHackathon(null).stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Richiesta non trovata con ID: " + id));

        Mentore mentore = (Mentore) utenteRepository
                .findById(Objects.requireNonNull(body.get("mentoreId")))
                .orElseThrow(() -> new IllegalArgumentException("Mentore non trovato"));

        return ResponseEntity.ok(handlerSupporto.propostaCall(richiesta, mentore));
    }

    /**
     * POST /api/supporto/segnalazione — segnala una violazione del regolamento.
     * Body: descrizione, teamId, mentoreId, hackathonId
     */
    @PostMapping("/segnalazione")
    public ResponseEntity<SegnalazioneViolazione> segnalaViolazione(@RequestBody Map<String, Object> body) {
        Team team = teamRepository
                .findById(Long.parseLong(body.get("teamId").toString()))
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));

        Mentore mentore = (Mentore) utenteRepository
                .findById(Long.parseLong(body.get("mentoreId").toString()))
                .orElseThrow(() -> new IllegalArgumentException("Mentore non trovato"));

        SegnalazioneViolazione segnalazione = handlerSupporto.segnalaViolazione(
                body.get("descrizione").toString(),
                team,
                mentore,
                Long.parseLong(body.get("hackathonId").toString()));
        return ResponseEntity.ok(segnalazione);
    }

    /**
     * GET /api/supporto/segnalazioni/{hackathonId} — lista segnalazioni per hackathon.
     */
    @GetMapping("/segnalazioni/{hackathonId}")
    public ResponseEntity<List<SegnalazioneViolazione>> getSegnalazioni(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(handlerSupporto.getSegnalazioniPerHackathon(hackathonId));
    }

    /**
     * PUT /api/supporto/segnalazione/{id}/prendi-in-carico — l'organizzatore prende in carico.
     */
    @PutMapping("/segnalazione/{id}/prendi-in-carico")
    public ResponseEntity<SegnalazioneViolazione> prendiInCarico(@PathVariable Long id) {
        return ResponseEntity.ok(handlerSupporto.prendiInCaricoSegnalazione(id));
    }

    /**
     * PUT /api/supporto/segnalazione/{id}/conferma — l'organizzatore conferma la violazione.
     */
    @PutMapping("/segnalazione/{id}/conferma")
    public ResponseEntity<SegnalazioneViolazione> conferma(@PathVariable Long id) {
        return ResponseEntity.ok(handlerSupporto.confermaSegnalazione(id));
    }

    /**
     * PUT /api/supporto/segnalazione/{id}/rigetta — l'organizzatore rigetta la segnalazione.
     */
    @PutMapping("/segnalazione/{id}/rigetta")
    public ResponseEntity<SegnalazioneViolazione> rigetta(@PathVariable Long id) {
        return ResponseEntity.ok(handlerSupporto.rigettaSegnalazione(id));
    }
}
