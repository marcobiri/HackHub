package unicam.hackhub.handler;

import org.springframework.stereotype.Service;
import unicam.hackhub.model.factory.UtenteFactory;
import unicam.hackhub.model.team.InvitoTeam;
import unicam.hackhub.model.team.MembroTeam;
import unicam.hackhub.model.team.Team;
import unicam.hackhub.model.user.Utente;
import unicam.hackhub.repository.InvitoTeamRepository;
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
    private final InvitoTeamRepository invitoTeamRepository;

    public HandlerTeam(TeamRepository teamRepository,
                       UtenteRepository utenteRepository,
                       InvitoTeamRepository invitoTeamRepository) {
        this.teamRepository = teamRepository;
        this.utenteRepository = utenteRepository;
        this.invitoTeamRepository = invitoTeamRepository;
    }

    /**
     * Crea un nuovo team. L'utente creatore diventa automaticamente membro.
     */
    public Team creaTeam(String nome, Long creatoreId) {
        Utente utente = utenteRepository.findById(Objects.requireNonNull(creatoreId))
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con ID: " + creatoreId));

        MembroTeam creatore;
        if (utente instanceof MembroTeam mt) {
            creatore = mt;
        } else {
            creatore = UtenteFactory.creaMembroTeam(
                    utente.getUsername(), utente.getEmail(), utente.getPassword());
            creatore.setId(utente.getId());
        }

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

        InvitoTeam invito = new InvitoTeam(team, mittente, destinatario);
        return invitoTeamRepository.save(invito);
    }

    /**
     * Accetta un invito: cambia lo stato a ACCETTATO e aggiunge
     * il destinatario come membro del team.
     */
    public InvitoTeam accettaInvito(Long invitoId) {
        InvitoTeam invito = invitoTeamRepository.findById(Objects.requireNonNull(invitoId))
                .orElseThrow(() -> new IllegalArgumentException("Invito non trovato con ID: " + invitoId));

        // Verifica che l'utente non appartenga già a un team
        Utente destinatario = invito.getDestinatario();
        if (destinatario instanceof MembroTeam mt && mt.getTeam() != null) {
            throw new IllegalStateException(
                    "L'utente appartiene già a un team. Un utente può appartenere a un solo team alla volta.");
        }

        invito.accetta();

        // Converti il destinatario in MembroTeam e aggiungilo al team
        MembroTeam nuovoMembro;
        if (destinatario instanceof MembroTeam mt) {
            nuovoMembro = mt;
        } else {
            nuovoMembro = UtenteFactory.creaMembroTeam(
                    destinatario.getUsername(), destinatario.getEmail(), destinatario.getPassword());
            nuovoMembro.setId(destinatario.getId());
        }

        Team team = invito.getTeam();
        team.aggiungiMembro(nuovoMembro, Integer.MAX_VALUE);
        teamRepository.save(team);

        return invitoTeamRepository.save(invito);
    }

    /**
     * Rifiuta un invito: cambia lo stato a RIFIUTATO.
     */
    public InvitoTeam rifiutaInvito(Long invitoId) {
        InvitoTeam invito = invitoTeamRepository.findById(Objects.requireNonNull(invitoId))
                .orElseThrow(() -> new IllegalArgumentException("Invito non trovato con ID: " + invitoId));

        invito.rifiuta();
        return invitoTeamRepository.save(invito);
    }

    /**
     * Restituisce gli inviti ricevuti da un utente.
     */
    public List<InvitoTeam> getInvitiPerUtente(Long utenteId) {
        return invitoTeamRepository.findByDestinatarioId(utenteId);
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
