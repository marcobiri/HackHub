package unicam.hackhub.model.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import unicam.hackhub.model.user.Utente;

/**
 * Rappresenta un membro del team. Estende Utente.
 * Un utente può appartenere a un solo team per volta.
 */
@Entity
@DiscriminatorValue("MEMBRO_TEAM")
public class MembroTeam extends Utente {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public MembroTeam() {
    }

    public MembroTeam(String username, String email, String password) {
        super(username, email, password);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
