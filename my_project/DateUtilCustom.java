import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtilCustom {

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

    
    /**
     * Calcola i giorni che mancano al compleanno di una persona.
     * @param dataNascita
     * @param nome
     * @return messaggio giorni che mancano al tuo compleanno
     */
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
     * Ottieni un messaggio magico in base al giorno della settimana di una data.
     * @param datamagica
     * @return Il messaggio magico.
     * @throws DateTimeParseException Se la data non è valida.
     */
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
}