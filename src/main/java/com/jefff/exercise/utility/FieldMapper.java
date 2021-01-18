package com.jefff.exercise.utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FieldMapper {
    public static final String PARSE_PATTERN = "MM/dd/yyyy";
    public static final DateTimeFormatter DATE_PARSE_FORMATTER = DateTimeFormatter.ofPattern(PARSE_PATTERN);
    public static final String OUT_PATTERN = "M/d/yyyy";
    public static final DateTimeFormatter DATE_OUTPUT_FORMATTER = DateTimeFormatter.ofPattern(OUT_PATTERN);


    public static LocalDate toDate(String str) {
        if (isEmpty(str)) {
            return null;
        }
        str = normalizeDateString(str);
        try {
            LocalDate localDate = LocalDate.parse(str, DATE_PARSE_FORMATTER);
            return localDate;

        } catch (DateTimeParseException ignore) {
            return null;
        }
    }

    public static LocalDate toDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    public static String formatDate(LocalDate date) {
        return DATE_OUTPUT_FORMATTER.format(date);
    }

    /**
     * normalizeDateString()
     * <p>
     * Seems like the input files deal with a variety of dae formats.
     * 11/1/20
     * 11/1/2020
     * 11/01/2020
     * Regardless, of which input format we get, normalize to a fully consistent MM/DD/YYYY format
     * to make subsequent converstion to a LocalDate easier.
     *
     * @param str - input date string
     * @return - If this is a short date string with only a 2 digit year, prepend it with a '20'
     * Also, pad 1 digit day and month parts with a leading '0'
     */
    public static String normalizeDateString(String str) {

        if (str.length() < PARSE_PATTERN.length()) {
            // need to do some padding to get a fully normalized string.
            final String[] parts = str.split("/");
            if (parts.length == 3) {
                String monStr = parts[0];
                String dayStr = parts[1];
                String yrStr = parts[2];

                if (monStr.length() == 1) {
                    monStr = "0" + monStr;
                }
                if (dayStr.length() == 1) {
                    dayStr = "0" + dayStr;
                }
                if (yrStr.length() == 2) {
                    yrStr = "20" + yrStr;
                }
                return monStr + "/" + dayStr + "/" + yrStr;
            }
        }
        return str;
    }


    public static Integer toInteger(String str) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            final int res = Integer.parseInt(str);
            return res;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }
}
