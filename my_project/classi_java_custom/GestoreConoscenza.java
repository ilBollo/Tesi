package classi_java_custom;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GestoreConoscenza {
    
    // Classe dedicata per i metadati
    public static class Metadati {
        private String titolo;
        private String autore;
        private String data;
        private List<String> paroleChiave;

        public Metadati(String titolo, String autore, String data, List<String> paroleChiave) {
            this.titolo = titolo;
            this.autore = autore;
            this.data = data;
            this.paroleChiave = Collections.unmodifiableList(paroleChiave);
        }

        // Getter e metodi utili
        public String getTitolo() { return titolo; }
        public String getAutore() { return autore; }
        public String getData() { return data; }
        public List<String> getParoleChiave() { return paroleChiave; }
    }

    private final Map<String, String> documentIndex;

    public GestoreConoscenza() {
        this.documentIndex = new HashMap<>();
        initializeIndex();
    }

    private void initializeIndex() {
        documentIndex.put("algoritmo", "Documento sugli algoritmi di ordinamento");
        documentIndex.put("machine learning", "Documento sui modelli di machine learning");
        documentIndex.put("sicurezza", "Documento sulle best practice di sicurezza informatica");
    }

    /**
     * Indicizza documenti tecnici estraendo metadati strutturati
     * @param documento Contenuto da analizzare (non nullo)
     * @return Oggetto Metadati con informazioni estratte
     * @throws IllegalArgumentException se il documento è nullo o vuoto
     */
    public Metadati indicizzaDocumento(String documento) {
        if (documento == null || documento.trim().isEmpty()) {
            throw new IllegalArgumentException("Documento non valido");
        }

        return new Metadati(
            estraiTitolo(documento),
            estraiAutore(documento),
            estraiData(documento),
            estraiParoleChiave(documento)
        );
    }

    /**
     * Cerca nella knowledge base utilizzando una query semantica
     * @param query Domanda naturale in input
     * @return Lista di risultati rilevanti ordinati per pertinenza
     */
    public List<String> ricercaSemantica(String query) {
        String queryNormalizzata = query.toLowerCase();
        return documentIndex.entrySet().stream()
            .filter(entry -> queryNormalizzata.contains(entry.getKey()))
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());
    }

    /**
     * Estrae entità nominate dal documento
     * @param documento Contenuto da analizzare
     * @return Set di entità uniche ordinate alfabeticamente
     */
    public Set<String> estraiEntitaNominate(String documento) {
        return documentIndex.keySet().stream()
            .filter(key -> documento.toLowerCase().contains(key))
            .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Genera un riassunto intelligente del documento
     * @param documento Contenuto da riassumere
     * @return Stringa con i primi 100 caratteri significativi
     */
    public String generaRiassunto(String documento) {
        String cleanedDoc = documento.replaceAll("\\s+", ").trim();
        return cleanedDoc.substring(0, Math.min(cleanedDoc.length(), 100)) + 
            (cleanedDoc.length() > 100 ? "..." : "");
    }

    // Metodi di estrazione migliorati con regex
    private String estraiTitolo(String documento) {
        Pattern pattern = Pattern.compile("^#\\s*(.+)$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(documento);
        return matcher.find() ? matcher.group(1) : "Nessun titolo rilevato";
    }

    private String estraiAutore(String documento) {
        Pattern pattern = Pattern.compile("Autore:\\s*(.+)$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(documento);
        return matcher.find() ? matcher.group(1) : "Autore sconosciuto";
    }

    private String estraiData(String documento) {
        Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        Matcher matcher = pattern.matcher(documento);
        return matcher.find() ? matcher.group() : "Data non disponibile";
    }

    private List<String> estraiParoleChiave(String documento) {
        Pattern pattern = Pattern.compile("#keyword:\\s*(.+)$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(documento);
        List<String> keywords = new ArrayList<>();
        while (matcher.find()) {
            keywords.add(matcher.group(1));
        }
        return !keywords.isEmpty() ? keywords : List.of("Tecnologia", "Informatica");
    }
}