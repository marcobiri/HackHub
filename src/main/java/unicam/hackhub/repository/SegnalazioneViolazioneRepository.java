package unicam.hackhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.model.support.SegnalazioneViolazione;

import java.util.List;

@Repository
public interface SegnalazioneViolazioneRepository extends JpaRepository<SegnalazioneViolazione, Long> {

    List<SegnalazioneViolazione> findByHackathonId(Long hackathonId);
}
