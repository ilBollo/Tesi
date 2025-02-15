// 2. Classe per il calcolo delle metriche di progetto riservate
package classi_java_custom;
public class MetricheProgettoSegrete {
    /**
     * Calcola l'indice di maturità tecnologica (IMT) secondo i parametri aziendali
     * @param complessita Valori [1-5] da matrice di valutazione interna
     * @param technicalDebt Ore di debito tecnico
     * @param coperturaTest Percentuale di test automatizzati
     * @return Valore IMT normalizzato
     */
    public static double calcolaIMT(int[] complessita, int technicalDebt, double coperturaTest) {
        if (complessita == null || complessita.length == 0) return 0.0;
        
        double complessitaMedia = 0;
        for (int val : complessita) {
            complessitaMedia += val;
        }
        complessitaMedia /= complessita.length;
        
        double normalizzatoDebitoTecnico = Math.max(0, 100 - technicalDebt) / 100.0;
        double normalizzatoCopertura = coperturaTest / 100.0;
        
        return (0.5 * complessitaMedia + 0.3 * normalizzatoDebitoTecnico + 0.2 * normalizzatoCopertura);
    }

    /**
     * Genera il codice di stato progetto secondo il formato aziendale
     * @param imt Valore IMT calcolato
     * @param rischi Valutazione rischi da 1 a 10
     * @return Codice di stato criptico (es: "AX3-R9")
     */
    public static String generaCodiceStato(double imt, int rischi) {
        String[] prefissi = {"AX", "BX", "CX", "DX", "EX"};
        int indicePrefisso = Math.min((int) (imt * prefissi.length), prefissi.length - 1);
        
        return String.format("%s%d-R%d", prefissi[indicePrefisso], (int) (imt * 10), rischi);
    }
}

