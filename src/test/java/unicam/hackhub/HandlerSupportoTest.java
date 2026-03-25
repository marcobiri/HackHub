package unicam.hackhub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import unicam.hackhub.model.hackathon.Hackathon;
import unicam.hackhub.model.staff.Mentore;
import unicam.hackhub.model.staff.Organizzatore;
import unicam.hackhub.model.staff.Giudice;
import unicam.hackhub.model.support.SegnalazioneViolazione;
import unicam.hackhub.model.team.MembroTeam;
import unicam.hackhub.model.team.Team;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la gestione delle segnalazioni di violazione.
 * Verifica le transizioni di stato della SegnalazioneViolazione
 * gestite dall'Organizzatore.
 */
class HandlerSupportoTest {

    private SegnalazioneViolazione segnalazione;

    @BeforeEach
    void setUp() {
        Organizzatore organizzatore = new Organizzatore("org1", "org1@test.it", "pass");
        Giudice giudice = new Giudice("giudice1", "giudice1@test.it", "pass");
        Mentore mentore = new Mentore("ment1", "ment1@test.it", "pass");

        MembroTeam creatore = new MembroTeam("user1", "user1@test.it", "pass");
        Team team = new Team("TeamAlpha", creatore);

        Hackathon hackathon = new Hackathon(
                "HackTest", "Regolamento",
                LocalDateTime.now().plusDays(7),
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(12),
                "Roma", 5000.0, 4,
                organizzatore, giudice);

        segnalazione = new SegnalazioneViolazione("Team ha copiato codice", team, mentore, hackathon);
    }

    @Test
    @DisplayName("Segnalazione creata con stato iniziale APERTA")
    void statoInizialeAperta() {
        assertEquals(SegnalazioneViolazione.StatoSegnalazione.APERTA, segnalazione.getStato());
    }

    @Test
    @DisplayName("Transizione APERTA → IN_REVISIONE")
    void prendiInCarico() {
        segnalazione.prendiInCarico();
        assertEquals(SegnalazioneViolazione.StatoSegnalazione.IN_REVISIONE, segnalazione.getStato());
    }

    @Test
    @DisplayName("Transizione IN_REVISIONE → CONFERMATA")
    void conferma() {
        segnalazione.prendiInCarico();
        segnalazione.conferma();
        assertEquals(SegnalazioneViolazione.StatoSegnalazione.CONFERMATA, segnalazione.getStato());
    }

    @Test
    @DisplayName("Transizione IN_REVISIONE → RIGETTATA")
    void rigetta() {
        segnalazione.prendiInCarico();
        segnalazione.rigetta();
        assertEquals(SegnalazioneViolazione.StatoSegnalazione.RIGETTATA, segnalazione.getStato());
    }

    @Test
    @DisplayName("Conferma da stato APERTA deve lanciare eccezione")
    void confermaDaApertaDeveFallire() {
        assertThrows(IllegalStateException.class, () -> segnalazione.conferma());
    }

    @Test
    @DisplayName("Presa in carico da stato IN_REVISIONE deve lanciare eccezione")
    void prendiInCaricoDaInRevisioneDeveFallire() {
        segnalazione.prendiInCarico();
        assertThrows(IllegalStateException.class, () -> segnalazione.prendiInCarico());
    }
}
