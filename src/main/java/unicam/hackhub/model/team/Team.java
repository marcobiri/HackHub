package unicam.hackhub.model.team;

import jakarta.persistence.*;
import unicam.hackhub.model.user.Utente;

import java.util.ArrayList;
import java.util.List;

/**
 * Gruppo di utenti che partecipa a uno o più hackathon.
 * Ogni utente può appartenere a un solo team alla volta.
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

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembroTeam> membri = new ArrayList<>();

    public Team() {
    }

    public Team(String nome, MembroTeam creatore) {
        this.nome = nome;
        this.creatore = creatore;
        creatore.setTeam(this);
        this.membri.add(creatore);
    }

    /**
     * Aggiunge un membro al team.
     * 
     * @throws IllegalStateException se il team ha raggiunto la dimensione massima
     */
    public void aggiungiMembro(MembroTeam membro, int maxDimensione) {
        if (membri.size() >= maxDimensione) {
            throw new IllegalStateException("Il team ha raggiunto la dimensione massima: " + maxDimensione);
        }
        if (!membri.contains(membro)) {
            membro.setTeam(this);
            membri.add(membro);
        }
    }

    public void rimuoviMembro(MembroTeam membro) {
        membri.remove(membro);
        membro.setTeam(null);
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

    public List<MembroTeam> getMembri() {
        return membri;
    }

    public void setMembri(List<MembroTeam> membri) {
        this.membri = membri;
    }
}
