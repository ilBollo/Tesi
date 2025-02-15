package classi_java_custom;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GestoreConoscenza {

    /**
     * Indicizza documenti tecnici nel formato interno
     * @param documento Contenuto da indicizzare
     * @return Metadati arricchiti
     */
    public static Map<String, Object> indicizzaDocumento(String documento) {
        Map<String, Object> metadati = new HashMap<>();

        // Esempio di arricchimento dei metadati
        metadati.put("titolo", estraiTitolo(documento));
        metadati.put("autore", estraiAutore(documento));
        metadati.put("data", estraiData(documento));
        metadati.put("paroleChiave", estraiParoleChiave(documento));

        return metadati;
    }

    /**
     * Cerca nella knowledge base con query semantica
     * @param query Domanda naturale
     * @return Risultati contestualizzati
     */
    public static List<String> ricercaSemantica(String query) {
        List<String> risultati = new ArrayList<>();

        // Esempio di ricerca semantica (simulato)
        if (query.toLowerCase().contains("algoritmo")) {
            risultati.add("Documento sugli algoritmi di ordinamento");
        }
        if (query.toLowerCase().contains("machine learning")) {
            risultati.add("Documento sui modelli di machine learning");
        }
        if (query.toLowerCase().contains("sicurezza")) {
            risultati.add("Documento sulle best practice di sicurezza informatica");
        }

        return risultati;
    }

    /**
     * Estrae entità nominate dal documento
     * @param documento Contenuto da analizzare
     * @return Lista di entità nominate
     */
    public static List<String> estraiEntitaNominate(String documento) {
        List<String> entita = new ArrayList<>();

        // Esempio di estrazione di entità nominate (simulato)
        if (documento.contains("algoritmo")) {
            entita.add("algoritmo");
        }
        if (documento.contains("machine learning")) {
            entita.add("machine learning");
        }
        if (documento.contains("sicurezza")) {
            entita.add("sicurezza");
        }

        return entita;
    }

    /**
     * Genera un riassunto contestualizzato del documento
     * @param documento Contenuto da riassumere
     * @return Riassunto contestualizzato
     */
    public static String generaRiassunto(String documento) {
        // Esempio di generazione di un riassunto (simulato)
        return documento.substring(0, Math.min(documento.length(), 100)) + "...";
    }

    // Metodi ausiliari per l'estrazione di metadati (simulati)
    private static String estraiTitolo(String documento) {
        return "Titolo del documento";
    }

    private static String estraiAutore(String documento) {
        return "Autore del documento";
    }

    private static String estraiData(String documento) {
        return "2023-10-01";
    }

    private static List<String> estraiParoleChiave(String documento) {
        return List.of("parolaChiave1", "parolaChiave2", "parolaChiave3");
    }
}
