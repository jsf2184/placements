package com.jefff.exercise.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ImpressionRecord {
    final int placementId;
    final LocalDate date;
    final int numImpressions;
}
