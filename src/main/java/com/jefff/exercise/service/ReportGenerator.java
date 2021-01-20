package com.jefff.exercise.service;

import com.jefff.exercise.collection.PaddedArrayList;
import com.jefff.exercise.model.PlacementCount;
import com.jefff.exercise.persistence.DataStore;

public class ReportGenerator {
    DataStore dataStore;

    public ReportGenerator(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public PaddedArrayList<PlacementCount> generatePrimaryReport() {
        // Create a result with a place to accumulate per Placement Counts.
        PaddedArrayList<PlacementCount> result = new PaddedArrayList<>();
        dataStore.getPlacements()
                 .map(PlacementCount::new)
                 .forEach(pc -> result.add(pc.getId(), pc));

        dataStore.getDeliveries()
                 .forEach(d -> {
                     final PlacementCount placementCount = result.get(d.getPlacementId());
                     if (placementCount != null && placementCount.includesDate(d.getDate())) {
                         placementCount.incrementImpressionCount(d.getNumImpressions());
                     }
                 });
        return result;
    }

}
