package unicam.hackhub.model.team;

import jakarta.persistence.*;
import unicam.hackhub.model.user.Utente;

import java.util.ArrayList;
import java.util.List;

/**
 * Gruppo di utenti che partecipa a uno o più hackathon.
 */
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "creatore_id")
    private Utente creatore;

    @ManyToMany
    @JoinTable(name = "team_membri", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "utente_id"))
    private List<Utente> membri = new ArrayList<>();

    public Team() {
    }

    public Team(String nome, Utente creatore) {
        this.nome = nome;
        this.creatore = creatore;
        this.membri.add(creatore);
    }

    /**
     * Aggiunge un membro al team.
     *
     * @throws IllegalStateException se il team ha raggiunto la dimensione massima
     */
    public void aggiungiMembro(Utente utente, int maxDimensione) {
        if (membri.size() >= maxDimensione) {
            throw new IllegalStateException("Il team ha raggiunto la dimensione massima: " + maxDimensione);
        }
        if (!membri.contains(utente)) {
            membri.add(utente);
        }
    }

    public void rimuoviMembro(Utente utente) {
        membri.remove(utente);
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

    public Utente getCreatore() {
        return creatore;
    }

    public void setCreatore(Utente creatore) {
        this.creatore = creatore;
    }

    public List<Utente> getMembri() {
        return membri;
    }

    public void setMembri(List<Utente> membri) {
        this.membri = membri;
    }
}
