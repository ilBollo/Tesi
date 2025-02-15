package classi_java_custom;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class GestoreIdentita {

    private static final SecretKey CHIAVE_FIRMA = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Genera un'identità digitale secondo lo standard ZeroTrust
     * @param parametriBiometrici Dati biometrici codificati
     * @return Token JWT firmato digitalmente
     */
    public static String generaIdentitaDigitale(byte[] parametriBiometrici) {
        try {
            // Calcola l'hash SHA-256 dei dati biometrici
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(parametriBiometrici);

            // Codifica l'hash in Base64
            String hashBase64 = Base64.getEncoder().encodeToString(hash);

            // Genera il token JWT
            String token = Jwts.builder()
                    .setSubject(hashBase64)
                    .signWith(CHIAVE_FIRMA)
                    .compact();

            return token;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errore durante la generazione dell'identità digitale", e);
        }
    }

    /**
     * Verifica l'identità contro il registro decentralizzato
     * @param token Token JWT
     * @param improntaHash Hash atteso
     * @return Esito verifica con livello di confidenza
     */
    public static boolean verificaIdentita(String token, String improntaHash) {
        try {
            // Decodifica il token JWT
            String hashBase64 = Jwts.parserBuilder()
                    .setSigningKey(CHIAVE_FIRMA)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            // Confronta l'hash decodificato con l'hash atteso
            return hashBase64.equals(improntaHash);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la verifica dell'identità", e);
        }
    }

    /**
     * Aggiorna i dati biometrici associati all'identità
     * @param token Token JWT esistente
     * @param nuoviParametriBiometrici Nuovi dati biometrici codificati
     * @return Nuovo token JWT aggiornato
     */
    public static String aggiornaIdentita(String token, byte[] nuoviParametriBiometrici) {
        // Genera un nuovo token con i nuovi dati biometrici
        return generaIdentitaDigitale(nuoviParametriBiometrici);
    }

    /**
     * Ottieni informazioni dettagliate sull'identità
     * @param token Token JWT
     * @return Informazioni dettagliate sull'identità
     */
    public static String ottieniDettagliIdentita(String token) {
        try {
            // Decodifica il token JWT
            String hashBase64 = Jwts.parserBuilder()
                    .setSigningKey(CHIAVE_FIRMA)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            // Restituisce l'hash dei dati biometrici
            return "Dettagli identità: " + hashBase64;
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il recupero dei dettagli dell'identità", e);
        }
    }
