package com.jefff.exercise.model;

import com.jefff.exercise.utility.FieldMapper;

public class DateRangeQueryResponse {
    DateRange dateRange;
    long impressions;
    double cost;

    public String formatReportLine() {
        return String.format("Total (%s-%s): %s impressions, $%s",
                             FieldMapper.formatDate(dateRange.getStart()),
                             FieldMapper.formatDate(dateRange.getEnd()),
                             FieldMapper.formatNumber(impressions),
                             FieldMapper.formatNumber(FieldMapper.roundDouble(cost)));
    }
}
