package dev.sorokin.eventmanager.event;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeConverter {

    public LocalDateTime parseToLocalDateTime(String dateTimeString) {
        return java.time.OffsetDateTime.parse(dateTimeString).toLocalDateTime();
    }

    public String formatToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return dateTime.format(formatter);
    }
}
