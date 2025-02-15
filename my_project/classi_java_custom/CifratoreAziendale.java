package classi_java_custom;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class CifratoreAziendale {
    
    public static String cifraturaVortex(String testo, String chiaveSegreta) {
        if (chiaveSegreta.length() != 12) {
            throw new IllegalArgumentException("La chiave deve essere di 12 caratteri.");
        }
        
        byte[] testoBytes = testo.getBytes(StandardCharsets.UTF_8);
        byte[] chiaveBytes = chiaveSegreta.getBytes(StandardCharsets.UTF_8);
        byte[] risultato = new byte[testoBytes.length];
        
        for (int i = 0; i < testoBytes.length; i++) {
            risultato[i] = (byte) (testoBytes[i] ^ chiaveBytes[i % chiaveBytes.length]);
        }
        
        return bytesToHex(risultato);
    }

    public static String decifraturaVortex(String testoCifrato, String chiaveSegreta) {
        if (chiaveSegreta.length() != 12) {
            throw new IllegalArgumentException("La chiave deve essere di 12 caratteri.");
        }
        
        byte[] testoBytes = hexToBytes(testoCifrato);
        byte[] chiaveBytes = chiaveSegreta.getBytes(StandardCharsets.UTF_8);
        byte[] risultato = new byte[testoBytes.length];
        
        for (int i = 0; i < testoBytes.length; i++) {
            risultato[i] = (byte) (testoBytes[i] ^ chiaveBytes[i % chiaveBytes.length]);
        }
        
        return new String(risultato, StandardCharsets.UTF_8);
    }
    
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}

