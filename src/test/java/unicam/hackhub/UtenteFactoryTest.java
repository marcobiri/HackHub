package unicam.hackhub;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import unicam.hackhub.model.factory.UtenteFactory;
import unicam.hackhub.model.staff.*;
import unicam.hackhub.model.team.MembroTeam;
import unicam.hackhub.model.user.Utente;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;

/**
 * Test per il Design Pattern Factory — Creazione Utenti/Staff.
 * Verifica la creazione corretta dei diversi tipi di utente tramite
 * UtenteFactory.
 */
class UtenteFactoryTest {

    @Test
    @DisplayName("Creazione Organizzatore tramite factory")
    void creaOrganizzatore() {
        Organizzatore org = UtenteFactory.creaOrganizzatore("orgUser", "org@test.it", "pass123");

        assertNotNull(org);
        assertEquals("orgUser", org.getUsername());
        assertEquals("org@test.it", org.getEmail());
        assertEquals(RuoloStaff.ORGANIZZATORE, org.getRuolo());
        assertInstanceOf(Organizzatore.class, org);
    }

    @Test
    @DisplayName("Creazione Giudice tramite factory")
    void creaGiudice() {
        Giudice giudice = UtenteFactory.creaGiudice("giudiceUser", "giudice@test.it", "pass123");

        assertNotNull(giudice);
        assertEquals("giudiceUser", giudice.getUsername());
        assertEquals(RuoloStaff.GIUDICE, giudice.getRuolo());
        assertInstanceOf(Giudice.class, giudice);
    }

    @Test
    @DisplayName("Creazione Mentore tramite factory")
    void creaMentore() {
        Mentore mentore = UtenteFactory.creaMentore("mentoreUser", "mentore@test.it", "pass123");

        assertNotNull(mentore);
        assertEquals("mentoreUser", mentore.getUsername());
        assertEquals(RuoloStaff.MENTORE, mentore.getRuolo());
        assertInstanceOf(Mentore.class, mentore);
    }

    @Test
    @DisplayName("Creazione MembroTeam tramite factory")
    void creaMembroTeam() {
        MembroTeam membro = UtenteFactory.creaMembroTeam("membroUser", "membro@test.it", "pass123");

        assertNotNull(membro);
        assertEquals("membroUser", membro.getUsername());
        assertInstanceOf(MembroTeam.class, membro);
    }

    @Test
    @DisplayName("Creazione Utente base tramite factory")
    void creaUtente() {
        Utente utente = UtenteFactory.creaUtente("utenteUser", "utente@test.it", "pass123");

        assertNotNull(utente);
        assertEquals("utenteUser", utente.getUsername());
        assertInstanceOf(Utente.class, utente);
    }

    @Test
    @DisplayName("Creazione staff dinamica con RuoloStaff.ORGANIZZATORE")
    void creaStaffOrganizzatore() {
        MembroStaff staff = UtenteFactory.creaStaff(RuoloStaff.ORGANIZZATORE, "s1", "s1@test.it", "pass");

        assertInstanceOf(Organizzatore.class, staff);
        assertEquals(RuoloStaff.ORGANIZZATORE, staff.getRuolo());
    }

    @Test
    @DisplayName("Creazione staff dinamica con RuoloStaff.GIUDICE")
    void creaStaffGiudice() {
        MembroStaff staff = UtenteFactory.creaStaff(RuoloStaff.GIUDICE, "s2", "s2@test.it", "pass");

        assertInstanceOf(Giudice.class, staff);
        assertEquals(RuoloStaff.GIUDICE, staff.getRuolo());
    }

    @Test
    @DisplayName("Creazione staff dinamica con RuoloStaff.MENTORE")
    void creaStaffMentore() {
        MembroStaff staff = UtenteFactory.creaStaff(RuoloStaff.MENTORE, "s3", "s3@test.it", "pass");

        assertInstanceOf(Mentore.class, staff);
        assertEquals(RuoloStaff.MENTORE, staff.getRuolo());
    }

    @Test
    @DisplayName("Objects.requireNonNull lancia eccezione su utente null")
    void requireNonNullLanciaEccezioneSuNull() {
        assertThrows(NullPointerException.class, () -> Objects.requireNonNull(null));
    }

    @Test
    @DisplayName("La factory non restituisce mai un utente null")
    void factoryNonRestituisceNull() {
        Utente utente = UtenteFactory.creaUtente("test", "test@test.it", "pass");
        assertNotNull(Objects.requireNonNull(utente));
    }
}
