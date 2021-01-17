package com.jefff.exercise.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PlacementRecord {
    final int id;
    final String name;
    final LocalDate start;
    final LocalDate end;
    int cpm;

    public boolean includesDate(LocalDate date) {
        return (date.isEqual(start) || date.isAfter(start)) &&
                (date.isEqual(end) || date.isBefore(end));
    }
}
