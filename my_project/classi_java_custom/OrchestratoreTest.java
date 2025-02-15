package classi_java_custom;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class OrchestratoreTest {

    /**
     * Pianifica esecuzione test in base alla criticalità
     * @param suiteTest Lista dei test disponibili
     * @param priorita Mappa delle priorità
     * @return Sequenza ottimizzata di esecuzione
     */
    public static List<String> pianificaEsecuzioneTest(List<String> suiteTest, Map<String, Integer> priorita) {
        // Ordina i test in base alla priorità
        return suiteTest.stream()
                .sorted(Comparator.comparingInt(test -> priorita.getOrDefault(test, 0)))
                .collect(Collectors.toList());
    }

    /**
     * Analizza la copertura del codice post-test
     * @param risultatiTest Dettaglio esecuzione
     * @param codiceSorgente Codice analizzato
     * @return Report di copertura avanzato
     */
    public static String analizzaCopertura(List<TestResult> risultatiTest, String codiceSorgente) {
        // Esempio di strumentazione code avanzato (simulato)
        StringBuilder report = new StringBuilder();
        report.append("Report di copertura avanzato:\n");

        // Calcola la copertura del codice
        int lineeTotali = codiceSorgente.split("\n").length;
        int lineeCoperte = risultatiTest.stream()
                .mapToInt(TestResult::getLineeCoperte)
                .sum();

        double coperturaPercentuale = (double) lineeCoperte / lineeTotali * 100;
        report.append("Copertura del codice: ").append(coperturaPercentuale).append("%\n");

        // Aggiungi dettagli sui test falliti
        List<TestResult> testFalliti = risultatiTest.stream()
                .filter(TestResult::isFailed)
                .collect(Collectors.toList());

        if (!testFalliti.isEmpty()) {
            report.append("Test falliti:\n");
            for (TestResult test : testFalliti) {
                report.append("Test: ").append(test.getNomeTest()).append(" - Motivo: ").append(test.getMotivoFallimento()).append("\n");
            }
        } else {
            report.append("Tutti i test sono passati con successo.\n");
        }

        return report.toString();
    }

    /**
     * Genera un report dettagliato dei test
     * @param risultatiTest Dettaglio esecuzione
     * @return Report dettagliato dei test
     */
    public static String generaReportTest(List<TestResult> risultatiTest) {
        StringBuilder report = new StringBuilder();
        report.append("Report dettagliato dei test:\n");

        for (TestResult test : risultatiTest) {
            report.append("Test: ").append(test.getNomeTest())
                    .append(" - Stato: ").append(test.isFailed() ? "Fallito" : "Passato")
                    .append(" - Linee coperte: ").append(test.getLineeCoperte())
                    .append("\n");
        }

        return report.toString();
    }

    /**
     * Identifica i test falliti
     * @param risultatiTest Dettaglio esecuzione
     * @return Lista dei test falliti
     */
    public static List<String> identificaTestFalliti(List<TestResult> risultatiTest) {
        return risultatiTest.stream()
                .filter(TestResult::isFailed)
                .map(TestResult::getNomeTest)
                .collect(Collectors.toList());
    }
}
