package unicam.hackhub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import unicam.hackhub.model.hackathon.Hackathon;
import unicam.hackhub.model.staff.Giudice;
import unicam.hackhub.model.staff.Mentore;
import unicam.hackhub.model.staff.Organizzatore;
import unicam.hackhub.model.submission.Valutazione;
import unicam.hackhub.model.team.Team;
import unicam.hackhub.model.team.MembroTeam;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la logica di business dell'Hackathon.
 * Verifica creazione, iscrizione team, e validazioni.
 */
class HandlerHackathonTest {

    private Hackathon hackathon;
    private Organizzatore organizzatore;
    private Giudice giudice;

    @BeforeEach
    void setUp() {
        organizzatore = new Organizzatore("org1", "org1@test.it", "pass");
        giudice = new Giudice("giudice1", "giudice1@test.it", "pass");

        hackathon = new Hackathon(
                "HackathonTest",
                "Regolamento di test",
                LocalDateTime.now().plusDays(7),
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(12),
                "Roma",
                5000.0,
                4,
                organizzatore,
                giudice);
    }

    @Test
    @DisplayName("Hackathon viene creato con tutti i dati corretti")
    void creazioneHackathon() {
        assertEquals("HackathonTest", hackathon.getNome());
        assertEquals("Regolamento di test", hackathon.getRegolamento());
        assertEquals("Roma", hackathon.getLuogo());
        assertEquals(5000.0, hackathon.getPremioDenaro());
        assertEquals(4, hackathon.getMaxDimensioneTeam());
        assertEquals("IN_ISCRIZIONE", hackathon.getStatoCorrente());
        assertEquals(organizzatore, hackathon.getOrganizzatore());
        assertEquals(giudice, hackathon.getGiudice());
    }

    @Test
    @DisplayName("Iscrizione team possibile in stato IN_ISCRIZIONE")
    void iscrizioneTeam() {
        MembroTeam creatore = new MembroTeam("user1", "user1@test.it", "pass");
        Team team = new Team("TeamAlpha", creatore);

        hackathon.aggiungiTeam(team);
        assertEquals(1, hackathon.getTeams().size());
        assertEquals("TeamAlpha", hackathon.getTeams().get(0).getNome());
    }

    @Test
    @DisplayName("Iscrizione team non possibile in stato IN_CORSO")
    void iscrizioneNonPermessa() {
        hackathon.avanzaStato(); // IN_CORSO

        MembroTeam creatore = new MembroTeam("user1", "user1@test.it", "pass");
        Team team = new Team("TeamAlpha", creatore);

        assertThrows(IllegalStateException.class, () -> hackathon.aggiungiTeam(team));
    }

    @Test
    @DisplayName("Aggiunta mentore all'hackathon")
    void aggiungiMentore() {
        Mentore mentore = new Mentore("ment1", "ment1@test.it", "pass");
        hackathon.aggiungiMentore(mentore);

        assertEquals(1, hackathon.getMentori().size());
        assertEquals("ment1", hackathon.getMentori().get(0).getUsername());
    }

    @Test
    @DisplayName("Valutazione con punteggio fuori range 0-10 lancia eccezione")
    void valutazionePunteggioNonValido() {
        assertThrows(IllegalArgumentException.class, () -> new Valutazione("Ottimo", 11.0, null, giudice));

        assertThrows(IllegalArgumentException.class, () -> new Valutazione("Pessimo", -1.0, null, giudice));
    }

    @Test
    @DisplayName("Valutazione con punteggio valido viene creata correttamente")
    void valutazioneValida() {
        Valutazione valutazione = new Valutazione("Buon lavoro", 8.5, null, giudice);

        assertEquals("Buon lavoro", valutazione.getGiudizio());
        assertEquals(8.5, valutazione.getPunteggio());
        assertEquals(giudice, valutazione.getGiudice());
    }
}
