package unicam.hackhub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.model.factory.UtenteFactory;
import unicam.hackhub.model.user.Utente;
import unicam.hackhub.repository.UtenteRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Controller REST per la gestione degli utenti.
 */
@RestController
@RequestMapping("/api/utente")
public class UtenteController {

    private final UtenteRepository utenteRepository;

    public UtenteController(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    /**
     * GET /api/utente — lista tutti gli utenti.
     */
    @GetMapping
    public ResponseEntity<List<Utente>> getTuttiUtenti() {
        return ResponseEntity.ok(utenteRepository.findAll());
    }

    /**
     * GET /api/utente/{id} — dettaglio utente.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Utente> getUtenteById(@PathVariable Long id) {
        Utente utente = utenteRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con ID: " + id));
        return ResponseEntity.ok(utente);
    }

    /**
     * POST /api/utente — registra un nuovo utente.
     * Body: username, email, password
     */
    @PostMapping
    public ResponseEntity<Utente> registraUtente(@RequestBody Map<String, String> body) {
        if (utenteRepository.existsByUsername(body.get("username"))) {
            throw new IllegalArgumentException("Username già in uso: " + body.get("username"));
        }
        if (utenteRepository.existsByEmail(body.get("email"))) {
            throw new IllegalArgumentException("Email già in uso: " + body.get("email"));
        }

        Utente utente = UtenteFactory.creaUtente(
                body.get("username"),
                body.get("email"),
                body.get("password"));
        return ResponseEntity.ok(Objects.requireNonNull(utenteRepository.save(utente)));
    }

    /**
     * GET /api/utente/cerca?username=... — cerca utente per username.
     */
    @GetMapping("/cerca")
    public ResponseEntity<Utente> cercaPerUsername(@RequestParam String username) {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato: " + username));
        return ResponseEntity.ok(utente);
    }

    /**
     * POST /api/utente/login — effettua l'accesso alla piattaforma.
     * Body: username, password
     */
    @PostMapping("/login")
    public ResponseEntity<Utente> login(@RequestBody Map<String, String> body) {
        Utente utente = utenteRepository.findByUsername(body.get("username"))
                .orElseThrow(() -> new IllegalArgumentException("Username non trovato: " + body.get("username")));

        if (!utente.getPassword().equals(body.get("password"))) {
            throw new IllegalArgumentException("Password non corretta.");
        }

        return ResponseEntity.ok(utente);
    }
}
