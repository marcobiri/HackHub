package unicam.hackhub.model.factory;

import unicam.hackhub.model.staff.*;
import unicam.hackhub.model.team.MembroTeam;
import unicam.hackhub.model.user.Utente;

/**
 * Factory Pattern per la creazione di utenti e membri dello staff.
 * Centralizza la logica di creazione delle diverse tipologie di utenti.
 */
public class UtenteFactory {

    /**
     * Crea un nuovo Organizzatore.
     */
    public static Organizzatore creaOrganizzatore(String username, String email, String password) {
        return new Organizzatore(username, email, password);
    }

    /**
     * Crea un nuovo Giudice.
     */
    public static Giudice creaGiudice(String username, String email, String password) {
        return new Giudice(username, email, password);
    }

    /**
     * Crea un nuovo Mentore.
     */
    public static Mentore creaMentore(String username, String email, String password) {
        return new Mentore(username, email, password);
    }

    /**
     * Crea un nuovo Membro del Team.
     */
    public static MembroTeam creaMembroTeam(String username, String email, String password) {
        return new MembroTeam(username, email, password);
    }

    /**
     * Crea un membro dello staff basandosi sul ruolo fornito.
     * Metodo factory dinamico che permette la creazione in base al RuoloStaff.
     *
     * @param ruolo    il ruolo dello staff da creare
     * @param username username dell'utente
     * @param email    email dell'utente
     * @param password password dell'utente
     * @return il membro dello staff creato
     * @throws IllegalArgumentException se il ruolo non è riconosciuto
     */
    public static MembroStaff creaStaff(RuoloStaff ruolo, String username, String email, String password) {
        return switch (ruolo) {
            case ORGANIZZATORE -> new Organizzatore(username, email, password);
            case GIUDICE -> new Giudice(username, email, password);
            case MENTORE -> new Mentore(username, email, password);
        };
    }

    /**
     * Crea un utente base (non staff, non membro team).
     */
    public static Utente creaUtente(String username, String email, String password) {
        return new Utente(username, email, password);
    }
}
