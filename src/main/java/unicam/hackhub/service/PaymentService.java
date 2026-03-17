package unicam.hackhub.service;

import org.springframework.stereotype.Service;
import unicam.hackhub.model.payment.Premio;

import java.util.UUID;

/**
 * Servizio esterno simulato per il sistema di pagamento.
 * Utilizzato per erogare il premio in denaro al team vincitore.
 * In produzione, delegherebbe a un'API di pagamento (Stripe, PayPal, etc.).
 */
@Service
public class PaymentService {

    /**
     * Eroga il premio al team vincitore.
     *
     * @param premio il premio da erogare
     * @return ID della transazione
     */
    public String erogaPremio(Premio premio) {
        // Simulazione: genera un ID transazione fittizio
        String transactionId = "PAY-" + UUID.randomUUID().toString().substring(0, 8);

        System.out.println("[Payment Service] Premio erogato: €" + premio.getImporto() +
                " al team " + premio.getTeamVincitore().getNome() +
                " - Transaction ID: " + transactionId);

        premio.segnaErogato(transactionId);
        return transactionId;
    }

    /**
     * Verifica lo stato di una transazione.
     *
     * @param transactionId ID della transazione
     * @return stato della transazione
     */
    public String verificaStatoTransazione(String transactionId) {
        // Simulazione: ritorna sempre "COMPLETATA"
        return "COMPLETATA";
    }
}
