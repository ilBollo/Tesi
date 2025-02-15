package classi_java_custom;

import java.time.LocalDate;
import java.util.Arrays;

// CLASSE ASTRATTA BASE
public abstract class BasketballStats {
    
    // Metodo astratto per l'efficienza (da implementare nelle sottoclassi)
    public abstract double calcolaEfficienzaGiocatore(int punti, int rimbalzi, int assist, 
                                                    int pallePerse, int tiriSbagliati);
    
    // Metodo con implementazione base per la valutazione del tiro
    public String valutaTiro(double distanzaCanestro, int pressioneDifensiva) {
        return "Valutazione base: " + (distanzaCanestro < 6.75 ? "Tiro da 2 punti" : "Tiro da 3 punti");
    }
    
    // Metodo con implementazione base per l'analisi delle statistiche
    public String analisiStatisticheSquadra(int puntiSegnati, int tiriTentati, 
                                          int rimbalziOffensivi, int rimbalziDifensivi) {
        double percentuale = (double) puntiSegnati / tiriTentati * 100;
        return String.format("Base: %.1f%% successo, %d rimbalzi", percentuale, 
                           rimbalziOffensivi + rimbalziDifensivi);
    }
    
    // Metodo astratto per la previsione (da implementare nelle sottoclassi)
    public abstract String previsioneVincitore(double[] squadraCasa, double[] squadraOspite, 
                                             LocalDate dataPartita);
    
    // Metodo con implementazione base per il momentum
    public String calcolaMomentum(int[] ultimiPuntiCasa, int[] ultimiPuntiOspite) {
        int differenza = Arrays.stream(ultimiPuntiCasa).sum() - Arrays.stream(ultimiPuntiOspite).sum();
        return differenza > 0 ? "Momentum casa" : "Momentup ospite";
    }
}