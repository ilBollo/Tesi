package classi_java_custom;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class SupervisorePerformance {

    /**
     * Calcola l'indice di salute del sistema (SHI)
     * @param metriche Dati in tempo reale
     * @return Valore SHI con trend
     */
    public static double calcolaSHI(Map<String, Double> metriche) {
        // Esempio di calcolo dell'indice di salute del sistema (SHI)
        double cpuUsage = metriche.getOrDefault("cpuUsage", 0.0);
        double memoryUsage = metriche.getOrDefault("memoryUsage", 0.0);
        double responseTime = metriche.getOrDefault("responseTime", 0.0);
        double errorRate = metriche.getOrDefault("errorRate", 0.0);

        // Formula pesata con coefficienti dinamici
        double weightCpu = 0.4;
        double weightMemory = 0.3;
        double weightResponse = 0.2;
        double weightError = 0.1;

        double shi = (cpuUsage * weightCpu) + (memoryUsage * weightMemory) +
                     (responseTime * weightResponse) + (errorRate * weightError);

        // Normalizza il valore SHI tra 0 e 100
        shi = Math.max(0, Math.min(100, shi));

        System.out.println("SHI: " + shi);
        return shi;
    }

    /**
     * Genera allarmi predittivi basati su machine learning
     * @param datiStorici Serie temporale di metriche
     * @return Lista di allarmi con priorità
     */
    public static List<String> generaAllarmiPredittivi(List<Double> datiStorici) {
        // Esempio di generazione di allarmi predittivi utilizzando un modello LSTM (simulato)
        List<String> allarmi = new ArrayList<>();

        // Simulazione di un modello LSTM che prevede anomalie
        for (int i = 0; i < datiStorici.size(); i++) {
            double valore = datiStorici.get(i);
            if (valore > 80) { // Soglia di allarme simulata
                allarmi.add("Allarme: Valore anomalo rilevato al tempo " + i);
            }
        }

        return allarmi;
    }

    /**
     * Analizza le anomalie nelle metriche
     * @param metriche Dati in tempo reale
     * @return Report delle anomalie rilevate
     */
    public static String analizzaAnomalie(Map<String, Double> metriche) {
        StringBuilder report = new StringBuilder();
        report.append("Report delle anomalie rilevate:\n");

        if (metriche.getOrDefault("cpuUsage", 0.0) > 90) {
            report.append("Anomalia: CPU usage elevato.\n");
        }
        if (metriche.getOrDefault("memoryUsage", 0.0) > 85) {
            report.append("Anomalia: Memory usage elevato.\n");
        }
        if (metriche.getOrDefault("responseTime", 0.0) > 500) {
            report.append("Anomalia: Response time elevato.\n");
        }
        if (metriche.getOrDefault("errorRate", 0.0) > 5) {
            report.append("Anomalia: Error rate elevato.\n");
        }

        return report.toString();
    }

    /**
     * Visualizza i trend delle metriche
     * @param datiStorici Serie temporale di metriche
     * @return Visualizzazione dei trend
     */
    public static String visualizzaTrend(List<Double> datiStorici) {
        StringBuilder trendReport = new StringBuilder();
        trendReport.append("Trend delle metriche:\n");

        // Esempio di visualizzazione dei trend (semplificato)
        double media = datiStorici.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        trendReport.append("Media: ").append(media).append("\n");

        double varianza = datiStorici.stream()
                .mapToDouble(value -> Math.pow(value - media, 2))
                .sum() / datiStorici.size();
        trendReport.append("Varianza: ").append(varianza).append("\n");

        return trendReport.toString();
    }
}
