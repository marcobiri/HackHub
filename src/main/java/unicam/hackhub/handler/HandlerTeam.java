package unicam.hackhub.handler;

import org.springframework.stereotype.Service;
import unicam.hackhub.model.team.InvitoTeam;
import unicam.hackhub.model.team.Team;
import unicam.hackhub.model.user.Utente;
import unicam.hackhub.repository.TeamRepository;
import unicam.hackhub.repository.UtenteRepository;

import java.util.List;
import java.util.Objects;

/**
 * Handler per la gestione dei team.
 * Creazione team, gestione inviti, membri.
 */
@Service
public class HandlerTeam {

    private final TeamRepository teamRepository;
    private final UtenteRepository utenteRepository;

    public HandlerTeam(TeamRepository teamRepository, UtenteRepository utenteRepository) {
        this.teamRepository = teamRepository;
        this.utenteRepository = utenteRepository;
    }

    /**
     * Crea un nuovo team. L'utente creatore diventa automaticamente membro.
     */
    public Team creaTeam(String nome, Long creatoreId) {
        Utente creatore = utenteRepository.findById(Objects.requireNonNull(creatoreId))
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con ID: " + creatoreId));

        Team team = new Team(nome, creatore);
        return teamRepository.save(team);
    }

    /**
     * Crea un invito per un altro utente a unirsi al team.
     */
    public InvitoTeam invitaUtente(Long teamId, Long mittenteId, Long destinatarioId) {
        Team team = teamRepository.findById(Objects.requireNonNull(teamId))
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato con ID: " + teamId));
        Utente mittente = utenteRepository.findById(Objects.requireNonNull(mittenteId))
                .orElseThrow(() -> new IllegalArgumentException("Mittente non trovato con ID: " + mittenteId));
        Utente destinatario = utenteRepository.findById(Objects.requireNonNull(destinatarioId))
                .orElseThrow(() -> new IllegalArgumentException("Destinatario non trovato con ID: " + destinatarioId));

        return new InvitoTeam(team, mittente, destinatario);
    }

    /**
     * Restituisce tutti i team nel sistema.
     */
    public List<Team> getTuttiTeam() {
        return teamRepository.findAll();
    }

    /**
     * Restituisce un team per ID.
     */
    public Team getTeamById(Long id) {
        return teamRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato con ID: " + id));
    }
}
