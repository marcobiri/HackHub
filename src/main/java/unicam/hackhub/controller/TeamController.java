package unicam.hackhub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.handler.HandlerHackathon;
import unicam.hackhub.handler.HandlerTeam;
import unicam.hackhub.model.team.InvitoTeam;
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

    /**
     * POST /api/team/{teamId}/invita — invia un invito a un utente.
     * Body: mittenteId, destinatarioId
     */
    @PostMapping("/{teamId}/invita")
    public ResponseEntity<InvitoTeam> invitaUtente(@PathVariable Long teamId,
            @RequestBody Map<String, Object> body) {
        InvitoTeam invito = handlerTeam.invitaUtente(
                teamId,
                Long.parseLong(body.get("mittenteId").toString()),
                Long.parseLong(body.get("destinatarioId").toString()));
        return ResponseEntity.ok(invito);
    }

    /**
     * GET /api/team/inviti/{utenteId} — lista gli inviti ricevuti da un utente.
     */
    @GetMapping("/inviti/{utenteId}")
    public ResponseEntity<List<InvitoTeam>> getInviti(@PathVariable Long utenteId) {
        return ResponseEntity.ok(handlerTeam.getInvitiPerUtente(utenteId));
    }

    /**
     * PUT /api/team/inviti/{invitoId}/accetta — accetta un invito.
     */
    @PutMapping("/inviti/{invitoId}/accetta")
    public ResponseEntity<InvitoTeam> accettaInvito(@PathVariable Long invitoId) {
        return ResponseEntity.ok(handlerTeam.accettaInvito(invitoId));
    }

    /**
     * PUT /api/team/inviti/{invitoId}/rifiuta — rifiuta un invito.
     */
    @PutMapping("/inviti/{invitoId}/rifiuta")
    public ResponseEntity<InvitoTeam> rifiutaInvito(@PathVariable Long invitoId) {
        return ResponseEntity.ok(handlerTeam.rifiutaInvito(invitoId));
    }
}
