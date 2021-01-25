package com.jefff.exercise;

import com.jefff.exercise.api.request.DateRange;
import com.jefff.exercise.persistence.DataStore;
import com.jefff.exercise.report.ReportingService;
import com.jefff.exercise.report.generation.DateRangeReportGenerator;
import com.jefff.exercise.report.generation.PlacementCountReportGenerator;
import com.jefff.exercise.report.printing.DateRangeReportPrinter;
import com.jefff.exercise.report.printing.PlacementCountReportPrinter;
import com.jefff.exercise.utility.ArgParser;
import com.jefff.exercise.utility.LineStream;
import com.jefff.exercise.utility.Parser;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
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
        try {
            Processor processor = createProcessor(argParser.getPlacementFileName(),
                                                  argParser.getDeliveryFileName(),
                                                  argParser.getQueryFileName(),
                                                  new PrintWriter(System.out, true));
            processor.process();
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("File Access problems! Exiting.");
        }
    }

    public static Processor createProcessor(String placementFileName,
                                            String deliveryFileName,
                                            String queryFileName,
                                            PrintWriter printWriter) throws Exception {
        LineStream queryInputStream = null;
        LineStream placementInputStream = LineStream.getLineStream(placementFileName);
        LineStream deliveryInputStream = LineStream.getLineStream(deliveryFileName);
        if (queryFileName != null && queryFileName.length() > 0) {
            queryInputStream = LineStream.getLineStream(queryFileName);
        }

        Parser parser = new Parser(true);
        DataStore dataStore = new DataStore();
        ReportingService reportingService = new ReportingService(new PlacementCountReportGenerator(dataStore),
                                                                 new PlacementCountReportPrinter(printWriter),
                                                                 new DateRangeReportGenerator(dataStore),
                                                                 new DateRangeReportPrinter(printWriter));
        Processor processor = new Processor(parser,
                                            placementInputStream,
                                            deliveryInputStream,
                                            queryInputStream,
                                            dataStore,
                                            reportingService);
        return processor;

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
        reportingService.handlePlacementCountReport();

        // And now process our range query requests
        if (queryLineStream != null) {
            final Stream<DateRange> dateRangeStream = queryLineStream.getStream()
                                                                     .map(line -> parser.parseDateRange(line.getText(), line.getLineNumber()))
                                                                     .filter(Objects::nonNull);
            reportingService.handleDateRangeReport(dateRangeStream);
        }
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
