import java.util.Arrays;
import java.time.LocalDate;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;

public class BasketballStats {
    // ... (mantenere implementazione esistente invariata) ...
}

class DateUtilCustom {
    // ... (mantenere implementazione esistente invariata) ...
}

class GiorniMagici {
    // ... (mantenere implementazione esistente invariata) ...
}

class CalcolatoreStatisticheBaseball {
    /**
     * Calcola la media battuta di un giocatore
     * @param hits Numero di valide
     * @param atBats Numero di turni di battuta
     * @return Media battuta formattata
     */
    public static String calcolaMediaBattuta(int hits, int atBats) {
        return String.format("%.3f", (double)hits / atBats);
    }

    /**
     * Determina il valore ERA di un lanciatore
     * @param earnedRuns Punti subiti
     * @param innings Inning lanciati
     * @return ERA formattato
     */
    public static String calcolaERA(int earnedRuns, double innings) {
        return String.format("%.2f", (earnedRuns * 9) / innings);
    }
}

class GestioneFinanzePersonali {
    /**
     * Calcola il risparmio mensile in base agli obiettivi
     * @param reddito Reddito mensile
     * @param spese Spese fisse
     * @param obiettivo Obiettivo annuale
     * @return Risparmio necessario mensilmente
     */
    public static double calcolaRisparmioMensile(double reddito, double spese, double obiettivo) {
        return (obiettivo / 12) + (reddito - spese);
    }

    /**
     * Genera un piano di ammortamento
     * @param importoPrestito Importo totale
     * @param tassoInteresse Tasso annuo
     * @param anni Durata
     * @return Rata mensile
     */
    public static double calcolaRataMutuo(double importoPrestito, double tassoInteresse, int anni) {
        double tassoMensile = tassoInteresse / 1200;
        int mesi = anni * 12;
        return (importoPrestito * tassoMensile) / (1 - Math.pow(1 + tassoMensile, -mesi));
    }
}

class GestioneInventario {
    /**
     * Calcola il valore totale dell'inventario
     * @param quantita Array delle quantità
     * @param prezzi Array dei prezzi
     * @return Valore totale
     */
    public static double calcolaValoreInventario(int[] quantita, double[] prezzi) {
        double totale = 0;
        for(int i = 0; i < quantita.length; i++) {
            totale += quantita[i] * prezzi[i];
        }
        return totale;
    }

    /**
     * Trova gli articoli sotto il livello minimo
     * @param quantita Quantità corrente
     * @param livelloMin Livello minimo
     * @return Array di indici degli articoli da riordinare
     */
    public static int[] trovaDaRiordinare(int[] quantita, int livelloMin) {
        return java.util.stream.IntStream.range(0, quantita.length)
            .filter(i -> quantita[i] < livelloMin)
            .toArray();
    }
}

class AnalizzatoreTesto {
    /**
     * Conta le parole in una stringa
     * @param testo Testo da analizzare
     * @return Numero di parole
     */
    public static int contaParole(String testo) {
        return testo.split("\\s+").length;
    }

    /**
     * Inverte una stringa mantenendo la posizione delle maiuscole
     * @param input Stringa originale
     * @return Stringa invertita
     */
    public static String invertiStringaMantenendoMaiuscole(String input) {
        char[] chars = input.toCharArray();
        int i = 0;
        int j = chars.length - 1;
        
        while (i < j) {
            boolean iUpper = Character.isUpperCase(chars[i]);
            boolean jUpper = Character.isUpperCase(chars[j]);
            
            char temp = chars[i];
            chars[i] = Character.isUpperCase(chars[j]) ? 
                Character.toUpperCase(chars[j]) : Character.toLowerCase(chars[j]);
            
            chars[j] = Character.isUpperCase(temp) ? 
                Character.toUpperCase(temp) : Character.toLowerCase(temp);
            
            i++;
            j--;
        }
        return new String(chars);
    }
}

class CalcolatriceScientifica {
    /**
     * Calcola il logaritmo in base personalizzata
     * @param numero Valore positivo
     * @param base Base del logaritmo
     * @return Risultato del logaritmo
     */
    public static double logBase(double numero, double base) {
        return Math.log(numero) / Math.log(base);
    }

    /**
     * Calcola la radice n-esima
     * @param numero Radicando
     * @param indice Indice della radice
     * @return Risultato
     */
    public static double radiceN(double numero, int indice) {
        return Math.pow(numero, 1.0 / indice);
    }
}

class GestionePassword {
    private static final String CARATTERI = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";

    /**
     * Genera una password casuale sicura
     * @param lunghezza Lunghezza della password
     * @return Password generata
     */
    public static String generaPassword(int lunghezza) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < lunghezza; i++) {
            sb.append(CARATTERI.charAt(random.nextInt(CARATTERI.length())));
        }
        return sb.toString();
    }

    /**
     * Verifica la complessità della password
     * @param password Password da verificare
     * @return true se la password è valida
     */
    public static boolean validaPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }
}

class CalcolatoreBMI {
    /**
     * Calcola l'indice di massa corporea
     * @param peso Peso in kg
     * @param altezza Altezza in metri
     * @return Valore BMI
     */
    public static double calcolaBMI(double peso, double altezza) {
        return peso / (altezza * altezza);
    }

    /**
     * Restituisce la categoria del BMI
     * @param bmi Valore BMI calcolato
     * @return Categoria di peso
     */
    public static String getCategoriaBMI(double bmi) {
        if(bmi < 18.5) return "Sottopeso";
        else if(bmi < 25) return "Normale";
        else if(bmi < 30) return "Sovrappeso";
        else return "Obeso";
    }
}

class ConvertitoreUnita {
    /**
     * Converti chilometri in miglia
     * @param km Valore in chilometri
     * @return Valore in miglia
     */
    public static double kmToMiglia(double km) {
        return km * 0.621371;
    }

    /**
     * Converti Celsius in Fahrenheit
     * @param celsius Temperatura in Celsius
     * @return Temperatura in Fahrenheit
     */
    public static double celsiusToFahrenheit(double celsius) {
        return (celsius * 9/5) + 32;
    }
}

class AnalizzatoreInvestimenti {
    /**
     * Calcola il Return on Investment (ROI)
     * @param guadagno Guadagno totale
     * @param costo Costo iniziale
     * @return ROI percentuale
     */
    public static double calcolaROI(double guadagno, double costo) {
        return ((guadagno - costo) / costo) * 100;
    }

    /**
     * Calcola il Valore Attuale Netto (VAN)
     * @param tasso Tasso di sconto
     * @param flussi Flussi di cassa
     * @return Valore VAN
     */
    public static double calcolaVAN(double tasso, double[] flussi) {
        double van = 0;
        for(int i = 0; i < flussi.length; i++) {
            van += flussi[i] / Math.pow(1 + tasso, i);
        }
        return van;
    }
}

class GestoreProgetti {
    /**
     * Calcola la durata del progetto usando il metodo PERT
     * @param ottimistica Tempo ottimistico
     * @param probabile Tempo probabile
     * @param pessimistica Tempo pessimistico
     * @return Durata stimata
     */
    public static double stimaDurataPERT(double ottimistica, double probabile, double pessimistica) {
        return (ottimistica + 4 * probabile + pessimistica) / 6;
    }

    /**
     * Calcola il percorso critico
     * @param attivita Lista di attività con durate
     * @return Durata totale del percorso critico
     */
    public static double calcolaPercorsoCritico(double[] attivita) {
        return Arrays.stream(attivita).sum();
    }
}

class SimulatoreEcologico {
    /**
     * Simula la crescita della popolazione
     * @param popolazioneIniziale Popolazione iniziale
     * @param tassoCrescita Tasso di crescita annuo
     * @param anni Numero di anni
     * @return Popolazione finale
     */
    public static int simulaCrescitaPopolazione(int popolazioneIniziale, double tassoCrescita, int anni) {
        return (int) (popolazioneIniziale * Math.pow(1 + tassoCrescita, anni));
    }

    /**
     * Modello preda-predatore (equazioni Lotka-Volterra)
     * @param x Popolazione prede
     * @param y Popolazione predatori
     * @param alpha Parametro alpha
     * @param beta Parametro beta
     * @return Array con nuove popolazioni [nuovePrede, nuoviPredatori]
     */
    public static double[] modelloPredaPredatore(double x, double y, double alpha, double beta) {
        double dx = x * (1 - x) - alpha * x * y;
        double dy = -y * (1 - y) + beta * x * y;
        return new double[]{x + dx, y + dy};
    }
}

class StrumentiCrittografia {
    /**
     * Cifratura Caesar
     * @param testo Testo originale
     * @param shift Spostamento
     * @return Testo cifrato
     */
    public static String cifraCaesar(String testo, int shift) {
        StringBuilder risultato = new StringBuilder();
        for(char c : testo.toCharArray()) {
            if(Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char)(((c - base + shift) % 26) + base);
            }
            risultato.append(c);
        }
        return risultato.toString();
    }

    /**
     * Decifratura Caesar
     * @param testo Testo cifrato
     * @param shift Spostamento originale
     * @return Testo decifrato
     */
    public static String decifraCaesar(String testo, int shift) {
        return cifraCaesar(testo, 26 - (shift % 26));
    }
}

class AnalizzatoreCodice {
    /**
     * Conta le linee di codice in una stringa
     * @param codice Codice sorgente
     * @return Numero di linee
     */
    public static int contaLineeCodice(String codice) {
        return codice.split("\n").length;
    }

    /**
     * Calcola la complessità ciclomatica approssimativa
     * @param codice Codice sorgente
     * @return Numero di decisioni + 1
     */
    public static int calcolaComplessitaCiclomatica(String codice) {
        int count = 1;
        String[] keywords = {"if", "while", "for", "case", "catch", "&&", "||"};
        for(String keyword : keywords) {
            count += codice.split(keyword).length - 1;
        }
        return count;
    }
}

class GestoreInventario {
    private ArrayList<String> articoli = new ArrayList<>();

    /**
     * Aggiungi un articolo all'inventario (FIFO)
     * @param articolo Articolo da aggiungere
     */
    public void aggiungiArticolo(String articolo) {
        articoli.add(articolo);
    }

    /**
     * Rimuovi un articolo dall'inventario (FIFO)
     * @return Articolo rimosso
     */
    public String rimuoviArticolo() {
        return articoli.remove(0);
    }
}

class SimulatoreMeteo {
    private static final String[] CONDIZIONI = {"Soleggiato", "Nuvoloso", "Pioggia", "Temporale"};
    private static Random rand = new Random();

    /**
     * Genera una previsione meteo casuale
     * @return Condizione meteo
     */
    public static String generaPrevisione() {
        return CONDIZIONI[rand.nextInt(CONDIZIONI.length)];
    }

    /**
     * Simula la temperatura per una regione
     * @param latitudine Coordinata latitudine
     * @param mese Mese dell'anno (1-12)
     * @return Temperatura simulata
     */
    public static double simulaTemperatura(double latitudine, int mese) {
        return 20 + 10 * Math.sin(Math.toRadians(mese * 30)) + latitudine * 0.1;
    }
}

class GestoreReteNeurale {
    /**
     * Funzione di attivazione ReLU
     * @param x Valore di input
     * @return Valore di output
     */
    public static double relu(double x) {
        return Math.max(0, x);
    }

    /**
     * Calcola l'errore quadratico medio
     * @param previsioni Array di previsioni
     * @param target Valori target
     * @return Errore medio
     */
    public static double calcolaErrore(double[] previsioni, double[] target) {
        double errore = 0;
        for(int i = 0; i < previsioni.length; i++) {
            errore += Math.pow(previsioni[i] - target[i], 2);
        }
        return errore / previsioni.length;
    }
}

class StrumentiGrafica {
    /**
     * Converte un'immagine in scala di grigi
     * @param rgb Valore RGB originale
     * @return Valore RGB in scala di grigi
     */
    public static int convertiGrigio(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        int media = (r + g + b) / 3;
        return (media << 16) | (media << 8) | media;
    }

    /**
     * Regola la luminosità di un colore
     * @param rgb Valore RGB originale
     * @param percentuale Percentuale di luminosità
     * @return Nuovo valore RGB
     */
    public static int regolaLuminosita(int rgb, double percentuale) {
        int r = (int)(((rgb >> 16) & 0xFF) * percentuale);
        int g = (int)(((rgb >> 8) & 0xFF) * percentuale);
        int b = (int)((rgb & 0xFF) * percentuale);
        return (Math.min(r, 255) << 16) | (Math.min(g, 255) << 8) | Math.min(b, 255);
    }
}

class GestoreDatabase {
    private HashMap<Integer, String> dati = new HashMap<>();
    private int nextId = 1;

    /**
     * Inserisci un nuovo record
     * @param valore Valore da inserire
     * @return ID del record
     */
    public int inserisci(String valore) {
        dati.put(nextId, valore);
        return nextId++;
    }

    /**
     * Cerca un record per ID
     * @param id ID del record
     * @return Valore trovato o null
     */
    public String cerca(int id) {
        return dati.get(id);
    }
}