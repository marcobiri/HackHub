package unicam.hackhub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.handler.HandlerHackathon;
import unicam.hackhub.model.hackathon.Hackathon;
import unicam.hackhub.model.staff.Giudice;
import unicam.hackhub.model.staff.Mentore;
import unicam.hackhub.model.staff.Organizzatore;
import unicam.hackhub.repository.UtenteRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Controller REST per la gestione degli hackathon.
 */
@RestController
@RequestMapping("/api/hackathon")
public class HackathonController {

    private final HandlerHackathon handlerHackathon;
    private final UtenteRepository utenteRepository;

    public HackathonController(HandlerHackathon handlerHackathon,
            UtenteRepository utenteRepository) {
        this.handlerHackathon = handlerHackathon;
        this.utenteRepository = utenteRepository;
    }

    /**
     * GET /api/hackathon — lista tutti gli hackathon (accessibile anche al
     * visitatore).
     */
    @GetMapping
    public ResponseEntity<List<Hackathon>> getTuttiHackathon() {
        return ResponseEntity.ok(handlerHackathon.getTuttiHackathon());
    }

    /**
     * GET /api/hackathon/{id} — dettaglio hackathon.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Hackathon> getHackathonById(@PathVariable Long id) {
        return ResponseEntity.ok(handlerHackathon.getHackathonById(id));
    }

    /**
     * POST /api/hackathon — crea un nuovo hackathon.
     * Body: nome, regolamento, scadenzaIscrizioni, dataInizio, dataFine,
     * luogo, premioDenaro, maxDimensioneTeam, organizzatoreId, giudiceId
     */
    @PostMapping
    public ResponseEntity<Hackathon> creaHackathon(@RequestBody Map<String, Object> body) {
        Organizzatore organizzatore = (Organizzatore) utenteRepository
                .findById(Long.parseLong(body.get("organizzatoreId").toString()))
                .orElseThrow(() -> new IllegalArgumentException("Organizzatore non trovato"));

        Giudice giudice = (Giudice) utenteRepository
                .findById(Long.parseLong(body.get("giudiceId").toString()))
                .orElseThrow(() -> new IllegalArgumentException("Giudice non trovato"));

        Hackathon hackathon = handlerHackathon.creaHackathon(
                body.get("nome").toString(),
                body.get("regolamento").toString(),
                LocalDateTime.parse(body.get("scadenzaIscrizioni").toString()),
                LocalDate.parse(body.get("dataInizio").toString()),
                LocalDate.parse(body.get("dataFine").toString()),
                body.get("luogo").toString(),
                Double.parseDouble(body.get("premioDenaro").toString()),
                Integer.parseInt(body.get("maxDimensioneTeam").toString()),
                organizzatore,
                giudice);
        return ResponseEntity.ok(hackathon);
    }

    /**
     * PUT /api/hackathon/{id}/avanza-stato — avanza lo stato dell'hackathon.
     */
    @PutMapping("/{id}/avanza-stato")
    public ResponseEntity<Hackathon> avanzaStato(@PathVariable Long id) {
        return ResponseEntity.ok(handlerHackathon.avanzaStato(id));
    }

    /**
     * POST /api/hackathon/{id}/mentore — aggiunge un mentore all'hackathon.
     */
    @PostMapping("/{id}/mentore")
    public ResponseEntity<Hackathon> aggiungiMentore(@PathVariable Long id,
            @RequestBody Map<String, Long> body) {
        Mentore mentore = (Mentore) utenteRepository
                .findById(Objects.requireNonNull(body.get("mentoreId")))
                .orElseThrow(() -> new IllegalArgumentException("Mentore non trovato"));
        return ResponseEntity.ok(handlerHackathon.aggiungiMentore(id, mentore));
    }

    /**
     * POST /api/hackathon/{id}/proclama-vincitore — proclama il vincitore.
     */
    @PostMapping("/{id}/proclama-vincitore")
    public ResponseEntity<Hackathon> proclamaVincitore(@PathVariable Long id,
            @RequestBody Map<String, Long> body) {
        return ResponseEntity.ok(handlerHackathon.proclamaVincitore(id, body.get("teamId")));
    }

    /**
     * GET /api/hackathon/stato/{stato} — filtra per stato.
     */
    @GetMapping("/stato/{stato}")
    public ResponseEntity<List<Hackathon>> getPerStato(@PathVariable String stato) {
        return ResponseEntity.ok(handlerHackathon.getHackathonPerStato(stato));
    }
}
