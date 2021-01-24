package com.jefff.exercise.report;

import com.jefff.exercise.api.request.DateRange;
import com.jefff.exercise.api.response.DateRangeQueryResponse;
import com.jefff.exercise.api.response.PlacementCount;
import com.jefff.exercise.report.generation.DateRangeReportGenerator;
import com.jefff.exercise.report.generation.PlacementCountReportGenerator;
import com.jefff.exercise.report.printing.DateRangeReportPrinter;
import com.jefff.exercise.report.printing.PlacementCountReportPrinter;
import com.jefff.exercise.utility.PaddedArrayList;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

@Slf4j
public class ReportingService {

    PlacementCountReportGenerator placementCountReportGenerator;
    PlacementCountReportPrinter placementCountReportPrinter;
    DateRangeReportGenerator dateRangeReportGenerator;
    DateRangeReportPrinter dateRangeReportPrinter;

    public ReportingService(PlacementCountReportGenerator placementCountReportGenerator,
                            PlacementCountReportPrinter placementCountReportPrinter,
                            DateRangeReportGenerator dateRangeReportGenerator,
                            DateRangeReportPrinter dateRangeReportPrinter) {
        this.placementCountReportGenerator = placementCountReportGenerator;
        this.placementCountReportPrinter = placementCountReportPrinter;
        this.dateRangeReportGenerator = dateRangeReportGenerator;
        this.dateRangeReportPrinter = dateRangeReportPrinter;
    }

    public void handlePlacementCountReport() {
        final PaddedArrayList<PlacementCount> placementCounts = placementCountReportGenerator.generatePlacementCounts();
        placementCountReportPrinter.printPlacementCountReport(placementCounts);
    }

    public void handleDateRangeReport(Stream<DateRange> dateRangeRequestStream) {
        final Stream<DateRangeQueryResponse> rangeResponseStream =
                dateRangeReportGenerator.generateRangeQueryResponseStream(dateRangeRequestStream);

        dateRangeReportPrinter.printDateRangeQueryResponseStream(rangeResponseStream);
    }
}
