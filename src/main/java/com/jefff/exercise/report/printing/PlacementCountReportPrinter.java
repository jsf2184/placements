package com.jefff.exercise.report.printing;

import com.jefff.exercise.api.response.PlacementCount;
import com.jefff.exercise.utility.PaddedArrayList;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.util.Objects;

@Slf4j
public class PlacementCountReportPrinter {

    private PrintWriter writer;
    public PlacementCountReportPrinter(PrintWriter writer) {
        this.writer = writer;
    }

    public void printPlacementCountReport(PaddedArrayList<PlacementCount> placementCounts) {
        writer.printf("\n\nPrimary Report With PlacementCounts follows...\n");
        placementCounts
                .stream()
                .filter(Objects::nonNull) // null entries can exist in our placementCounts array if there are gaps in the IDs
                .forEach(placementCount -> writer.printf("%s\n", placementCount.toString()));
    }
}
