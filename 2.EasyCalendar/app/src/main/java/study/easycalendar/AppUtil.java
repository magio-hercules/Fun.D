package study.easycalendar;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUtil {

    public static String getStringByLong(long longTime, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, java.util.Locale.getDefault());
        return dateFormat.format(new Date(longTime));
    }

    public static String getStringByDate(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, java.util.Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String getStringByLocalDateTime(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    public static String getStringByLocalTime(LocalTime localTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localTime.format(dateTimeFormatter);
    }
}
