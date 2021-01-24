package com.jefff.exercise.utility;

import com.jefff.exercise.utility.ArgParser;
import org.junit.Assert;
import org.junit.Test;

public class ArgParserTest {

    public static final String OVERWRITE_DELIVERY_FILE = "ovw_delivery.csv";
    public static final String OVERWRITE_PLACEMENTS_FILE = "ovw_placements.csv";
    public static final String OVERWRITE_QUERY_FILE = "ovw_query.txt";

    @Test
    public void testProcess() {
        // test no args defaulting
        runParseTest(new String[0],
                     ArgParser.DEFAULT_DELIVERY_FILE,
                     ArgParser.DEFAULT_PLACEMENTS_FILE,
                     null,
                     true);

        // test with all args specified
        runParseTest(new String[]{
                             "-df", OVERWRITE_DELIVERY_FILE,
                             "-pf", OVERWRITE_PLACEMENTS_FILE,
                             "-qf", OVERWRITE_QUERY_FILE},
                     OVERWRITE_DELIVERY_FILE,
                     OVERWRITE_PLACEMENTS_FILE,
                     OVERWRITE_QUERY_FILE,
                     true);


        // test with all -h specified
        runParseTest(new String[]{
                             "-df", OVERWRITE_DELIVERY_FILE,
                             "-pf", OVERWRITE_PLACEMENTS_FILE,
                             "-qf", OVERWRITE_QUERY_FILE,
                             "-h"},
                     OVERWRITE_DELIVERY_FILE,
                     OVERWRITE_PLACEMENTS_FILE,
                     OVERWRITE_QUERY_FILE,
                     false);

        // test with all extra arg specified
        runParseTest(new String[]{
                             "-df", OVERWRITE_DELIVERY_FILE,
                             "-pf", OVERWRITE_PLACEMENTS_FILE,
                             "-qf", OVERWRITE_QUERY_FILE,
                             "-extra"},

                     OVERWRITE_DELIVERY_FILE,
                     OVERWRITE_PLACEMENTS_FILE,
                     OVERWRITE_QUERY_FILE,
                     false);

        // test with all dangling arg specified
        runParseTest(new String[]{
                             "-df", OVERWRITE_DELIVERY_FILE,
                             "-pf", OVERWRITE_PLACEMENTS_FILE,
                             "-qf"},

                     OVERWRITE_DELIVERY_FILE,
                     OVERWRITE_PLACEMENTS_FILE,
                     OVERWRITE_QUERY_FILE,
                     false);
    }

    void runParseTest(String[] args,
                      String expectedDeliveryFileName,
                      String expectedPlacementFileName,
                      String expectedQueryFileName,
                      boolean expectSuccess) {
        ArgParser argParser = new ArgParser(args);
        Assert.assertEquals(expectSuccess, argParser.process());
        if (expectSuccess) {
            Assert.assertEquals(expectedDeliveryFileName, argParser.getDeliveryFileName());
            Assert.assertEquals(expectedPlacementFileName, argParser.getPlacementFileName());
            Assert.assertEquals(expectedQueryFileName, argParser.getQueryFileName());
        }
    }

}