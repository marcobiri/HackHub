package unicam.hackhub;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import unicam.hackhub.model.hackathon.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per il Design Pattern State — Ciclo di vita dell'Hackathon.
 * Verifica le transizioni di stato e le operazioni permesse in ogni fase.
 */
class HackathonStatoTest {

    @Test
    @DisplayName("Stato iniziale deve essere IN_ISCRIZIONE")
    void statoIniziale() {
        Hackathon hackathon = new Hackathon();
        assertEquals("IN_ISCRIZIONE", hackathon.getStatoCorrente());
        assertTrue(hackathon.puoIscrivere());
        assertFalse(hackathon.puoSottomettere());
        assertFalse(hackathon.puoValutare());
        assertFalse(hackathon.puoProclamare());
    }

    @Test
    @DisplayName("Transizione IN_ISCRIZIONE → IN_CORSO")
    void transizioneInCorso() {
        Hackathon hackathon = new Hackathon();
        hackathon.avanzaStato();

        assertEquals("IN_CORSO", hackathon.getStatoCorrente());
        assertFalse(hackathon.puoIscrivere());
        assertTrue(hackathon.puoSottomettere());
        assertTrue(hackathon.puoRichiedereSupporto());
        assertFalse(hackathon.puoValutare());
    }

    @Test
    @DisplayName("Transizione IN_CORSO → IN_VALUTAZIONE")
    void transizioneInValutazione() {
        Hackathon hackathon = new Hackathon();
        hackathon.avanzaStato(); // IN_CORSO
        hackathon.avanzaStato(); // IN_VALUTAZIONE

        assertEquals("IN_VALUTAZIONE", hackathon.getStatoCorrente());
        assertTrue(hackathon.puoValutare());
        assertFalse(hackathon.puoSottomettere());
        assertFalse(hackathon.puoIscrivere());
    }

    @Test
    @DisplayName("Transizione IN_VALUTAZIONE → CONCLUSO")
    void transizioneConcluso() {
        Hackathon hackathon = new Hackathon();
        hackathon.avanzaStato(); // IN_CORSO
        hackathon.avanzaStato(); // IN_VALUTAZIONE
        hackathon.avanzaStato(); // CONCLUSO

        assertEquals("CONCLUSO", hackathon.getStatoCorrente());
        assertTrue(hackathon.puoProclamare());
        assertFalse(hackathon.puoValutare());
        assertFalse(hackathon.puoSottomettere());
        assertFalse(hackathon.puoIscrivere());
    }

    @Test
    @DisplayName("Avanzamento da CONCLUSO deve lanciare eccezione")
    void avanzamentoDaConclusoDeveFallire() {
        Hackathon hackathon = new Hackathon();
        hackathon.avanzaStato(); // IN_CORSO
        hackathon.avanzaStato(); // IN_VALUTAZIONE
        hackathon.avanzaStato(); // CONCLUSO

        assertThrows(IllegalStateException.class, hackathon::avanzaStato);
    }

    @Test
    @DisplayName("Ciclo completo: IN_ISCRIZIONE → IN_CORSO → IN_VALUTAZIONE → CONCLUSO")
    void cicloCompleto() {
        Hackathon hackathon = new Hackathon();

        assertEquals("IN_ISCRIZIONE", hackathon.getStato().getNome());
        hackathon.avanzaStato();
        assertEquals("IN_CORSO", hackathon.getStato().getNome());
        hackathon.avanzaStato();
        assertEquals("IN_VALUTAZIONE", hackathon.getStato().getNome());
        hackathon.avanzaStato();
        assertEquals("CONCLUSO", hackathon.getStato().getNome());
    }

    @Test
    @DisplayName("Iscrizione team non possibile quando hackathon è IN_CORSO")
    void iscrizioneNonPermessaInCorso() {
        Hackathon hackathon = new Hackathon();
        hackathon.avanzaStato(); // IN_CORSO

        assertFalse(hackathon.puoIscrivere());
    }
}
