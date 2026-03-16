package unicam.hackhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.model.submission.Sottomissione;

import java.util.List;
import java.util.Optional;

@Repository
public interface SottomissioneRepository extends JpaRepository<Sottomissione, Long> {

    List<Sottomissione> findByHackathonId(Long hackathonId);

    List<Sottomissione> findByTeamId(Long teamId);

    Optional<Sottomissione> findByTeamIdAndHackathonId(Long teamId, Long hackathonId);
}
