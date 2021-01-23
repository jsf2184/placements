package com.jefff.exercise.service;

import com.jefff.exercise.collection.PaddedArrayList;
import com.jefff.exercise.io.input.LineStream;
import com.jefff.exercise.model.DateRangeQueryResponse;
import com.jefff.exercise.model.DeliveryRecord;
import com.jefff.exercise.model.PlacementCount;
import com.jefff.exercise.persistence.DataStore;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
public class ReportingService {
    DataStore dataStore;

    public ReportingService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public PaddedArrayList<PlacementCount> generatePrimaryReport() {
        // Create a 'placementCounts' PaddedArrayListCollection (indexed by placementId) to accumulate per
        // Placement Counts.
        log.info("Primary Report With PlacementCounts follows...\n");
        PaddedArrayList<PlacementCount> placementCounts = new PaddedArrayList<>();
        dataStore.getPlacements()
                 .map(PlacementCount::new)
                 .forEach(pc -> placementCounts.add(pc.getId(), pc));

        // Now go thru all of the deliveries, adding the count
        dataStore.getDeliveries().forEach(delivery -> addDeliveryCountToPlacement(delivery, placementCounts));
        return placementCounts;
    }


    public void printPrimaryReport(PaddedArrayList<PlacementCount> placementCounts) {
        placementCounts
                .stream()
                .filter(Objects::nonNull) // null entries can exist in our placementCounts array if there are gaps in the IDs
                .forEach(placementCount -> System.out.println(placementCount.toString()));
    }


    /**
     * addDeliveryCountToPlacement()
     * <p>
     * For a given deliveryRecord, find its corresponding placement. If its placement does not exist, or the date
     * of the deliveryRecord is not included in the Placement's range, log an error warning. Otherwise, add
     * the number of impressions for that deliveryRecord to the count for its placement.
     *
     * @param delivery        - the deliverRecord being processed
     * @param placementCounts - a PaddedArrayList collection of all the PlacementCounts
     */
    static void addDeliveryCountToPlacement(DeliveryRecord delivery, PaddedArrayList<PlacementCount> placementCounts) {
        final PlacementCount placementCount = placementCounts.get(delivery.getPlacementId());
        if (placementCount == null || !placementCount.includesDate(delivery.getDate())) {
            log.warn("Could not find suitable placement for deliveryRecord: {}", delivery.toString());
            return;
        }
        placementCount.incrementImpressionCount(delivery.getNumImpressions());
    }


    public Stream<DateRangeQueryResponse> generateDateQueryReponseStream(LineStream queryInputStream) {
        return null;
    }
}
