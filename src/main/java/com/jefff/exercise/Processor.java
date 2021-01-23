package com.jefff.exercise;

import com.jefff.exercise.collection.PaddedArrayList;
import com.jefff.exercise.io.input.LineStream;
import com.jefff.exercise.model.Parser;
import com.jefff.exercise.model.PlacementCount;
import com.jefff.exercise.persistence.DataStore;
import com.jefff.exercise.service.ReportingService;
import com.jefff.exercise.utility.ArgParser;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class Processor {
    private final Parser parser;
    private final LineStream placementInputStream;
    private final LineStream deliveryInputStream;
    private final LineStream queryInputStream;
    private final DataStore dataStore;
    private ReportingService reportingService;


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
        ReportingService reportingService = new ReportingService(dataStore);
        Processor processor = new Processor(parser,
                                            placementInputStream,
                                            deliveryInputStream,
                                            queryInputStream,
                                            dataStore,
                                            reportingService);

        processor.process();
    }

    public Processor(Parser parser,
                     LineStream placementInputStream,
                     LineStream deliveryInputStream,
                     LineStream queryInputStream,
                     DataStore dataStore,
                     ReportingService reportingService) {

        this.parser = parser;
        this.placementInputStream = placementInputStream;
        this.deliveryInputStream = deliveryInputStream;
        this.queryInputStream = queryInputStream;
        this.dataStore = dataStore;
        this.reportingService = reportingService;
    }


    public void process() {
        persistData();
        final PaddedArrayList<PlacementCount> placementCounts = reportingService.generatePrimaryReport();
        reportingService.printPrimaryReport(placementCounts);
        reportingService.generateDateQueryReponseStream(queryInputStream);


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


}
