package ru.practicum.adapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeAdapter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime stringToLocalDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, formatter);
    }
}
