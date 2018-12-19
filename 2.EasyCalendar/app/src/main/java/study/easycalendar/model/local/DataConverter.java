package study.easycalendar.model.local;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;


public class DataConverter {

    @TypeConverter
    public static LocalDate toDate(Long timestamp) {
        if (timestamp == null) return null;
        return LocalDate.ofEpochDay(timestamp);
    }

    @TypeConverter
    public static Long toLong(LocalDate date) {
        if (date == null) return null;
        return date.toEpochDay();
    }

    @TypeConverter
    public static LocalTime toTime(Long timestamp) {
        return timestamp == null ? null : LocalTime.ofNanoOfDay(timestamp);
    }

    @TypeConverter
    public static Long toLong(LocalTime time) {
        return time == null ? null : time.toNanoOfDay();
    }


}
