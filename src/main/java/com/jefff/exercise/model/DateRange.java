package com.jefff.exercise.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DateRange {
    LocalDate start;
    LocalDate end;
}
