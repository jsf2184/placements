package com.jefff.exercise.persistence;

import com.jefff.exercise.api.request.DateRange;
import com.jefff.exercise.entity.DeliveryRecord;
import com.jefff.exercise.utility.FieldMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataStoreTest {

    public static final LocalDate NOV_1 = FieldMapper.toDate("11/1/2020");
    public static final LocalDate DEC_1 = FieldMapper.toDate("12/1/2020");
    public static final LocalDate DEC_7 = FieldMapper.toDate("12/7/2020");
    public static final LocalDate DEC_10 = FieldMapper.toDate("12/10/2020");
    public static final LocalDate DEC_15 = FieldMapper.toDate("12/15/2020");
    public static final LocalDate DEC_20 = FieldMapper.toDate("12/20/2020");

    public static final LocalDate DEC_23_2025 = FieldMapper.toDate("12/23/2025");
    public static final LocalDate JAN_23_2026 = FieldMapper.toDate("1/23/2026");

    // Create 2 DeliveryRecords for every date between 12/1 and 12/15 but 12/10
    public static final DeliveryRecord DR_DEC_1_100 = new DeliveryRecord(1, DEC_1, 100);
    public static final DeliveryRecord DR_DEC_1_200 = new DeliveryRecord(2, DEC_1, 200);
    public static final DeliveryRecord DR_DEC_7_100 = new DeliveryRecord(1, DEC_7, 100);
    public static final DeliveryRecord DR_DEC_7_200 = new DeliveryRecord(2, DEC_7, 200);
    public static final DeliveryRecord DR_DEC_15_100 = new DeliveryRecord(1, DEC_15, 100);
    public static final DeliveryRecord DR_DEC_15_200 = new DeliveryRecord(2, DEC_15, 200);

    public static final List<DeliveryRecord> ALL_DELIVERIES =
            Arrays.asList(DR_DEC_1_100,
                          DR_DEC_1_200,
                          DR_DEC_7_100,
                          DR_DEC_7_200,
                          DR_DEC_15_100,
                          DR_DEC_15_200);


    DataStore dataStore;

    @Before
    public void testSetup() {
        dataStore = new DataStore();
        ALL_DELIVERIES.forEach(dataStore::add);
    }

    @Test
    public void testRetrieveDeliveryRecordsInRange() {
        // Retrieve an interesting range, the dates for which have no records,
        // but which should include those records from 12/1 and 12/7
        DateRange dateRange = new DateRange(NOV_1, DEC_10);
        List<DeliveryRecord> retrieved = dataStore.getDeliveries(dateRange)
                                                  .collect(Collectors.toList());
        List<DeliveryRecord> expectedDeliveries = Arrays.asList(
                DR_DEC_1_100,
                DR_DEC_1_200,
                DR_DEC_7_100,
                DR_DEC_7_200
        );

        Assert.assertEquals(expectedDeliveries, retrieved);

        // Lets try another retrieval which should return the same deliveries, but make the
        // DateRange explicitly bound the records we know to be in the DataStore
        dateRange = new DateRange(DEC_1, DEC_7);
        retrieved = dataStore.getDeliveries(dateRange)
                             .collect(Collectors.toList());
        // And again verify we retrieve those we expected.
        Assert.assertEquals(expectedDeliveries, retrieved);

        // And now verify that if we use a broad range, we can retrieve all of them
        dateRange = new DateRange(NOV_1, DEC_20);
        retrieved = dataStore.getDeliveries(dateRange)
                             .collect(Collectors.toList());
        // And again verify we retrieve those we expected.
        Assert.assertEquals(ALL_DELIVERIES, retrieved);


    }

    @Test
    public void testNoDeliveryRecordsInRange() {
        // We have no deliveries this far into the future
        DateRange dateRange = new DateRange(DEC_23_2025, JAN_23_2026);
        List<DeliveryRecord> retrieved = dataStore.getDeliveries(dateRange)
                                                  .collect(Collectors.toList());
        Assert.assertTrue(retrieved.isEmpty());
    }
}