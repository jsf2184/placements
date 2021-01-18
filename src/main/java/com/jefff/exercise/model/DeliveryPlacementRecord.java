package com.jefff.exercise.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DeliveryPlacementRecord
 * <p>
 * A "joined" record that consists of a DeliveryRecord and the Placement that it
 * should be associated with. If there is no corresponding placement, it will be
 * null.
 */
@Data
@AllArgsConstructor
public class DeliveryPlacementRecord {
    DeliveryRecord delivery;
    PlacementRecord placement;

}
