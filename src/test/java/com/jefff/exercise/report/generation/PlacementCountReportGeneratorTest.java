package com.jefff.exercise.report.generation;

import com.jefff.exercise.api.response.PlacementCount;
import com.jefff.exercise.entity.DeliveryRecord;
import com.jefff.exercise.entity.PlacementRecord;
import com.jefff.exercise.persistence.DataStore;
import com.jefff.exercise.utility.FieldMapper;
import com.jefff.exercise.utility.PaddedArrayList;
import com.jefff.exercise.utility.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlacementCountReportGeneratorTest {
    Parser parser = new Parser(false);
    DataStore dataStore;

    public static final int GOOD_DELIVERY1_IMPRESSIONS = 35123;
    public static final int GOOD_DELIVERY2_IMPRESSIONS = 35124;
    public static final int BAD_DELIVERY1_IMPRESSIONS = 35125;

    // Two placementRecords in our world. Note that placementRecord2 will have no corresponding deliveryRecords
    PlacementRecord placementRecord2 = parser.parsePlacement("2 ,Business,12/1/20,12/31/20,8", 2);

    // But placementRecord3 will have 3 deliveries associated with it.
    PlacementRecord placementRecord3 = parser.parsePlacement("3 ,Travel  ,11/1/20,11/30/20,3", 3);

    // Delivery Records for placementRecord 3 (2 good, and one bad)
    DeliveryRecord goodDeliveryP3_1 = new DeliveryRecord(3, FieldMapper.toDate("11/25/2020"), GOOD_DELIVERY1_IMPRESSIONS);
    DeliveryRecord goodDeliveryP3_2 = new DeliveryRecord(3, FieldMapper.toDate("11/28/2020"), GOOD_DELIVERY2_IMPRESSIONS);
    DeliveryRecord badDeliveryP3_1 = new DeliveryRecord(3, FieldMapper.toDate("11/25/2025"), BAD_DELIVERY1_IMPRESSIONS);

    PlacementCountReportGenerator placementCountReportGenerator;

    @Before
    public void testSetup() {
        dataStore = mock(DataStore.class);
        when(dataStore.getPlacementRecord(2)).thenReturn(placementRecord2);
        when(dataStore.getPlacementRecord(3)).thenReturn(placementRecord3);
        when(dataStore.getPlacements()).thenReturn(Stream.of(placementRecord2, placementRecord3));
        when(dataStore.getDeliveries()).thenReturn(Stream.of(goodDeliveryP3_1, goodDeliveryP3_2, badDeliveryP3_1));
        placementCountReportGenerator = new PlacementCountReportGenerator(dataStore);
    }

    @Test
    public void testGeneratePlacementCounts() {
        final PaddedArrayList<PlacementCount> actual = placementCountReportGenerator.generatePlacementCounts();
        Assert.assertNull(actual.get(0));
        Assert.assertNull(actual.get(1));

        final PlacementCount resultForPlacement2 = actual.get(2);
        // Build expectations for placement 2
        PlacementCount expectedForPlacement2 = new PlacementCount(placementRecord2);
        Assert.assertEquals(expectedForPlacement2, resultForPlacement2);

        final PlacementCount resultForPlacement3 = actual.get(3);


    }
}