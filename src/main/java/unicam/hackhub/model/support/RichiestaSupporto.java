package unicam.hackhub.model.support;

import jakarta.persistence.*;
import unicam.hackhub.model.hackathon.Hackathon;
import unicam.hackhub.model.staff.Mentore;
import unicam.hackhub.model.team.Team;

import java.time.LocalDateTime;

/**
 * Richiesta di supporto (call) inviata dal team al mentore.
 * La prenotazione della call è gestita tramite il sistema Calendar esterno.
 */
@Entity
public class RichiestaSupporto {

    public enum StatoRichiesta {
        IN_ATTESA,
        CALL_PROPOSTA,
        COMPLETATA,
        RIFIUTATA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String messaggio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoRichiesta stato;

    private LocalDateTime dataRichiesta;
    private String calendarEventId;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "mentore_id")
    private Mentore mentore;

    @ManyToOne
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    public RichiestaSupporto() {
        this.stato = StatoRichiesta.IN_ATTESA;
        this.dataRichiesta = LocalDateTime.now();
    }

    public RichiestaSupporto(String messaggio, Team team, Hackathon hackathon) {
        this.messaggio = messaggio;
        this.team = team;
        this.hackathon = hackathon;
        this.stato = StatoRichiesta.IN_ATTESA;
        this.dataRichiesta = LocalDateTime.now();
    }

    public void propostaCall(Mentore mentore, String calendarEventId) {
        this.mentore = mentore;
        this.calendarEventId = calendarEventId;
        this.stato = StatoRichiesta.CALL_PROPOSTA;
    }

    public void completa() {
        this.stato = StatoRichiesta.COMPLETATA;
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public StatoRichiesta getStato() {
        return stato;
    }

    public void setStato(StatoRichiesta stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataRichiesta() {
        return dataRichiesta;
    }

    public void setDataRichiesta(LocalDateTime dataRichiesta) {
        this.dataRichiesta = dataRichiesta;
    }

    public String getCalendarEventId() {
        return calendarEventId;
    }

    public void setCalendarEventId(String calendarEventId) {
        this.calendarEventId = calendarEventId;
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
