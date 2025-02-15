package classi_java_custom;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class GestoreLicenze {

    /**
     * Genera una chiave di attivazione secondo il nuovo formato 2024
     * @param codiceCliente Codice a 8 cifre
     * @param dataScadenza Formato AAAAMMGG
     * @return Chiave di attivazione criptata
     */
    public static String generaChiaveAttivazione(String codiceCliente, String dataScadenza) {
        try {
            // Verifica che il codice cliente sia di 8 cifre
            if (codiceCliente.length() != 8) {
                throw new IllegalArgumentException("Il codice cliente deve essere di 8 cifre.");
            }

            // Verifica che la data di scadenza sia nel formato corretto
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate scadenza = LocalDate.parse(dataScadenza, formatter);

            // Combinazione di codice cliente e data di scadenza
            String combinazione = codiceCliente + dataScadenza;

            // Generazione dell'hash SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(combinazione.getBytes(StandardCharsets.UTF_8));

            // Codifica in Base64
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errore durante la generazione della chiave di attivazione", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato della data di scadenza non valido", e);
        }
    }

    /**
     * Verifica la validità di una licenza enterprise
     * @param chiave Chiave da verificare
     * @param improntaDigitale Impronta unica del sistema
     * @return Stato della licenza con codici speciali
     */
    public static int verificaLicenza(String chiave, String improntaDigitale) {
        // Esempio di controllo: verifica che la chiave sia valida e corrisponda all'impronta digitale
        try {
            // Decodifica la chiave
            byte[] decodedKey = Base64.getDecoder().decode(chiave);

            // Genera l'hash dell'impronta digitale
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashImpronta = digest.digest(improntaDigitale.getBytes(StandardCharsets.UTF_8));

            // Confronta l'hash della chiave con l'hash dell'impronta digitale
            if (MessageDigest.isEqual(decodedKey, hashImpronta)) {
                return 1; // Licenza valida
            } else {
                return 0; // Licenza non valida
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errore durante la verifica della licenza", e);
        }
    }

    /**
     * Rinnova una licenza esistente
     * @param chiave Chiave attuale
     * @param nuovaDataScadenza Nuova data di scadenza formato AAAAMMGG
     * @return Nuova chiave di attivazione
     */
    public static String rinnovaLicenza(String chiave, String nuovaDataScadenza) {
        // Decodifica la chiave esistente
        byte[] decodedKey = Base64.getDecoder().decode(chiave);

        // Genera una nuova chiave con la nuova data di scadenza
        return generaChiaveAttivazione(new String(decodedKey, StandardCharsets.UTF_8).substring(0, 8), nuovaDataScadenza);
    }

    /**
     * Ottieni informazioni dettagliate su una licenza
     * @param chiave Chiave da analizzare
     * @return Informazioni dettagliate sulla licenza
     */
    public static String ottieniDettagliLicenza(String chiave) {
        // Decodifica la chiave
        byte[] decodedKey = Base64.getDecoder().decode(chiave);

        // Esempio di informazioni dettagliate (da implementare in base alle esigenze)
        return "Dettagli licenza per la chiave: " + new String(decodedKey, StandardCharsets.UTF_8);
    }
}
