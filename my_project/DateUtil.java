import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    /**
     * Formatta una data nel formato "dd/MM/yyyy".
     *
     * @param date La data da formattare.
     * @return La data formattata come stringa.
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    /**
     * Formatta una data nel formato specificato.
     *
     * @param date   La data da formattare.
     * @param format Il formato desiderato (es. "yyyy-MM-dd").
     * @return La data formattata come stringa.
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * Converte una stringa in un oggetto Date.
     *
     * @param dateString La stringa da convertire (es. "31/12/2023").
     * @param format     Il formato della stringa (es. "dd/MM/yyyy").
     * @return L'oggetto Date corrispondente.
     * @throws ParseException Se la stringa non è nel formato corretto.
     */
    public static Date parseDate(String dateString, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dateString);
    }

    /**
     * Calcola la differenza in giorni tra due date.
     *
     * @param startDate La data di inizio.
     * @param endDate   La data di fine.
     * @return La differenza in giorni.
     */
    public static long getDifferenceInDays(Date startDate, Date endDate) {
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
     * Calcola la differenza in ore tra due date.
     *
     * @param startDate La data di inizio.
     * @param endDate   La data di fine.
     * @return La differenza in ore.
     */
    public static long getDifferenceInHours(Date startDate, Date endDate) {
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        return TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
     * Aggiunge un numero specificato di giorni a una data.
     *
     * @param date  La data di partenza.
     * @param days  Il numero di giorni da aggiungere.
     * @return La nuova data.
     */
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    /**
     * Verifica se una data è nel passato rispetto alla data corrente.
     *
     * @param date La data da verificare.
     * @return True se la data è nel passato, altrimenti False.
     */
    public static boolean isPastDate(Date date) {
        Date currentDate = new Date();
        return date.before(currentDate);
    }

    

    public static String giorniAlmiocompleannoSpecial(Date dataNascita, String nome) {
        return "Caro" + nome + " mancano " + getDifferenceInDays(dataNascita, getCurrentDate()) + " giorni al tuo compleanno";
    }

    /**
     * Verifica se una data è nel futuro rispetto alla data corrente.
     *
     * @param date La data da verificare.
     * @return True se la data è nel futuro, altrimenti False.
     */
    public static boolean isFutureDate(Date date) {
        Date currentDate = new Date();
        return date.after(currentDate);
    }

    /**
     * Restituisce la data corrente.
     *
     * @return La data corrente.
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * Restituisce una stringa magica con un messaggio specifico in base al giorno indicato....
     * @param date
     * @return
     */
    public static String sorpresa (LocalDate date){
        String uao = GiorniMagici.getMessaggioMagico(date) ;
        return uao;
    }
    // Esempio di utilizzo della libreria
    public static void main(String[] args) {
  //      try {
            System.out.println(sorpresa(LocalDate.of(2025, 1, 28)));
            // Ottenere la data corrente
  //          Date currentDate = DateUtil.getCurrentDate();
   //         System.out.println("Data corrente: " + DateUtil.formatDate(currentDate));

            // Aggiungere 5 giorni alla data corrente
  //          Date futureDate = DateUtil.addDays(currentDate, 5);
   //         System.out.println("Data tra 5 giorni: " + DateUtil.formatDate(futureDate));

            // Verificare se la data è nel futuro
    //        if (DateUtil.isFutureDate(futureDate)) {
     //           System.out.println("La data è nel futuro!");
       //     }

            // Calcolare la differenza in giorni tra due date
    //        Date pastDate = DateUtil.parseDate("01/01/2023", "dd/MM/yyyy");
     //       long diffInDays = DateUtil.getDifferenceInDays(pastDate, currentDate);
      //      System.out.println("Differenza in giorni: " + diffInDays);

//        } catch (ParseException e) {
 //           System.out.println("Errore nel parsing della data: " + e.getMessage());
 //       }
    }
}