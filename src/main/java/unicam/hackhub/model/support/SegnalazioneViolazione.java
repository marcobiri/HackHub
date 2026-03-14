package unicam.hackhub.model.support;

import jakarta.persistence.*;
import unicam.hackhub.model.hackathon.Hackathon;
import unicam.hackhub.model.staff.Mentore;
import unicam.hackhub.model.team.Team;

import java.time.LocalDateTime;

/**
 * Segnalazione di violazione del regolamento da parte di un mentore.
 * Il mentore segnala il team all'organizzatore per le decisioni del caso.
 */
@Entity
public class SegnalazioneViolazione {

    public enum StatoSegnalazione {
        APERTA,
        IN_REVISIONE,
        CONFERMATA,
        RIGETTATA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000, nullable = false)
    private String descrizione;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoSegnalazione stato;

    private LocalDateTime dataSegnalazione;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "mentore_id", nullable = false)
    private Mentore mentore;

    @ManyToOne
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    public SegnalazioneViolazione() {
        this.stato = StatoSegnalazione.APERTA;
        this.dataSegnalazione = LocalDateTime.now();
    }

    public SegnalazioneViolazione(String descrizione, Team team, Mentore mentore, Hackathon hackathon) {
        this.descrizione = descrizione;
        this.team = team;
        this.mentore = mentore;
        this.hackathon = hackathon;
        this.stato = StatoSegnalazione.APERTA;
        this.dataSegnalazione = LocalDateTime.now();
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public StatoSegnalazione getStato() {
        return stato;
    }

    public void setStato(StatoSegnalazione stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataSegnalazione() {
        return dataSegnalazione;
    }

    public void setDataSegnalazione(LocalDateTime dataSegnalazione) {
        this.dataSegnalazione = dataSegnalazione;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Mentore getMentore() {
        return mentore;
    }

    public void setMentore(Mentore mentore) {
        this.mentore = mentore;
    }

    public Hackathon getHackathon() {
        return hackathon;
    }

    public void setHackathon(Hackathon hackathon) {
        this.hackathon = hackathon;
    }
}
