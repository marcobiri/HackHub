package unicam.hackhub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.handler.HandlerHackathon;
import unicam.hackhub.handler.HandlerTeam;
import unicam.hackhub.model.team.Team;

import java.util.List;
import java.util.Map;

/**
 * Controller REST per la gestione dei team.
 */
@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final HandlerTeam handlerTeam;
    private final HandlerHackathon handlerHackathon;

    public TeamController(HandlerTeam handlerTeam,
            HandlerHackathon handlerHackathon) {
        this.handlerTeam = handlerTeam;
        this.handlerHackathon = handlerHackathon;
    }

    /**
     * GET /api/team — lista tutti i team.
     */
    @GetMapping
    public ResponseEntity<List<Team>> getTuttiTeam() {
        return ResponseEntity.ok(handlerTeam.getTuttiTeam());
    }

    /**
     * GET /api/team/{id} — dettaglio team.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(handlerTeam.getTeamById(id));
    }

    /**
     * POST /api/team — crea un nuovo team.
     * Body: nome, creatoreId
     */
    @PostMapping
    public ResponseEntity<Team> creaTeam(@RequestBody Map<String, Object> body) {
        Team team = handlerTeam.creaTeam(
                body.get("nome").toString(),
                Long.parseLong(body.get("creatoreId").toString()));
        return ResponseEntity.ok(team);
    }

    /**
     * POST /api/team/{teamId}/iscrivi/{hackathonId} — iscrive un team a un
     * hackathon.
     */
    @PostMapping("/{teamId}/iscrivi/{hackathonId}")
    public ResponseEntity<String> iscriviAdHackathon(@PathVariable Long teamId,
            @PathVariable Long hackathonId) {
        Team team = handlerTeam.getTeamById(teamId);
        handlerHackathon.iscriviTeam(hackathonId, team);
        return ResponseEntity.ok("Team iscritto con successo all'hackathon.");
    }
}
