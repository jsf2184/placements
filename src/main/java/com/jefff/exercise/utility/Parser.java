package com.jefff.exercise.utility;

import com.jefff.exercise.api.request.DateRange;
import com.jefff.exercise.entity.DeliveryRecord;
import com.jefff.exercise.entity.PlacementRecord;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Arrays;

@Slf4j
public class Parser {
    public static final int NUM_IMPRESSION_FIELDS = 3;
    public static final int NUM_PLACEMENT_FIELDS = 5;
    public static final int NUM_RANGE_FIELDS = 2;

    boolean skipHeaderLine;

    public Parser(boolean skipHeaderLine) {
        this.skipHeaderLine = skipHeaderLine;
    }

    public DeliveryRecord parseDelivery(String csvLine, int lineNumber) {
        if (lineNumber == 1 && skipHeaderLine) {
            // Don't bother parsing the header line.
            return null;
        }

        try {
            final String[] parts = validateNumFields(csvLine, ",", NUM_IMPRESSION_FIELDS, "Impression", lineNumber);
            final int placementId = toInteger(parts[0], "placementId", "Impression", lineNumber);
            final LocalDate date = toLocalDate(parts[1], "date", "Impression", lineNumber);
            final int numImpressions = toInteger(parts[2], "numImpressions", "Impression", lineNumber);
            return new DeliveryRecord(placementId, date, numImpressions);
        } catch (Exception ignore) {
            return null;
        }
    }

    public PlacementRecord parsePlacement(String csvLine, int lineNumber) {
        if (lineNumber == 1 && skipHeaderLine) {
            // Don't bother parsing the header line.
            return null;
        }

        try {
            final String[] parts = validateNumFields(csvLine, ",", NUM_PLACEMENT_FIELDS, "Placement", lineNumber);
            final int id = toInteger(parts[0], "id", "Placement", lineNumber);
            final String name = validateString(parts[1], "name", "Placement", lineNumber);
            final LocalDate start = toLocalDate(parts[2], "start", "Placement", lineNumber);
            final LocalDate end = toLocalDate(parts[3], "end", "Placement", lineNumber);
            final int cpm = toInteger(parts[4], "cpm", "Placement", lineNumber);
            return new PlacementRecord(id, name, start, end, cpm);
        } catch (Exception ignore) {
            return null;
        }

    }

    public DateRange parseDateRange(String rangeLine, int lineNumber) {
        // remove all white space.
        if (rangeLine == null) {
            return null;
        }
        rangeLine = rangeLine.replaceAll("\\s+", "");
        // split on the '-' that should separate the 2 parts
        try {
            final String[] parts = validateNumFields(rangeLine, "-", NUM_RANGE_FIELDS, "DateRange", lineNumber);
            final LocalDate start = toLocalDate(parts[0], "start", "DateRange", lineNumber);
            final LocalDate end = toLocalDate(parts[1], "end", "DateRange", lineNumber);
            return new DateRange(start, end);

        } catch (Exception ignore) {
            return null;
        }


    }

    public static String[] validateNumFields(String line,
                                             String splitChar,
                                             int numExpectedFields,
                                             String recordName,
                                             int lineNumber) throws Exception {

        if (line == null) {
            log.warn("{} Parsing Error. Null input for line number: {}, expecting {} fields",
                     recordName, lineNumber, NUM_IMPRESSION_FIELDS);
            throw new Exception();
        }

        final String[] parts = Arrays.stream(line.split(splitChar)).map(String::trim).toArray(String[]::new);
        if (parts.length != numExpectedFields) {
            log.warn("{} Parsing Error. Incorrect number of fields on line number: {}, expecting {} fields",
                     recordName, lineNumber, numExpectedFields);
            throw new Exception();
        }
        return parts;

    }


    public static String validateString(String str, String fieldName, String recordName, int lineNumber) throws Exception {
        if (FieldMapper.isEmpty(str)) {
            log.warn("{} Parsing Error. Invalid empty {} field on line number: {}",
                     recordName, fieldName, lineNumber);
            throw new Exception();
        }
        return str;
    }

    public static LocalDate toLocalDate(String str, String fieldName, String recordName, int lineNumber) throws Exception {
        final LocalDate date = FieldMapper.toDate(str);
        if (date == null) {
            log.warn("{} Parsing Error. Invalid {} field ('{}') on line number: {}, expecting MM/dd/yyyy",
                     recordName, fieldName, str, lineNumber);
            throw new Exception();
        }
        return date;
    }

    public static int toInteger(String str, String fieldName, String recordName, int lineNumber) throws Exception {
        final Integer result = FieldMapper.toInteger(str);
        if (result == null) {
            log.warn("{} Parsing Error. Invalid {} field ('{}') on line number: {}, expecting Integer",
                     recordName, fieldName, str, lineNumber);
            throw new Exception();
        }
        return result;
    }
}
