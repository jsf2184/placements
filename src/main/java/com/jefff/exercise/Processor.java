package com.jefff.exercise;

import com.jefff.exercise.utility.PaddedArrayList;
import com.jefff.exercise.io.input.LineStream;
import com.jefff.exercise.api.response.DateRangeQueryResponse;
import com.jefff.exercise.utility.Parser;
import com.jefff.exercise.api.response.PlacementCount;
import com.jefff.exercise.persistence.DataStore;
import com.jefff.exercise.service.ReportingService;
import com.jefff.exercise.utility.ArgParser;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
public class Processor {
    private final Parser parser;
    private final LineStream placementLineStream;
    private final LineStream deliveryLineStream;
    private final LineStream queryLineStream;
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

        Parser parser = new Parser(true);
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
                     LineStream placementLineStream,
                     LineStream deliveryLineStream,
                     LineStream queryLineStream,
                     DataStore dataStore,
                     ReportingService reportingService) {

        this.parser = parser;
        this.placementLineStream = placementLineStream;
        this.deliveryLineStream = deliveryLineStream;
        this.queryLineStream = queryLineStream;
        this.dataStore = dataStore;
        this.reportingService = reportingService;
    }


    public void process() {
        persistData();
        // Create our standard report (Counts per Placement)
        final PaddedArrayList<PlacementCount> placementCounts = reportingService.generatePrimaryReport();
        // And print the report
        reportingService.printPrimaryReport(placementCounts);

        // And now process our range query requests

        Stream<DateRangeQueryResponse> rangeQueryResponseStream = reportingService.generateDateQueryReponseStream(queryLineStream);
        // And print the rangeQuery responses


    }

    public void persistData() {
        placementLineStream.getStream()
                           .map(line -> parser.parsePlacement(line.getText(), line.getLineNumber()))
                           .filter(Objects::nonNull)
                           .forEach(dataStore::add);

        deliveryLineStream.getStream()
                          .map(line -> parser.parseDelivery(line.getText(), line.getLineNumber()))
                          .filter(Objects::nonNull)
                          .forEach(dataStore::add);
    }


}
