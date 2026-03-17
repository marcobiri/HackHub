package unicam.hackhub.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Servizio esterno simulato per la gestione del calendario.
 * Utilizzato per pianificare call tra mentore e team.
 * In produzione, delegherebbe a un'API Calendar (Google Calendar, Outlook,
 * etc.).
 */
@Service
public class CalendarService {

    /**
     * Prenota uno slot per una call tra mentore e team.
     *
     * @param mentoreUsername username del mentore
     * @param teamNome        nome del team
     * @return ID dell'evento creato nel calendario
     */
    public String prenotaCall(String mentoreUsername, String teamNome) {
        // Simulazione: genera un ID evento fittizio
        String eventId = "CAL-" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("[Calendar Service] Call prenotata: " + mentoreUsername +
                " con team " + teamNome + " - Event ID: " + eventId);
        return eventId;
    }

    /**
     * Cancella un evento dal calendario.
     *
     * @param eventId ID dell'evento da cancellare
     */
    public void cancellaCall(String eventId) {
        System.out.println("[Calendar Service] Call cancellata: " + eventId);
    }
}
