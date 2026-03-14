package unicam.hackhub.model.submission;

import jakarta.persistence.*;
import unicam.hackhub.model.staff.Giudice;

import java.time.LocalDateTime;

/**
 * Valutazione di una sottomissione da parte del giudice.
 * Composta da un giudizio scritto e da un punteggio 0-10.
 */
@Entity
public class Valutazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String giudizio;

    /** Punteggio numerico compreso tra 0 e 10 */
    @Column(nullable = false)
    private double punteggio;

    private LocalDateTime dataValutazione;

    @ManyToOne
    @JoinColumn(name = "sottomissione_id", nullable = false)
    private Sottomissione sottomissione;

    @ManyToOne
    @JoinColumn(name = "giudice_id", nullable = false)
    private Giudice giudice;

    public Valutazione() {
        this.dataValutazione = LocalDateTime.now();
    }

    public Valutazione(String giudizio, double punteggio, Sottomissione sottomissione, Giudice giudice) {
        if (punteggio < 0 || punteggio > 10) {
            throw new IllegalArgumentException("Il punteggio deve essere compreso tra 0 e 10.");
        }
        this.giudizio = giudizio;
        this.punteggio = punteggio;
        this.sottomissione = sottomissione;
        this.giudice = giudice;
        this.dataValutazione = LocalDateTime.now();
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGiudizio() {
        return giudizio;
    }

    public void setGiudizio(String giudizio) {
        this.giudizio = giudizio;
    }

    public double getPunteggio() {
        return punteggio;
    }

    public void setPunteggio(double punteggio) {
        if (punteggio < 0 || punteggio > 10) {
            throw new IllegalArgumentException("Il punteggio deve essere compreso tra 0 e 10.");
        }
        this.punteggio = punteggio;
    }

    public LocalDateTime getDataValutazione() {
        return dataValutazione;
    }

    public void setDataValutazione(LocalDateTime dataValutazione) {
        this.dataValutazione = dataValutazione;
    }

    public Sottomissione getSottomissione() {
        return sottomissione;
    }

    public void setSottomissione(Sottomissione sottomissione) {
        this.sottomissione = sottomissione;
    }

    public Giudice getGiudice() {
        return giudice;
    }

    public void setGiudice(Giudice giudice) {
        this.giudice = giudice;
    }
}
