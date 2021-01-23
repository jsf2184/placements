package com.jefff.exercise.model;

import com.jefff.exercise.utility.FieldMapper;
import lombok.Data;

import java.time.LocalDate;

@SuppressWarnings("unused")
@Data
public class PlacementCount {
    PlacementRecord placement;
    long impressionCount;

    public PlacementCount(PlacementRecord placement) {
        this.placement = placement;
        this.impressionCount = 0;
    }

    public void incrementImpressionCount(int delta) {
        impressionCount += delta;
    }

    public int getId() {
        return placement.getId();
    }

    public boolean includesDate(LocalDate date) {
        return placement.includesDate(date);
    }

    public String getName() {
        return placement.getName();
    }

    public LocalDate getStart() {
        return placement.getStart();
    }

    public LocalDate getEnd() {
        return placement.getEnd();
    }

    public int getCpm() {
        return placement.getCpm();
    }

    public long getImpressionCount() {
        return impressionCount;
    }

    public long getCpmRoundedCost() {
        final double actual = ((double) impressionCount * getCpm()) / 1000.00;
        return FieldMapper.roundDouble(actual);
    }

    public String toString() {
        return String.format("%s (%s-%s): %s impressions @ $%s CPM = $%s",
                             placement.getName(),
                             FieldMapper.formatDate(getStart()),
                             FieldMapper.formatDate(getEnd()),
                             FieldMapper.formatNumber(impressionCount),
                             FieldMapper.formatNumber(getCpm()),
                             FieldMapper.formatNumber(getCpmRoundedCost()));
    }
}
