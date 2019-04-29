package ai.jobox.assignment.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    private static SimpleDateFormat apiRequestDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public static String getCurrentDate() {
        return apiRequestDateFormat.format(Calendar.getInstance().getTime());
    }

    /**
     * Retrieves the Day after given date in the desired format.
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getNextDay(String date) {

        Date currentDate = null;
        try {
            currentDate = apiRequestDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // convert date to calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // manipulate date
        calendar.add(Calendar.DATE, 1);

        return apiRequestDateFormat.format(calendar.getTime());
    }

    /**
     * Retrieves the day before given date in the desired format.
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getPreviousDay(String date) {

        Date currentDate = null;
        try {
            currentDate = apiRequestDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // convert date to calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // manipulate date
        calendar.add(Calendar.DATE, -1);

        return apiRequestDateFormat.format(calendar.getTime());
    }
}
