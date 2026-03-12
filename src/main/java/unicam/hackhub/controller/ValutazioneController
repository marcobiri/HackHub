package unicam.hackhub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.handler.HandlerValutazione;
import unicam.hackhub.model.staff.Giudice;
import unicam.hackhub.model.submission.Valutazione;
import unicam.hackhub.repository.UtenteRepository;

import java.util.List;
import java.util.Map;

/**
 * Controller REST per la gestione delle valutazioni.
 */
@RestController
@RequestMapping("/api/valutazione")
public class ValutazioneController {

    private final HandlerValutazione handlerValutazione;
    private final UtenteRepository utenteRepository;

    public ValutazioneController(HandlerValutazione handlerValutazione,
            UtenteRepository utenteRepository) {
        this.handlerValutazione = handlerValutazione;
        this.utenteRepository = utenteRepository;
    }

    /**
     * POST /api/valutazione — crea una valutazione.
     * Body: giudizio, punteggio, sottomissioneId, giudiceId
     */
    @PostMapping
    public ResponseEntity<Valutazione> valutaSottomissione(@RequestBody Map<String, Object> body) {
        Giudice giudice = (Giudice) utenteRepository
                .findById(Long.parseLong(body.get("giudiceId").toString()))
                .orElseThrow(() -> new IllegalArgumentException("Giudice non trovato"));

        Valutazione valutazione = handlerValutazione.valutaSottomissione(
                body.get("giudizio").toString(),
                Double.parseDouble(body.get("punteggio").toString()),
                Long.parseLong(body.get("sottomissioneId").toString()),
                giudice);
        return ResponseEntity.ok(valutazione);
    }

    /**
     * GET /api/valutazione/hackathon/{hackathonId} — valutazioni per hackathon.
     */
    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<List<Valutazione>> getPerHackathon(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(handlerValutazione.getValutazioniPerHackathon(hackathonId));
    }

    /**
     * GET /api/valutazione/giudice/{giudiceId} — valutazioni per giudice.
     */
    @GetMapping("/giudice/{giudiceId}")
    public ResponseEntity<List<Valutazione>> getPerGiudice(@PathVariable Long giudiceId) {
        return ResponseEntity.ok(handlerValutazione.getValutazioniPerGiudice(giudiceId));
    }
}
