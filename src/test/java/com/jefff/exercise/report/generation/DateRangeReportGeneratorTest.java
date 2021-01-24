package com.jefff.exercise.report.generation;

import com.jefff.exercise.api.request.DateRange;
import com.jefff.exercise.api.response.DateRangeQueryResponse;
import com.jefff.exercise.entity.DeliveryRecord;
import com.jefff.exercise.entity.PlacementRecord;
import com.jefff.exercise.persistence.DataStore;
import com.jefff.exercise.utility.FieldMapper;
import com.jefff.exercise.utility.Parser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class DateRangeReportGeneratorTest {

    Parser parser = new Parser(false);
    DataStore dataStore;
    DateRange coveredRange = parser.parseDateRange("11/22/2020-12/5/2020", 1);
    DateRange uncoveredRange = parser.parseDateRange("11/22/2025-12/5/2025", 1);

    public static final int GOOD_DELIVERY1_IMPRESSIONS = 35123;
    public static final int GOOD_DELIVERY2_IMPRESSIONS = 35124;
    public static final int BAD_DELIVERY1_IMPRESSIONS = 35125;
    public static final int BAD_DELIVERY2_IMPRESSIONS = 35126;

    // 2 good delivery records
    DeliveryRecord goodDelivery1 = new DeliveryRecord(3, FieldMapper.toDate("11/25/2020"), GOOD_DELIVERY1_IMPRESSIONS);
    DeliveryRecord goodDelivery2 = new DeliveryRecord(3, FieldMapper.toDate("11/28/2020"), GOOD_DELIVERY2_IMPRESSIONS);

    // a bad delivery record since it has a date that is not part of placement '3'
    DeliveryRecord badDelivery1 = new DeliveryRecord(3, FieldMapper.toDate("11/25/2025"), BAD_DELIVERY1_IMPRESSIONS);
    // a bad delivery record since there is no placement with id: 3421
    DeliveryRecord badDelivery2 = new DeliveryRecord(3421, FieldMapper.toDate("11/25/2020"), BAD_DELIVERY2_IMPRESSIONS);

    PlacementRecord placementRecord = parser.parsePlacement("3 ,Travel  ,11/1/20,11/30/20,3", 3);
    List<DeliveryRecord> coveredDeliveries = Arrays.asList(goodDelivery1, badDelivery1, badDelivery2, goodDelivery2);
    List<DeliveryRecord> uncoveredDeliveries = new ArrayList<>();
    DateRangeReportGenerator dateRangeReportGenerator;

    DateRangeQueryResponse expectedUncoveredRangeResponse;
    DateRangeQueryResponse expectedCoveredRangeResponse;


    @Before
    public void testSetup() {
        dataStore = mock(DataStore.class);
        when(dataStore.getPlacementRecord(3)).thenReturn(placementRecord);
        when(dataStore.getDeliveries(coveredRange)).thenReturn(coveredDeliveries.stream());
        when(dataStore.getDeliveries(uncoveredRange)).thenReturn(uncoveredDeliveries.stream());
        dateRangeReportGenerator = new DateRangeReportGenerator(dataStore);

        expectedUncoveredRangeResponse = new DateRangeQueryResponse(uncoveredRange);

        // Build the expected response for our coveredRange. Note that it only includes the impressions
        // from our 2 good deliveries
        expectedCoveredRangeResponse = new DateRangeQueryResponse(coveredRange);
        expectedCoveredRangeResponse.add(GOOD_DELIVERY1_IMPRESSIONS, 3);
        expectedCoveredRangeResponse.add(GOOD_DELIVERY2_IMPRESSIONS, 3);

    }

    @Test
    public void testProcessDateQueryWithRangeThatHasZeroDeliveries() {
        DateRangeQueryResponse actual = dateRangeReportGenerator.processDateQuery(uncoveredRange);
        Assert.assertEquals(expectedUncoveredRangeResponse, actual);
    }

    @Test
    public void testProcessDateQueryWithRangeThatHasTwoGoodDeliveriesAndTwoBadDeliveries() {
        DateRangeQueryResponse actual = dateRangeReportGenerator.processDateQuery(coveredRange);
        Assert.assertEquals(expectedCoveredRangeResponse, actual);
    }

    @Test
    public void testGenerateRangeQueryResponseStream() {
        Stream<DateRange> queryStream = Stream.of(uncoveredRange, coveredRange);
        final List<DateRangeQueryResponse> actualResponseList =
                dateRangeReportGenerator.generateRangeQueryResponseStream(queryStream)
                                        .collect(Collectors.toList());
        final List<DateRangeQueryResponse> expectedResponseList = Arrays.asList(expectedUncoveredRangeResponse,
                                                                                expectedCoveredRangeResponse);
        Assert.assertEquals(expectedResponseList, actualResponseList);
    }

}