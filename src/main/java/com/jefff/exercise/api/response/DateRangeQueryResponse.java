package com.jefff.exercise.api.response;

import com.jefff.exercise.api.request.DateRange;
import com.jefff.exercise.utility.FieldMapper;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class DateRangeQueryResponse {
    DateRange dateRange;
    long impressions;
    double cost;

    public DateRangeQueryResponse(DateRange dateRange) {
        this.dateRange = dateRange;
        impressions = 0;
        cost = 0;
    }

    public void add(int numImpressions, int cpm) {
        impressions += numImpressions;
        cost += numImpressions * cpm;
    }

    public String formatReportLine() {
        return String.format("Total (%s-%s): %s impressions, $%s",
                             FieldMapper.formatDate(dateRange.getStart()),
                             FieldMapper.formatDate(dateRange.getEnd()),
                             FieldMapper.formatNumber(impressions),
                             FieldMapper.formatNumber(FieldMapper.roundDouble(cost / 1000)));
    }
}
