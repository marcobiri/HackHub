package unicam.hackhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.model.team.InvitoTeam;

import java.util.List;

@Repository
public interface InvitoTeamRepository extends JpaRepository<InvitoTeam, Long> {

    /**
     * Trova tutti gli inviti destinati a un utente.
     */
    List<InvitoTeam> findByDestinatarioId(Long destinatarioId);

    /**
     * Trova tutti gli inviti per un team.
     */
    List<InvitoTeam> findByTeamId(Long teamId);
}
