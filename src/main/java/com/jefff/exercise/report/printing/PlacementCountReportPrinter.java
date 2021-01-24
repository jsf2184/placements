package com.jefff.exercise.report.printing;

import com.jefff.exercise.api.response.PlacementCount;
import com.jefff.exercise.utility.PaddedArrayList;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class PlacementCountReportPrinter {
    public void printPlacementCountReport(PaddedArrayList<PlacementCount> placementCounts) {
        System.out.println("\nPrimary Report With PlacementCounts follows...");
        placementCounts
                .stream()
                .filter(Objects::nonNull) // null entries can exist in our placementCounts array if there are gaps in the IDs
                .forEach(placementCount -> System.out.println(placementCount.toString()));
    }
}
