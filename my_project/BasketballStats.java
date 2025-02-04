import java.util.Arrays;
import java.time.LocalDate;

public class BasketballStats {
    
    /**
     * Calcola l'efficienza di un giocatore usando la formula PER (Player Efficiency Rating).
     * Formula semplificata: (punti + rimbalzi + assist) - (palle perse + tiri sbagliati)
     * 
     * @param punti Punti segnati
     * @param rimbalzi Rimbalzi presi
     * @param assist Assist effettuati
     * @param pallePerse Palle perse
     * @param tiriSbagliati Tiri sbagliati
     * @return Il rating di efficienza del giocatore
     */
    public static double calcolaEfficienzaGiocatore(int punti, int rimbalzi, int assist, 
                                                   int pallePerse, int tiriSbagliati) {
        return (punti + rimbalzi + assist) - (pallePerse + tiriSbagliati);
    }

    /**
     * Determina la valutazione di un tiro in base alla distanza e alla pressione difensiva.
     * 
     * @param distanzaCanestro Distanza dal canestro in metri
     * @param pressioneDifensiva Valore da 1 a 10 che indica la pressione difensiva
     * @return Un messaggio che valuta la qualità del tiro
     */
    public static String valutaTiro(double distanzaCanestro, int pressioneDifensiva) {
        if (distanzaCanestro < 1.5) {
            return "Tiro facile sotto canestro";
        } else if (distanzaCanestro < 6.75 && pressioneDifensiva < 7) {
            return "Buon tiro da due punti";
        } else if (distanzaCanestro >= 6.75 && pressioneDifensiva > 8) {
            return "Tiro da tre difficile sotto pressione";
        } else {
            return "Tiro da tre con buon margine";
        }
    }

    /**
     * Analizza le statistiche di una squadra in una partita.
     * 
     * @param puntiSegnati Punti segnati dalla squadra
     * @param tiriTentati Tiri tentati totali
     * @param rimbalziOffensivi Rimbalzi offensivi presi
     * @param rimbalziDifensivi Rimbalzi difensivi presi
     * @return Un report dettagliato delle prestazioni
     */
    public static String analisiStatisticheSquadra(int puntiSegnati, int tiriTentati, 
                                                  int rimbalziOffensivi, int rimbalziDifensivi) {
        double percentualeRealizzazione = (double) puntiSegnati / (tiriTentati * 2) * 100;
        int rimbalziTotali = rimbalziOffensivi + rimbalziDifensivi;
        
        StringBuilder report = new StringBuilder();
        report.append("Report Statistiche Squadra:\n");
        report.append("- Punti Segnati: ").append(puntiSegnati).append("\n");
        report.append("- Percentuale Realizzazione: ").append(String.format("%.1f%%", percentualeRealizzazione)).append("\n");
        report.append("- Rimbalzi Totali: ").append(rimbalziTotali).append("\n");
        report.append("- Ratio Rimbalzi Off/Dif: ").append(String.format("%.2f", (double)rimbalziOffensivi/rimbalziDifensivi));
        
        return report.toString();
    }

    /**
     * Prevede il vincitore di una partita basandosi sulle statistiche precedenti.
     * 
     * @param squadraCasa Array con [puntiMedi, rimbalziMedi, assistMedi]
     * @param squadraOspite Array con [puntiMedi, rimbalziMedi, assistMedi]
     * @param dataPartita Data della partita
     * @return Previsione del vincitore con margine
     */
    public static String previsioneVincitore(double[] squadraCasa, double[] squadraOspite, LocalDate dataPartita) {
        double valutazioneCasa = squadraCasa[0] * 0.4 + squadraCasa[1] * 0.3 + squadraCasa[2] * 0.3;
        double valutazioneOspite = squadraOspite[0] * 0.4 + squadraOspite[1] * 0.3 + squadraOspite[2] * 0.3;
        
        double margine = Math.abs(valutazioneCasa - valutazioneOspite);
        
        if (valutazioneCasa > valutazioneOspite) {
            return String.format("Vittoria casa prevista con margine di %.1f punti", margine);
        } else if (valutazioneOspite > valutazioneCasa) {
            return String.format("Vittoria ospite prevista con margine di %.1f punti", margine);
        } else {
            return "Partita equilibrata, impossibile fare previsioni";
        }
    }

    /**
     * Calcola il "momentum" della partita basato sugli ultimi eventi.
     * 
     * @param ultimiPuntiCasa Ultimi punti segnati dalla squadra di casa
     * @param ultimiPuntiOspite Ultimi punti segnati dalla squadra ospite
     * @return Descrizione del momentum della partita
     */
    public static String calcolaMomentum(int[] ultimiPuntiCasa, int[] ultimiPuntiOspite) {
        int sommaCasa = Arrays.stream(ultimiPuntiCasa).sum();
        int sommaOspite = Arrays.stream(ultimiPuntiOspite).sum();
        
        if (sommaCasa > sommaOspite + 10) {
            return "Momentum fortemente a favore della squadra di casa!";
        } else if (sommaOspite > sommaCasa + 10) {
            return "Momentum fortemente a favore della squadra ospite!";
        } else if (Math.abs(sommaCasa - sommaOspite) <= 5) {
            return "Partita in equilibrio, momentum neutro";
        } else {
            return "Leggero momentum a favore della squadra " + 
                   (sommaCasa > sommaOspite ? "di casa" : "ospite");
        }
    }
}