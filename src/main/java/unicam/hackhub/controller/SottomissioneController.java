package unicam.hackhub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.handler.HandlerSottomissione;
import unicam.hackhub.model.submission.Sottomissione;
import unicam.hackhub.model.team.Team;
import unicam.hackhub.repository.TeamRepository;

import java.util.List;
import java.util.Map;

/**
 * Controller REST per la gestione delle sottomissioni.
 */
@RestController
@RequestMapping("/api/sottomissione")
public class SottomissioneController {

    private final HandlerSottomissione handlerSottomissione;
    private final TeamRepository teamRepository;

    public SottomissioneController(HandlerSottomissione handlerSottomissione,
            TeamRepository teamRepository) {
        this.handlerSottomissione = handlerSottomissione;
        this.teamRepository = teamRepository;
    }

    /**
     * POST /api/sottomissione — invia una nuova sottomissione.
     * Body: titolo, contenuto, teamId, hackathonId
     */
    @PostMapping
    public ResponseEntity<Sottomissione> inviaSottomissione(@RequestBody Map<String, Object> body) {
        Team team = teamRepository.findById(Long.parseLong(body.get("teamId").toString()))
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));

        Sottomissione sottomissione = handlerSottomissione.inviaSottomissione(
                body.get("titolo").toString(),
                body.get("contenuto").toString(),
                team,
                Long.parseLong(body.get("hackathonId").toString()));
        return ResponseEntity.ok(sottomissione);
    }

    /**
     * PUT /api/sottomissione/{id} — aggiorna una sottomissione esistente.
     * Body: contenuto
     */
    @PutMapping("/{id}")
    public ResponseEntity<Sottomissione> aggiornaSottomissione(@PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
                handlerSottomissione.aggiornaSottomissione(id, body.get("contenuto")));
    }

    /**
     * GET /api/sottomissione/hackathon/{hackathonId} — sottomissioni per hackathon.
     */
    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<List<Sottomissione>> getPerHackathon(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(handlerSottomissione.getSottomissioniPerHackathon(hackathonId));
    }

    /**
     * GET /api/sottomissione/team/{teamId} — sottomissioni per team.
     */
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<Sottomissione>> getPerTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(handlerSottomissione.getSottomissioniPerTeam(teamId));
    }
}
