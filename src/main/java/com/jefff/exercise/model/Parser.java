package com.jefff.exercise.model;

import com.jefff.exercise.FieldMapper;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class Parser {
    public static final int NUM_IMPRESSION_FIELDS = 3;
    public static final int NUM_PLACEMENT_FIELDS = 5;

    ImpressionRecord parseImpression(String csvLine, int lineNumber) {
        try {
            final String[] parts = validateNumFields(csvLine, NUM_IMPRESSION_FIELDS, "Impression", lineNumber);
            final int placementId = toInteger(parts[0], "placementId", "Impression", lineNumber);
            final LocalDate date = toLocalDate(parts[1], "date", "Impression", lineNumber);
            final int numImpressions = toInteger(parts[2], "numImpressions", "Impression", lineNumber);
            return new ImpressionRecord(placementId, date, numImpressions);
        } catch (Exception ignore) {
            return null;
        }
    }

    PlacementRecord parsePlacement(String csvLine, int lineNumber) {
        try {
            final String[] parts = validateNumFields(csvLine, NUM_PLACEMENT_FIELDS, "Placement", lineNumber);
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

    public static String[] validateNumFields(String line,
                                             int numExpectedFields,
                                             String recordName,
                                             int lineNumber) throws Exception {

        if (line == null) {
            log.warn("{} Parsing Error. Null input for line number: {}, expecting {} fields",
                     recordName, lineNumber, NUM_IMPRESSION_FIELDS);
            throw new Exception();
        }

        final String[] parts = line.split(",");
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
