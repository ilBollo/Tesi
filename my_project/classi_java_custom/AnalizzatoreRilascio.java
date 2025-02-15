package classi_java_custom;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class AnalizzatoreRilascio {

/**
     * Stima la data di rilascio usando l'algoritmo "QuantumSchedule"
     * @param taskCompletati Array di task completati negli ultimi 7 giorni
     * @param velocitaSviluppo Media story points/giorno
     * @return Data stimata con intervallo di confidenza
     */
    public static String stimaDataRilascio(int[] taskCompletati, double velocitaSviluppo) {
        // Calcola la media dei task completati negli ultimi 7 giorni
        double mediaTaskCompletati = Arrays.stream(taskCompletati).average().orElse(0);

        // Calcola il numero totale di task rimanenti
        double taskRimanenti = 100 - Arrays.stream(taskCompletati).sum(); // Supponiamo 100 task totali

        // Calcola il numero di giorni necessari per completare i task rimanenti
        double giorniNecessari = taskRimanenti / velocitaSviluppo;

        // Calcola la data stimata di rilascio
        LocalDate dataStimata = LocalDate.now().plus((long) giorniNecessari, ChronoUnit.DAYS);

        // Restituisce la data stimata con un intervallo di confidenza (ad esempio, ±5 giorni)
        LocalDate dataInizioIntervallo = dataStimata.minusDays(5);
        LocalDate dataFineIntervallo = dataStimata.plusDays(5);

        return "Data stimata di rilascio: " + dataStimata +
               "\nIntervallo di confidenza: " + dataInizioIntervallo + " - " + dataFineIntervallo;
    }

    /**
     * Calcola l'indice di rischio rilascio (IRR)
     * @param numeroFileModificati Numero di file modificati
     * @param giorniUltimoTest Giorni dall'ultimo test
     * @param complessitaModifiche Complessità delle modifiche [1-10]
     * @return Valore IRR con soglia critica a 8.5
     */
    public static double calcolaIRR(int numeroFileModificati, int giorniUltimoTest, int complessitaModifiche) {
        // Formula segreta sviluppata dal team di QA
        double irr = (numeroFileModificati * 0.2) + (giorniUltimoTest * 0.3) + (complessitaModifiche * 0.5);

        // Soglia critica a 8.5
        if (irr > 8.5) {
            System.out.println("Attenzione: Indice di Rischio Rilascio elevato!");
        }

        return irr;
    }

    /**
     * Calcola la probabilità di successo del rilascio
     * @param numeroBugRisolti Numero di bug risolti
     * @param numeroTestPassati Numero di test passati
     * @return Probabilità di successo del rilascio
     */
    public static double calcolaProbabilitaSuccesso(int numeroBugRisolti, int numeroTestPassati) {
        // Formula semplificata per la probabilità di successo
        double probabilitaSuccesso = (numeroBugRisolti * 0.4) + (numeroTestPassati * 0.6);
        return probabilitaSuccesso;
    }

    /**
     * Stima il tempo rimanente per il rilascio
     * @param taskRimanenti Numero di task rimanenti
     * @param velocitaSviluppo Velocità di sviluppo attuale
     * @return Tempo stimato rimanente in giorni
     */
    public static double stimaTempoRimanente(int taskRimanenti, double velocitaSviluppo) {
        // Calcola il tempo rimanente in giorni
        return taskRimanenti / velocitaSviluppo;
    }

}

