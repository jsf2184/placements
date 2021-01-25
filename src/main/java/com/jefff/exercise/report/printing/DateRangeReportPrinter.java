package com.jefff.exercise.report.printing;

import com.jefff.exercise.api.response.DateRangeQueryResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.util.stream.Stream;

@Slf4j
public class DateRangeReportPrinter {

    private PrintWriter writer;

    public DateRangeReportPrinter(PrintWriter writer) {
        this.writer = writer;
    }

    public void printDateRangeQueryResponseStream(Stream<DateRangeQueryResponse> queryResponses) {
        writer.printf("\nDateRangeQueryReport follows...\n");
        queryResponses.forEach(queryResponse -> writer.printf("%s\n", queryResponse.formatReportLine()));
    }

}
