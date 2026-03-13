package unicam.hackhub.model.payment;

import jakarta.persistence.*;
import unicam.hackhub.model.hackathon.Hackathon;
import unicam.hackhub.model.team.Team;

import java.time.LocalDateTime;

/**
 * Premio in denaro erogato al team vincitore di un hackathon.
 * L'erogazione è gestita tramite il sistema di pagamento esterno.
 */
@Entity
public class Premio {

    public enum StatoPremio {
        DA_EROGARE,
        IN_ELABORAZIONE,
        EROGATO,
        ERRORE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double importo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoPremio stato;

    private String transactionId;
    private LocalDateTime dataErogazione;

    @OneToOne
    @JoinColumn(name = "hackathon_id", nullable = false, unique = true)
    private Hackathon hackathon;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team teamVincitore;

    public Premio() {
        this.stato = StatoPremio.DA_EROGARE;
    }

    public Premio(double importo, Hackathon hackathon, Team teamVincitore) {
        this.importo = importo;
        this.hackathon = hackathon;
        this.teamVincitore = teamVincitore;
        this.stato = StatoPremio.DA_EROGARE;
    }

    public void segnaErogato(String transactionId) {
        this.transactionId = transactionId;
        this.stato = StatoPremio.EROGATO;
        this.dataErogazione = LocalDateTime.now();
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getImporto() {
        return importo;
    }

    public void setImporto(double importo) {
        this.importo = importo;
    }

    public StatoPremio getStato() {
        return stato;
    }

    public void setStato(StatoPremio stato) {
        this.stato = stato;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getDataErogazione() {
        return dataErogazione;
    }

    public void setDataErogazione(LocalDateTime dataErogazione) {
        this.dataErogazione = dataErogazione;
    }

    public Hackathon getHackathon() {
        return hackathon;
    }

    public void setHackathon(Hackathon hackathon) {
        this.hackathon = hackathon;
    }

    public Team getTeamVincitore() {
        return teamVincitore;
    }

    public void setTeamVincitore(Team teamVincitore) {
        this.teamVincitore = teamVincitore;
    }
}
