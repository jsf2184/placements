package com.jefff.exercise.report.generation;

import com.jefff.exercise.api.request.DateRange;
import com.jefff.exercise.api.response.DateRangeQueryResponse;
import com.jefff.exercise.entity.DeliveryRecord;
import com.jefff.exercise.entity.PlacementRecord;
import com.jefff.exercise.persistence.DataStore;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

@Slf4j
public class DateRangeReportGenerator {
    DataStore dataStore;

    public DateRangeReportGenerator(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * generateRangeQueryResponseStream()
     * <p>
     * This method handles all the DateRange queries presented to the application by an input stream
     * of DateRange queries. In response, it returns a corresponding stream of DateRangeQueryResponses,
     * one for each DateRange in the query stream.
     *
     * @param queryStream - A stream of DateRange queries.
     * @return - An output stream of DateRangeQueryResponses, one for every DateRange on the queryStream
     */
    public Stream<DateRangeQueryResponse> generateRangeQueryResponseStream(Stream<DateRange> queryStream) {
        return queryStream.map(this::processDateQuery);
    }


    /**
     * processDateQuery()
     * <p>
     * process a singe DateRange query, producing a corresponding DateRangeQueryResponse for each
     *
     * @param dateRange - The DateRange that defines our Deliveries of interest.
     * @return - A DateRangeQueryResponse for that DateRange.
     */
    DateRangeQueryResponse processDateQuery(DateRange dateRange) {
        DateRangeQueryResponse result = new DateRangeQueryResponse(dateRange);
        final Stream<DeliveryRecord> deliveries = dataStore.getDeliveries(dateRange);
        deliveries.forEach(delivery -> {
            final PlacementRecord placementRecord = dataStore.getPlacementRecord(delivery.getPlacementId());
            if (placementRecord == null || !placementRecord.includesDate(delivery.getDate())) {
                log.warn("Could not find suitable placement for deliveryRecord: {}", delivery.toString());
            } else {
                result.add(delivery.getNumImpressions(), placementRecord.getCpm());
            }
        });
        return result;
    }
}
