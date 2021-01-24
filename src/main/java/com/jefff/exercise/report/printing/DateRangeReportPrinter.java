package com.jefff.exercise.report.printing;

import com.jefff.exercise.api.response.DateRangeQueryResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

@Slf4j
public class DateRangeReportPrinter {

    public void printDateRangeQueryResponseStream(Stream<DateRangeQueryResponse> queryResponses) {
        System.out.println("\nDateRangeQueryReport follows...");
        queryResponses.forEach(queryResponse -> System.out.printf("%s\n", queryResponse.formatReportLine()));
    }

}
