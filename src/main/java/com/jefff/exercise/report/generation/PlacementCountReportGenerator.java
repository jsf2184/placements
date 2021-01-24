package com.jefff.exercise.report.generation;

import com.jefff.exercise.api.response.PlacementCount;
import com.jefff.exercise.entity.DeliveryRecord;
import com.jefff.exercise.persistence.DataStore;
import com.jefff.exercise.utility.PaddedArrayList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlacementCountReportGenerator {
    DataStore dataStore;

    public PlacementCountReportGenerator(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public PaddedArrayList<PlacementCount> generatePlacementCounts() {
        // Create a skeletal 'placementCounts' PaddedArrayListCollection (indexed by placementId)
        // to accumulate per Placement Counts.
        //
        PaddedArrayList<PlacementCount> placementCounts = new PaddedArrayList<>();
        dataStore.getPlacements()
                 .map(PlacementCount::new)
                 .forEach(pc -> placementCounts.add(pc.getId(), pc));

        // Now go thru all of the deliveries, adding the count from each delivery to the
        // appropriate PlacementCount.
        //
        dataStore.getDeliveries().forEach(delivery -> addDeliveryCountToPlacement(delivery, placementCounts));
        return placementCounts;
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

}
