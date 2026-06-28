package unicam.hackhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import unicam.hackhub.model.user.Utente;

import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

    Optional<Utente> findByUsername(String username);

    Optional<Utente> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    /**
     * Converte un utente base in MembroTeam aggiornando il discriminator nella tabella.
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE utenti SET tipo_utente = 'MEMBRO_TEAM' WHERE id = :id", nativeQuery = true)
    void convertiInMembroTeam(@Param("id") Long id);
}
