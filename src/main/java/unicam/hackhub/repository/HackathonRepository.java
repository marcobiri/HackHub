package unicam.hackhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.model.hackathon.Hackathon;

import java.util.List;

@Repository
public interface HackathonRepository extends JpaRepository<Hackathon, Long> {

    List<Hackathon> findByStatoCorrente(String statoCorrente);

    List<Hackathon> findByOrganizzatoreId(Long organizzatoreId);

    List<Hackathon> findByGiudiceId(Long giudiceId);
}
