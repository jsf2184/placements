package com.jefff.exercise;

import com.jefff.exercise.io.input.LineStream;
import com.jefff.exercise.model.Parser;
import com.jefff.exercise.utility.ArgParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Processor {
    public static void main(String[] args) {
        ArgParser argParser = new ArgParser(args);
        if (!argParser.process()) {
            return;
        }

        LineStream placementLineStream;
        LineStream deliveryLineStream;
        LineStream queryLineStream = null;

        try {
            placementLineStream = LineStream.getLineStream(argParser.getPlacementFileName());
            deliveryLineStream = LineStream.getLineStream(argParser.getDeliveryFileName());
            final String queryFileName = argParser.getQueryFileName();
            if (queryFileName != null && queryFileName.length() > 0) {
                queryLineStream = LineStream.getLineStream(queryFileName);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("File Access problems! Exiting.");
            return;
        }

        Parser parser = new Parser();

        log.info("Placements");
        placementLineStream.getStream().forEach(l -> log.info("{}", l));
        log.info("Deliveries");
        deliveryLineStream.getStream().forEach(l -> log.info("{}", l));

    }
}
