package unicam.hackhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.model.submission.Valutazione;

import java.util.List;

@Repository
public interface ValutazioneRepository extends JpaRepository<Valutazione, Long> {

    List<Valutazione> findByGiudiceId(Long giudiceId);

    List<Valutazione> findBySottomissioneId(Long sottomissioneId);

    List<Valutazione> findBySottomissioneHackathonId(Long hackathonId);
}
