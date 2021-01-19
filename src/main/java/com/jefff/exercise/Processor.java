package com.jefff.exercise;

import com.jefff.exercise.collection.PaddedArrayList;
import com.jefff.exercise.io.input.LineStream;
import com.jefff.exercise.model.Parser;
import com.jefff.exercise.model.PlacementRecord;
import com.jefff.exercise.persistence.DataStore;
import com.jefff.exercise.utility.ArgParser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
public class Processor {
    private final Parser parser;
    private final LineStream placementInputStream;
    private final LineStream deliveryInputStream;
    private final LineStream queryInputStream;
    private final DataStore dataStore;


    public static void main(String[] args) {
        ArgParser argParser = new ArgParser(args);
        if (!argParser.process()) {
            return;
        }

        LineStream placementInputStream;
        LineStream deliveryInputStream;
        LineStream queryInputStream = null;

        try {
            placementInputStream = LineStream.getLineStream(argParser.getPlacementFileName());
            deliveryInputStream = LineStream.getLineStream(argParser.getDeliveryFileName());
            final String queryFileName = argParser.getQueryFileName();
            if (queryFileName != null && queryFileName.length() > 0) {
                queryInputStream = LineStream.getLineStream(queryFileName);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("File Access problems! Exiting.");
            return;
        }

        Parser parser = new Parser();
        DataStore dataStore = new DataStore();
        Processor processor = new Processor(parser,
                                            placementInputStream,
                                            deliveryInputStream,
                                            queryInputStream,
                                            dataStore);

        processor.process();
    }

    public Processor(Parser parser,
                     LineStream placementInputStream,
                     LineStream deliveryInputStream,
                     LineStream queryInputStream,
                     DataStore dataStore) {

        this.parser = parser;
        this.placementInputStream = placementInputStream;
        this.deliveryInputStream = deliveryInputStream;
        this.queryInputStream = queryInputStream;
        this.dataStore = dataStore;
    }


    public void process() {
        persistData();
        final PaddedArrayList<PlacementCount> placementCounts = generatePrimaryReport();

    }

    public void persistData() {
        placementInputStream.getStream()
                            .map(line -> parser.parsePlacement(line.getText(), line.getLineNumber()))
                            .filter(Objects::nonNull)
                            .forEach(dataStore::add);

        deliveryInputStream.getStream()
                           .map(line -> parser.parseDelivery(line.getText(), line.getLineNumber()))
                           .filter(Objects::nonNull)
                           .forEach(dataStore::add);
    }

    @Data
    public static class PlacementCount {
        PlacementRecord placement;
        long count;

        public PlacementCount(PlacementRecord placement) {
            this.placement = placement;
            this.count = 0;
        }

        public void incrementCount(int delta) {
            count += delta;
        }

        public int getId() {
            return placement.getId();
        }

        public boolean includesDate(LocalDate date) {
            return placement.includesDate(date);
        }
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
                         placementCount.incrementCount(d.getNumImpressions());
                     }
                 });
        return result;
    }

}
