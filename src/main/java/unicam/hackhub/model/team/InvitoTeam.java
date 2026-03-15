package unicam.hackhub.model.team;

import jakarta.persistence.*;
import unicam.hackhub.model.user.Utente;

import java.time.LocalDateTime;

/**
 * Invito a unirsi a un team.
 * Un utente può accettare o rifiutare l'invito.
 */
@Entity
public class InvitoTeam {

    public enum StatoInvito {
        IN_ATTESA,
        ACCETTATO,
        RIFIUTATO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "mittente_id", nullable = false)
    private Utente mittente;

    @ManyToOne
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Utente destinatario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoInvito stato;

    private LocalDateTime dataInvio;

    public InvitoTeam() {
        this.stato = StatoInvito.IN_ATTESA;
        this.dataInvio = LocalDateTime.now();
    }

    public InvitoTeam(Team team, Utente mittente, Utente destinatario) {
        this.team = team;
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.stato = StatoInvito.IN_ATTESA;
        this.dataInvio = LocalDateTime.now();
    }

    public void accetta() {
        if (this.stato != StatoInvito.IN_ATTESA) {
            throw new IllegalStateException("L'invito non è più in attesa.");
        }
        this.stato = StatoInvito.ACCETTATO;
    }

    public void rifiuta() {
        if (this.stato != StatoInvito.IN_ATTESA) {
            throw new IllegalStateException("L'invito non è più in attesa.");
        }
        this.stato = StatoInvito.RIFIUTATO;
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Utente getMittente() {
        return mittente;
    }

    public void setMittente(Utente mittente) {
        this.mittente = mittente;
    }

    public Utente getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Utente destinatario) {
        this.destinatario = destinatario;
    }

    public StatoInvito getStato() {
        return stato;
    }

    public void setStato(StatoInvito stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataInvio() {
        return dataInvio;
    }

    public void setDataInvio(LocalDateTime dataInvio) {
        this.dataInvio = dataInvio;
    }
}
