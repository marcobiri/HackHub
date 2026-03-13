package unicam.hackhub.model.hackathon;

import jakarta.persistence.*;
import unicam.hackhub.model.staff.Giudice;
import unicam.hackhub.model.staff.Mentore;
import unicam.hackhub.model.staff.Organizzatore;
import unicam.hackhub.model.team.Team;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entità principale: rappresenta un hackathon con il proprio ciclo di vita.
 * Utilizza lo State Pattern per gestire le transizioni di stato.
 */
@Entity
public class Hackathon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 5000)
    private String regolamento;

    private LocalDateTime scadenzaIscrizioni;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private String luogo;
    private double premioDenaro;
    private int maxDimensioneTeam;

    /** Stato corrente nel ciclo di vita (serializzato come stringa) */
    @Column(nullable = false)
    private String statoCorrente;

    @ManyToOne
    @JoinColumn(name = "organizzatore_id")
    private Organizzatore organizzatore;

    @ManyToOne
    @JoinColumn(name = "giudice_id")
    private Giudice giudice;

    @ManyToMany
    @JoinTable(name = "hackathon_mentori", joinColumns = @JoinColumn(name = "hackathon_id"), inverseJoinColumns = @JoinColumn(name = "mentore_id"))
    private List<Mentore> mentori = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "hackathon_teams", joinColumns = @JoinColumn(name = "hackathon_id"), inverseJoinColumns = @JoinColumn(name = "team_id"))
    private List<Team> teams = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "vincitore_id")
    private Team vincitore;

    public Hackathon() {
        this.statoCorrente = "IN_ISCRIZIONE";
    }

    public Hackathon(String nome, String regolamento, LocalDateTime scadenzaIscrizioni,
            LocalDate dataInizio, LocalDate dataFine, String luogo,
            double premioDenaro, int maxDimensioneTeam,
            Organizzatore organizzatore, Giudice giudice) {
        this.nome = nome;
        this.regolamento = regolamento;
        this.scadenzaIscrizioni = scadenzaIscrizioni;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.luogo = luogo;
        this.premioDenaro = premioDenaro;
        this.maxDimensioneTeam = maxDimensioneTeam;
        this.organizzatore = organizzatore;
        this.giudice = giudice;
        this.statoCorrente = "IN_ISCRIZIONE";
    }

    // --- State Pattern ---

    /**
     * Restituisce l'oggetto stato attuale basato sulla stringa persistita.
     */
    @Transient
    public StatoHackathon getStato() {
        return switch (statoCorrente) {
            case "IN_ISCRIZIONE" -> new StatoInIscrizione();
            case "IN_CORSO" -> new StatoInCorso();
            case "IN_VALUTAZIONE" -> new StatoInValutazione();
            case "CONCLUSO" -> new StatoConcluso();
            default -> throw new IllegalStateException("Stato sconosciuto: " + statoCorrente);
        };
    }

    /**
     * Avanza al prossimo stato nel ciclo di vita.
     * 
     * @throws IllegalStateException se non è possibile avanzare
     */
    public void avanzaStato() {
        StatoHackathon nuovoStato = getStato().avanzaStato();
        this.statoCorrente = nuovoStato.getNome();
    }

    public boolean puoIscrivere() {
        return getStato().puoIscrivere();
    }

    public boolean puoSottomettere() {
        return getStato().puoSottomettere();
    }

    public boolean puoValutare() {
        return getStato().puoValutare();
    }

    public boolean puoProclamare() {
        return getStato().puoProclamare();
    }

    public boolean puoRichiedereSupporto() {
        return getStato().puoRichiedereSupporto();
    }

    // --- Team Management ---

    public void aggiungiTeam(Team team) {
        if (!puoIscrivere()) {
            throw new IllegalStateException("Le iscrizioni non sono aperte in questo stato: " + statoCorrente);
        }
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }

    public void aggiungiMentore(Mentore mentore) {
        if (!mentori.contains(mentore)) {
            mentori.add(mentore);
        }
    }

    public void proclamaVincitore(Team team) {
        if (!puoProclamare()) {
            throw new IllegalStateException("Non è possibile proclamare il vincitore nello stato: " + statoCorrente);
        }
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("Il team non partecipa a questo hackathon.");
        }
        this.vincitore = team;
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRegolamento() {
        return regolamento;
    }

    public void setRegolamento(String regolamento) {
        this.regolamento = regolamento;
    }

    public LocalDateTime getScadenzaIscrizioni() {
        return scadenzaIscrizioni;
    }

    public void setScadenzaIscrizioni(LocalDateTime scadenzaIscrizioni) {
        this.scadenzaIscrizioni = scadenzaIscrizioni;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public double getPremioDenaro() {
        return premioDenaro;
    }

    public void setPremioDenaro(double premioDenaro) {
        this.premioDenaro = premioDenaro;
    }

    public int getMaxDimensioneTeam() {
        return maxDimensioneTeam;
    }

    public void setMaxDimensioneTeam(int maxDimensioneTeam) {
        this.maxDimensioneTeam = maxDimensioneTeam;
    }

    public String getStatoCorrente() {
        return statoCorrente;
    }

    public void setStatoCorrente(String statoCorrente) {
        this.statoCorrente = statoCorrente;
    }

    public Organizzatore getOrganizzatore() {
        return organizzatore;
    }

    public void setOrganizzatore(Organizzatore organizzatore) {
        this.organizzatore = organizzatore;
    }

    public Giudice getGiudice() {
        return giudice;
    }

    public void setGiudice(Giudice giudice) {
        this.giudice = giudice;
    }

    public List<Mentore> getMentori() {
        return mentori;
    }

    public void setMentori(List<Mentore> mentori) {
        this.mentori = mentori;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public Team getVincitore() {
        return vincitore;
    }

    public void setVincitore(Team vincitore) {
        this.vincitore = vincitore;
    }
}
