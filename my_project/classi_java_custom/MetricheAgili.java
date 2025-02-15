package classi_java_custom;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MetricheAgili {

    /**
     * Calcola il Velocity Score adattivo
     * @param sprintData Dati storici degli sprint
     * @param fattoreComplessità [1-5]
     * @return Velocity predittivo con deviazione standard
     */
    public static double calcolaVelocityScore(List<Double> sprintData, int fattoreComplessità) {
        if (sprintData == null || sprintData.isEmpty()) {
            throw new IllegalArgumentException("I dati degli sprint non possono essere nulli o vuoti.");
        }

        // Calcola la media dei dati storici degli sprint
        double mediaVelocity = sprintData.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        // Calcola la deviazione standard
        double deviazioneStandard = Math.sqrt(sprintData.stream()
                .mapToDouble(value -> Math.pow(value - mediaVelocity, 2))
                .sum() / sprintData.size());

        // Adatta il Velocity Score in base al fattore di complessità
        double velocityScore = mediaVelocity * (1 + (fattoreComplessità - 3) * 0.1);

        System.out.println("Velocity Score: " + velocityScore);
        System.out.println("Deviazione Standard: " + deviazioneStandard);

        return velocityScore;
    }

    /**
     * Genera il rapporto di maturità agile
     * @param metriche Mappa di metriche chiave
     * @return Rapporto strutturato in XML
     */
    public static String generaRapportoMaturita(Map<String, Double> metriche) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Creazione del documento XML
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("RapportoMaturitaAgile");
            doc.appendChild(rootElement);

            // Aggiunta delle metriche al rapporto
            for (Map.Entry<String, Double> entry : metriche.entrySet()) {
                Element metricaElement = doc.createElement("Metrica");
                metricaElement.setAttribute("nome", entry.getKey());
                metricaElement.setAttribute("valore", entry.getValue().toString());
                rootElement.appendChild(metricaElement);
            }

            // Trasformazione del documento XML in stringa
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new StringWriter());
            transformer.transform(source, result);

            return result.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la generazione del rapporto di maturità agile", e);
        }
    }

    /**
     * Calcola il Lead Time medio
     * @param leadTimes Lista dei tempi di lead
     * @return Lead Time medio
     */
    public static double calcolaLeadTimeMedio(List<Double> leadTimes) {
        if (leadTimes == null || leadTimes.isEmpty()) {
            throw new IllegalArgumentException("I tempi di lead non possono essere nulli o vuoti.");
        }

        return leadTimes.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    /**
     * Analizza la qualità del codice basata su metriche chiave
     * @param metriche Mappa di metriche chiave
     * @return Analisi della qualità del codice
     */
    public static String analizzaQualitaCodice(Map<String, Double> metriche) {
        // Esempio di analisi basata su metriche chiave
        double codeCoverage = metriche.getOrDefault("CodeCoverage", 0.0);
        double codeComplexity = metriche.getOrDefault("CodeComplexity", 0.0);
        double bugDensity = metriche.getOrDefault("BugDensity", 0.0);

        StringBuilder analisi = new StringBuilder();
        analisi.append("Analisi della qualità del codice:\n");
        analisi.append("Copertura del codice: ").append(codeCoverage).append("%\n");
        analisi.append("Complessità del codice: ").append(codeComplexity).append("\n");
        analisi.append("Densità di bug: ").append(bugDensity).append(" bug/KLOC\n");

        if (codeCoverage < 80) {
            analisi.append("Suggerimento: Aumentare la copertura del codice.\n");
        }
        if (codeComplexity > 10) {
            analisi.append("Suggerimento: Ridurre la complessità del codice.\n");
        }
        if (bugDensity > 0.5) {
            analisi.append("Suggerimento: Ridurre la densità di bug.\n");
        }

        return analisi.toString();
    }
