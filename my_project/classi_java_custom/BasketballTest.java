package classi_java_custom;

import java.time.LocalDate;

// CLASSE DI TEST
public class BasketballTest {
    public static void main(String[] args) {
        AdvancedBasketballStats stats = new AdvancedBasketballStats();
        
        // Test efficienza
        System.out.println("Efficienza avanzata: " + 
            stats.calcolaEfficienzaGiocatore(25, 10, 8, 3, 12));
        
        // Test valutazione tiro
        System.out.println("Valutazione tiro: " + 
            stats.valutaTiro(7.2, 8));
        
        // Test analisi statistica
        System.out.println("\nAnalisi squadra:\n" + 
            stats.analisiStatisticheSquadra(95, 80, 12, 28));
        
        // Test previsione
        System.out.println("\nPrevisione:\n" + 
            stats.previsioneVincitore(
                new double[]{88.5, 42.1, 23.8}, 
                new double[]{85.2, 38.7, 25.4}, 
                LocalDate.of(2024, 3, 15)));
        
        // Test nuovo metodo
        System.out.println("\nAndamento:\n" + 
            stats.analisiAndamento(new int[]{92, 85, 103, 88}));
    }
}