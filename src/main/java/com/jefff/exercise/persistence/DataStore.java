package com.jefff.exercise.persistence;

import com.jefff.exercise.utility.PaddedArrayList;
import com.jefff.exercise.api.request.DateRange;
import com.jefff.exercise.entity.DeliveryPlacementRecord;
import com.jefff.exercise.entity.DeliveryRecord;
import com.jefff.exercise.entity.PlacementRecord;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class DataStore {

    TreeMap<LocalDate, List<DeliveryRecord>> deliveryMapByDate;
    PaddedArrayList<PlacementRecord> placementRecords;

    public DataStore() {
        deliveryMapByDate = new TreeMap<>();
        placementRecords = new PaddedArrayList<>();
    }


    public void add(PlacementRecord placement) {
        // Add the PlacementRecord at the specified index.
        placementRecords.add(placement.getId(), placement);
    }

    public PlacementRecord getPlacementRecord(int i) {
        return placementRecords.get(i);
    }

    public Stream<PlacementRecord> getPlacements() {
        return placementRecords.stream().filter(Objects::nonNull);
    }

    /**
     * saveDelivery()
     * Place the deliveryRecord on a list within the map for this specfic date
     *
     * @param delivery - the record to store
     */
    public void add(DeliveryRecord delivery) {
        // From the map, get the list for this date. If there is not such a list, create it
        // and store it in the map.
        final List<DeliveryRecord> dateList = deliveryMapByDate.computeIfAbsent(delivery.getDate(), date -> new ArrayList<>());
        // Add it to the list.
        dateList.add(delivery);
    }

    /**
     * getDeliveries()
     *
     * @return All deliveries within the map
     */
    public Stream<DeliveryRecord> getDeliveries() {
        return deliveryMapByDate.values()
                                .stream()
                                .flatMap(Collection::stream);

    }

    public Stream<DeliveryPlacementRecord> getDeliveryPlacements() {
        return getDeliveries()
                .map(d -> new DeliveryPlacementRecord(d, getPlacementRecord(d.getPlacementId())));
    }

    /**
     * @param dateRange - date reanges of interest
     * @return - All deliveries within a range of dates (inclusive).
     */
    public Stream<DeliveryRecord> getDeliveries(DateRange dateRange) {
        return deliveryMapByDate.subMap(dateRange.getStart(), true, dateRange.getEnd(), true)
                                .values()
                                .stream()
                                .flatMap(Collection::stream);
    }


}
