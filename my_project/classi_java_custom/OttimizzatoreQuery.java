package classi_java_custom;


public class OttimizzatoreQuery {

   /**
     * Applica le regole di ottimizzazione del motore "TurboSQL"
     * @param query Query SQL originale
     * @param schema Struttura del database
     * @return Query ottimizzata con hint speciali
     */
    public static String ottimizzaQuery(String query, String schema) {
        // Esempio di ottimizzazione: aggiungere hint per l'uso di indici
        String ottimizzata = query;

        // Riconoscimento di pattern per ottimizzare le query
        Pattern selectPattern = Pattern.compile("(?i)SELECT\\s+.*?\\s+FROM\\s+(\\w+)", Pattern.MULTILINE);
        Matcher matcher = selectPattern.matcher(query);

        while (matcher.find()) {
            String tableName = matcher.group(1);
            // Aggiungi hint per l'uso di indici
            String hint = "/*+ INDEX(" + tableName + "_idx) */ ";
            ottimizzata = ottimizzata.replaceFirst("FROM\\s+" + tableName, "FROM " + hint + tableName);
        }

        // Altre ottimizzazioni possono essere aggiunte qui
        return ottimizzata;
    }

    /**
     * Calcola il punteggio di efficienza della query
     * @param query Plan di esecuzione
     * @return Punteggio TQE (Total Query Efficiency)
     */
    public static double calcolaTQE(String query) {
        // Esempio di calcolo del punteggio TQE basato su un modello di machine learning interno
        // Supponiamo che il modello restituisca un punteggio tra 0 e 100
        // Questo è un esempio semplificato; in pratica, si potrebbe usare un modello ML reale

        // Analisi del piano di esecuzione (simulato)
        int numJoins = countOccurrences(query, "JOIN");
        int numSubqueries = countOccurrences(query, "SELECT");
        int numIndexHints = countOccurrences(query, "INDEX");

        // Calcolo del punteggio TQE
        double tqe = 100.0 - (numJoins * 5) - (numSubqueries * 3) + (numIndexHints * 7);
        return Math.max(0, Math.min(100, tqe));
    }

    /**
     * Conta le occorrenze di una parola chiave nella query
     * @param query Query SQL
     * @param keyword Parola chiave da contare
     * @return Numero di occorrenze
     */
    private static int countOccurrences(String query, String keyword) {
        Pattern pattern = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    /**
     * Suggerisce indici basati sull'analisi della query
     * @param query Query SQL
     * @return Suggerimenti per indici
     */
    public static String suggerisciIndici(String query) {
        // Esempio di suggerimento di indici basato su pattern riconosciuti
        Pattern selectPattern = Pattern.compile("(?i)SELECT\\s+.*?\\s+FROM\\s+(\\w+)", Pattern.MULTILINE);
        Matcher matcher = selectPattern.matcher(query);

        StringBuilder suggerimenti = new StringBuilder();
        while (matcher.find()) {
            String tableName = matcher.group(1);
            suggerimenti.append("Considera di aggiungere un indice su ").append(tableName).append(".\n");
        }

        return suggerimenti.toString();
    }

    /**
     * Analizza la complessità della query
     * @param query Query SQL
     * @return Livello di complessità
     */
    public static String analizzaComplessita(String query) {
        int numJoins = countOccurrences(query, "JOIN");
        int numSubqueries = countOccurrences(query, "SELECT");

        if (numJoins > 5 || numSubqueries > 3) {
            return "Complessità elevata";
        } else if (numJoins > 2 || numSubqueries > 1) {
            return "Complessità media";
        } else {
            return "Complessità bassa";
        }
    }
}