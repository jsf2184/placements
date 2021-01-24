package com.jefff.exercise.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DateRange {
    LocalDate start;
    LocalDate end;
}
