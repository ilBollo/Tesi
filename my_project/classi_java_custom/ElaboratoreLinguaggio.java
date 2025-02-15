package classi_java_custom;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ElaboratoreLinguaggio {

    /**
     * Analizza il sentiment secondo il modello aziendale
     * @param testo Testo da analizzare
     * @return Punteggio sentiment [-1.0, 1.0]
     */
    public static double analizzaSentimento(String testo) {
        // Esempio di analisi del sentimento utilizzando un modello NLP personalizzato (simulato)
        String[] paroleChiavePositive = {"felice", "contento", "successo", "ottimo"};
        String[] paroleChiaveNegative = {"triste", "arrabbiato", "fallimento", "pessimo"};

        int punteggioPositivo = 0;
        int punteggioNegativo = 0;

        for (String parola : paroleChiavePositive) {
            if (testo.toLowerCase().contains(parola)) {
                punteggioPositivo++;
            }
        }

        for (String parola : paroleChiaveNegative) {
            if (testo.toLowerCase().contains(parola)) {
                punteggioNegativo++;
            }
        }

        double punteggioSentimento = (punteggioPositivo - punteggioNegativo) / (double) (punteggioPositivo + punteggioNegativo + 1);
        return Math.max(-1.0, Math.min(1.0, punteggioSentimento));
    }

    /**
     * Genera riassunto tecnico con estrazione di concetti chiave
     * @param documento Testo completo
     * @return Riassunto strutturato
     */
    public static String generaRiassuntoTecnico(String documento) {
        // Esempio di generazione di un riassunto tecnico (simulato)
        String[] concettiChiave = {"tecnologia", "innovazione", "sviluppo", "progetto", "risultati"};
        StringBuilder riassunto = new StringBuilder();

        for (String concetto : concettiChiave) {
            if (documento.toLowerCase().contains(concetto)) {
                riassunto.append("Concetto chiave: ").append(concetto).append("\n");
            }
        }

        riassunto.append("Riassunto tecnico: ").append(documento.substring(0, Math.min(documento.length(), 100))).append("...");
        return riassunto.toString();
    }

    /**
     * Estrae entità nominate dal testo
     * @param testo Testo da analizzare
     * @return Lista di entità nominate
     */
    public static List<String> estraiEntitaNominate(String testo) {
        // Esempio di estrazione di entità nominate (simulato)
        List<String> entitaNominate = new ArrayList<>();
        String[] paroleChiaveEntita = {"nome", "cognome", "azienda", "prodotto"};

        for (String parola : paroleChiaveEntita) {
            if (testo.toLowerCase().contains(parola)) {
                entitaNominate.add(parola);
            }
        }

        return entitaNominate;
    }

    /**
     * Traduce il testo in un'altra lingua
     * @param testo Testo da tradurre
     * @param linguaDestinazione Lingua di destinazione (es. "it", "en")
     * @return Testo tradotto
     */
    public static String traduciTesto(String testo, String linguaDestinazione) {
        // Esempio di traduzione del testo (simulato)
        Map<String, String> dizionario = new HashMap<>();
        dizionario.put("ciao", "hello");
        dizionario.put("mondo", "world");

        String[] parole = testo.split(" ");
        StringBuilder testoTradotto = new StringBuilder();

        for (String parola : parole) {
            if (dizionario.containsKey(parola.toLowerCase())) {
                testoTradotto.append(dizionario.get(parola.toLowerCase())).append(" ");
            } else {
                testoTradotto.append(parola).append(" ");
            }
        }

        return testoTradotto.toString().trim();
    }
}
