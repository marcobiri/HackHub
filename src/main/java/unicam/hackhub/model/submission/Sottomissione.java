package unicam.hackhub.model.submission;

import jakarta.persistence.*;
import unicam.hackhub.model.hackathon.Hackathon;
import unicam.hackhub.model.team.Team;

import java.time.LocalDateTime;

/**
 * Sottomissione (deliverable) inviata da un team per un hackathon.
 * Aggiornabile fino alla scadenza.
 */
@Entity
public class Sottomissione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10000)
    private String contenuto;

    private String titolo;
    private LocalDateTime dataInvio;
    private LocalDateTime dataUltimoAggiornamento;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    public Sottomissione() {
        this.dataInvio = LocalDateTime.now();
        this.dataUltimoAggiornamento = LocalDateTime.now();
    }

    public Sottomissione(String titolo, String contenuto, Team team, Hackathon hackathon) {
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.team = team;
        this.hackathon = hackathon;
        this.dataInvio = LocalDateTime.now();
        this.dataUltimoAggiornamento = LocalDateTime.now();
    }

    /**
     * Aggiorna il contenuto della sottomissione.
     */
    public void aggiorna(String nuovoContenuto) {
        this.contenuto = nuovoContenuto;
        this.dataUltimoAggiornamento = LocalDateTime.now();
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getContenuto() {
        return contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    public LocalDateTime getDataInvio() {
        return dataInvio;
    }

    public void setDataInvio(LocalDateTime dataInvio) {
        this.dataInvio = dataInvio;
    }

    public LocalDateTime getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }

    public void setDataUltimoAggiornamento(LocalDateTime dataUltimoAggiornamento) {
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Hackathon getHackathon() {
        return hackathon;
    }

    public void setHackathon(Hackathon hackathon) {
        this.hackathon = hackathon;
    }
}
