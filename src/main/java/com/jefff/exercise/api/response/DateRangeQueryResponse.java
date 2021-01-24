package com.jefff.exercise.api.response;

import com.jefff.exercise.api.request.DateRange;
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
