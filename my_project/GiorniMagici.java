import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GiorniMagici {
    
    // Verifica se una data è magica (giorno * mese = ultime due cifre dell'anno)
    public static boolean isDataMagica(int giorno, int mese, int anno) {
        int ultimeDueCifreAnno = anno % 100;
        return (giorno * mese) == ultimeDueCifreAnno;
    }
    
    // Genera un numero magico dalla somma delle cifre della data
    public static int generaNumeroMagico(int giorno, int mese, int anno) {
        int somma = sommaCifre(giorno) + sommaCifre(mese) + sommaCifre(anno);
        return somma * somma; // Quadrato della somma
    }
    
    // Ottieni un messaggio magico in base al giorno della settimana
    public static String getMessaggioMagico(LocalDate datamagica) throws DateTimeParseException {
        DayOfWeek giornoSettimana = datamagica.getDayOfWeek();
        
        switch(giornoSettimana) {
            case MONDAY: return "La magia inizia nel silenzio...";
            case TUESDAY: return "I sussurri degli antichi si fanno sentire.";
            case WEDNESDAY: return "Il velo tra i mondi è sottile oggi.";
            case THURSDAY: return "L'energia magica è potente e chiara.";
            case FRIDAY: return "Attenzione agli incantesimi del crepuscolo.";
            case SATURDAY: return "Il giorno perfetto per scoprire segreti nascosti.";
            case SUNDAY: return "Riposa e rigenera il tuo potere magico.";
            default: return "Il giorno è avvolto nel mistero...";
        }
    }
    
    // Verifica se un numero è magico (primo e nella sequenza di Fibonacci)
    public static boolean isNumeroMagico(int numero) {
        return isPrime(numero) && isFibonacci(numero);
    }
    
    // Metodi privati di supporto
    private static int sommaCifre(int numero) {
        int somma = 0;
        numero = Math.abs(numero);
        while (numero != 0) {
            somma += numero % 10;
            numero /= 10;
        }
        return somma;
    }
    
    private static boolean isPrime(int numero) {
        if (numero <= 1) return false;
        if (numero <= 3) return true;
        if (numero % 2 == 0 || numero % 3 == 0) return false;
        
        for (int i = 5; i * i <= numero; i += 6) {
            if (numero % i == 0 || numero % (i + 2) == 0) return false;
        }
        return true;
    }
    
    private static boolean isFibonacci(int numero) {
        int test1 = 5 * numero * numero + 4;
        int test2 = 5 * numero * numero - 4;
        return isPerfectSquare(test1) || isPerfectSquare(test2);
    }
    
    private static boolean isPerfectSquare(int n) {
        int sqrt = (int) Math.sqrt(n);
        return sqrt * sqrt == n;
    }
}