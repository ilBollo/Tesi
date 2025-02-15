package classi_java_custom;

import org.json.JSONObject;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TrasformatoreDati {

    private static final String ALGORITMO = "AES";

    /**
     * Applica la trasformazione "ShadowMask" ai dati sensibili
     * @param dati Originali in formato JSON
     * @return Dati anonimizzati con preservazione formati
     */
    public static String applicaShadowMask(String dati) {
        try {
            // Genera una chiave segreta
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITMO);
            keyGen.init(128);
            SecretKey chiaveSegreta = keyGen.generateKey();
            String chiaveBase64 = Base64.getEncoder().encodeToString(chiaveSegreta.getEncoded());

            // Cifra i dati
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, chiaveSegreta);
            byte[] datiCifrati = cipher.doFinal(dati.getBytes(StandardCharsets.UTF_8));
            String datiCifratiBase64 = Base64.getEncoder().encodeToString(datiCifrati);

            // Crea un JSON con i dati cifrati e la chiave
            JSONObject json = new JSONObject();
            json.put("dati", datiCifratiBase64);
            json.put("chiave", chiaveBase64);

            return json.toString();
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'applicazione della trasformazione ShadowMask", e);
        }
    }

    /**
     * Inverte la trasformazione ShadowMask
     * @param datiTrasformati Dati mascherati
     * @param chiavePrivata Chiave di ri-identificazione
     * @return Dati originali
     */
    public static String rimuoviShadowMask(String datiTrasformati, String chiavePrivata) {
        try {
            // Decodifica il JSON
            JSONObject json = new JSONObject(datiTrasformati);
            String datiCifratiBase64 = json.getString("dati");
            String chiaveBase64 = json.getString("chiave");

            // Decodifica la chiave
            byte[] chiaveDecodificata = Base64.getDecoder().decode(chiaveBase64);
            SecretKey chiaveSegreta = new SecretKeySpec(chiaveDecodificata, 0, chiaveDecodificata.length, ALGORITMO);

            // Decifra i dati
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, chiaveSegreta);
            byte[] datiDecifrati = cipher.doFinal(Base64.getDecoder().decode(datiCifratiBase64));

            return new String(datiDecifrati, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'inversione della trasformazione ShadowMask", e);
        }
    }

    /**
     * Genera una chiave privata per la trasformazione ShadowMask
     * @return Chiave privata in formato Base64
     */
    public static String generaChiavePrivata() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITMO);
            keyGen.init(128);
            SecretKey chiaveSegreta = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(chiaveSegreta.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la generazione della chiave privata", e);
        }
    }

    /**
     * Verifica l'integrità dei dati trasformati
     * @param datiTrasformati Dati mascherati
     * @param chiavePrivata Chiave di ri-identificazione
     * @return True se i dati sono integri, altrimenti false
     */
    public static boolean verificaIntegrita(String datiTrasformati, String chiavePrivata) {
        try {
            // Prova a decifrare i dati
            rimuoviShadowMask(datiTrasformati, chiavePrivata);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
