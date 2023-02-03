package br.edu.utfpr.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateTimeUtil {

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    public static Date localDateTimeToDate(LocalDateTime value) {
        return Date.from(ZonedDateTime.of(value, ZoneId.systemDefault()).toInstant());
    }

}
