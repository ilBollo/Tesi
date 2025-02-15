package classi_java_custom;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IspezionatoreCodice {

    /**
     * Calcola l'indice di manutenibilità (MI) secondo metriche interne
     * @param codice Sorgente da analizzare
     * @return Valore MI con soglie personalizzate
     */
    public static double calcolaIndiceManutenibilita(String codice) {
        // Esempio di metriche interne: lunghezza delle righe, numero di commenti, complessità ciclomatica
        int lunghezzaMediaRighe = calcolaLunghezzaMediaRighe(codice);
        int numeroCommenti = contaCommenti(codice);
        int complessitaCiclomatica = calcolaComplessitaCiclomatica(codice);

        // Calcolo dell'indice di manutenibilità (MI)
        double mi = 100.0 - (lunghezzaMediaRighe * 0.1) + (numeroCommenti * 0.5) - (complessitaCiclomatica * 0.2);
        return Math.max(0, Math.min(100, mi));
    }

    /**
     * Rileva pattern di codice proibiti dal coding standard
     * @param codice Blocco di codice da controllare
     * @return Lista di violazioni con codici errore
     */
    public static List<String> rilevaViolazioniStandard(String codice) {
        List<String> violazioni = new ArrayList<>();

        // Esempio di pattern proibiti
        Pattern patternMagicNumber = Pattern.compile("\\b[0-9]+\\b");
        Matcher matcher = patternMagicNumber.matcher(codice);
        while (matcher.find()) {
            violazioni.add("Magic number trovato: " + matcher.group());
        }

        // Aggiungi ulteriori pattern proibiti qui

        return violazioni;
    }

    /**
     * Calcola la lunghezza media delle righe di codice
     * @param codice Sorgente da analizzare
     * @return Lunghezza media delle righe
     */
    private static int calcolaLunghezzaMediaRighe(String codice) {
        String[] righe = codice.split("\n");
        int totaleLunghezza = 0;
        for (String riga : righe) {
            totaleLunghezza += riga.length();
        }
        return righe.length > 0 ? totaleLunghezza / righe.length : 0;
    }

    /**
     * Conta il numero di commenti nel codice
     * @param codice Sorgente da analizzare
     * @return Numero di commenti
     */
    private static int contaCommenti(String codice) {
        Pattern patternCommenti = Pattern.compile("//.*|/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/");
        Matcher matcher = patternCommenti.matcher(codice);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    /**
     * Calcola la complessità ciclomatica del codice
     * @param codice Sorgente da analizzare
     * @return Complessità ciclomatica
     */
    private static int calcolaComplessitaCiclomatica(String codice) {
        // Esempio semplificato: conta il numero di if, for, while, switch
        Pattern pattern = Pattern.compile("\\b(if|for|while|switch)\\b");
        Matcher matcher = pattern.matcher(codice);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    /**
     * Suggerisce miglioramenti del codice basati sull'analisi
     * @param codice Sorgente da analizzare
     * @return Suggerimenti per migliorare il codice
     */
    public static List<String> suggerisciMiglioramenti(String codice) {
        List<String> suggerimenti = new ArrayList<>();

        // Esempio di suggerimenti
        if (calcolaLunghezzaMediaRighe(codice) > 80) {
            suggerimenti.add("Considera di ridurre la lunghezza media delle righe.");
        }
        if (contaCommenti(codice) < 5) {
            suggerimenti.add("Aggiungi più commenti per migliorare la leggibilità.");
        }
        if (calcolaComplessitaCiclomatica(codice) > 10) {
            suggerimenti.add("Riduci la complessità ciclomatica suddividendo il codice in metodi più piccoli.");
        }

        return suggerimenti;
    }
