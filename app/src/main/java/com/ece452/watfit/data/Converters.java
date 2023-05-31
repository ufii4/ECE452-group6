package com.ece452.watfit.data;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public class Converters {
    @TypeConverter
    public static LocalDate fromDateString(String date) {
        return date == null ? null : LocalDate.parse(date);
    }

    @TypeConverter
    public static String toDateString(LocalDate date) {
        return date == null ? null : date.toString();
    }
}
