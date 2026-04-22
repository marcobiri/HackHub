package unicam.hackhub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.model.factory.UtenteFactory;
import unicam.hackhub.model.staff.MembroStaff;
import unicam.hackhub.model.staff.RuoloStaff;
import unicam.hackhub.model.user.Utente;
import unicam.hackhub.repository.UtenteRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Controller REST per la gestione dello staff.
 * Utilizza il Factory Pattern per creare utenti staff.
 */
@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final UtenteRepository utenteRepository;

    public StaffController(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    /**
     * POST /api/staff — crea un nuovo membro dello staff usando il Factory Pattern.
     * Body: username, email, password, ruolo (ORGANIZZATORE|GIUDICE|MENTORE)
     */
    @PostMapping
    public ResponseEntity<Utente> creaStaff(@RequestBody Map<String, String> body) {
        RuoloStaff ruolo = RuoloStaff.valueOf(body.get("ruolo").toUpperCase());

        MembroStaff staff = UtenteFactory.creaStaff(
                ruolo,
                body.get("username"),
                body.get("email"),
                body.get("password"));

        Utente saved = Objects.requireNonNull(utenteRepository.save((Utente) staff));
        return ResponseEntity.ok(saved);
    }

    /**
     * GET /api/staff — lista tutti i membri dello staff.
     */
    @GetMapping
    public ResponseEntity<List<Utente>> getTuttoStaff() {
        List<Utente> staff = utenteRepository.findAll().stream()
                .filter(u -> u instanceof MembroStaff)
                .toList();
        return ResponseEntity.ok(staff);
    }
}
