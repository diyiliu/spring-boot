package com.diyiliu.support.format;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Description: CNLocalDateFormatter
 * Author: DIYILIU
 * Update: 2017-10-25 08:56
 */
public class CNLocalDateFormatter implements Formatter<LocalDate> {
    public static final String CN_PATTERN = "yyyy-MM-dd";
    public static final String NORMAL_PATTERN = "dd/MM/yyyy";

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(getPattern(locale)));
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return DateTimeFormatter.ofPattern(getPattern(locale)).format(object);
    }

    public static String getPattern(Locale locale) {
        return isChina(locale) ? CN_PATTERN : NORMAL_PATTERN;
    }

    private static boolean isChina(Locale locale) {
        return Locale.CHINA.getCountry().equals(locale.getCountry());
    }
}