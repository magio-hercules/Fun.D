package com.fundroid.routinesc.data;

import androidx.room.TypeConverter;

import java.time.LocalTime;

public class DateConverter {

    @TypeConverter
    public static LocalTime toTime(Long timestamp) {
        return timestamp == null ? null : LocalTime.ofNanoOfDay(timestamp);
    }

    @TypeConverter
    public static Long toLong(LocalTime time) {
        return time == null ? null : time.toNanoOfDay();
    }
}
