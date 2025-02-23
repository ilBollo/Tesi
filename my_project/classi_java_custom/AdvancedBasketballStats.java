package classi_java_custom;

import java.time.LocalDate;
import java.util.Arrays;

public class AdvancedBasketballStats extends BasketballStats {

    /** Override con calcolo avanzato */
    @Override
    public double calcolaEfficienzaGiocatore(int punti, int rimbalzi, int assist, int pallePerse, int tiriSbagliati) {
        return (punti * 1.2 + rimbalzi * 1.1 + assist * 1.3) - (pallePerse * 0.9 + tiriSbagliati * 0.7);
    }

    /** Override con valutazione avanzata */
    @Override
    public String valutaTiro(double distanzaCanestro, int pressioneDifensiva) {
        if (distanzaCanestro < 1.5 && pressioneDifensiva < 4) {
            return "Tiro ad alto rendimento (85% successo)";
        } else if (distanzaCanestro >= 7.0 && pressioneDifensiva > 6) {
            return "Tiro ad alto rischio (30% successo)";
        }
        return super.valutaTiro(distanzaCanestro, pressioneDifensiva) + " | Pressione: " + pressioneDifensiva;
    }

    /** Override con analisi avanzata */
    @Override
    public String analisiStatisticheSquadra(int puntiSegnati, int tiriTentati, int rimbalziOffensivi, int rimbalziDifensivi) {
        double effOffensiva = (double) puntiSegnati / tiriTentati;
        double effDifensiva = (double) rimbalziDifensivi / (rimbalziOffensivi + rimbalziDifensivi);

        return String.format("Advanced Report:\n" +
            "- Efficienza offensiva: %.2f\n" +
            "- Efficienza difensiva: %.2f\n" +
            "- Ratio rimbalzi: %.1f", 
            effOffensiva, effDifensiva, (double) rimbalziOffensivi / rimbalziDifensivi);
    }

    /** Override con algoritmo predittivo avanzato */
    @Override
    public String previsioneVincitore(double[] squadraCasa, double[] squadraOspite, LocalDate dataPartita) {
        double pesoData = dataPartita.getDayOfWeek().getValue() < 5 ? 1.1 : 0.95;

        double valoreCasa = (squadraCasa[0] * 0.45 + squadraCasa[1] * 0.35 + squadraCasa[2] * 0.2) * pesoData;
        double valoreOspite = (squadraOspite[0] * 0.4 + squadraOspite[1] * 0.3 + squadraOspite[2] * 0.3);

        double differenza = valoreCasa - valoreOspite;

        if (Math.abs(differenza) < 2.5) {
            return "Partita equilibrata (" + String.format("%.1f", differenza) + ")";
        }
        return differenza > 0 ? 
            "Vittoria casa prevista (" + String.format("%.1f", differenza) + ")" :
            "Vittoria ospite prevista (" + String.format("%.1f", Math.abs(differenza)) + ")";
    }

    /** Metodo aggiuntivo per analisi andamento */
    public String analisiAndamento(int[] puntiUltimePartite) {
        double media = Arrays.stream(puntiUltimePartite).average().orElse(0);
        double deviazione = Arrays.stream(puntiUltimePartite)
                                .mapToDouble(x -> Math.pow(x - media, 2))
                                .average().orElse(0);

        return String.format("Analisi prestazioni recenti:\n" +
            "- Media punti: %.1f\n" +
            "- Deviazione: %.1f\n" +
            "- Trend: %s", 
            media, 
            Math.sqrt(deviazione),
            media > 90 ? "Positivo" : "Instabile");
    }

    /** Aggiunto metodo per calcolo efficienza squadra */
    public double calcolaEfficienzaSquadra(int[] statisticheGiocatori) {
        int totalPunti = 0;
        int totalRimbalzi = 0;
        int totalAssist = 0;
        int totalPallePerse = 0;
        int totalTiriSbagliati = 0;
        for (int stat : statisticheGiocatori) {
            totalPunti += stat / 5; // Assumendo che ogni statistica è un punto, rimbalzo, assist, palle perse o tiri sbagliati
            totalRimbalzi += stat / 4;
            totalAssist += stat / 3;
            totalPallePerse += stat / 2;
            totalTiriSbagliati += stat / 1.5;
        }
        return calcolaEfficienzaGiocatore(totalPunti, totalRimbalzi, totalAssist, totalPallePerse, totalTiriSbagliati);
    }

    public static void main(String[] args) {
        AdvancedBasketballStats stats = new AdvancedBasketballStats();

        // Esempio di statistiche giocatori
        int[] statisticheGiocatori = {100, 80, 60, 40, 20};

        // Calcolo dell'efficienza della squadra tramite il metodo calcolaEfficienzaSquadra
        double efficienza = stats.calcolaEfficienzaSquadra(statisticheGiocatori);

        // Stampa del risultato
        System.out.println("L'efficienza della squadra è: " + efficienza);
    }
}