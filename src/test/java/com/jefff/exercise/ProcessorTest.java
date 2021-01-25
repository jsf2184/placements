package com.jefff.exercise;

import com.jefff.exercise.utility.ArgParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class ProcessorTest {
    @Test
    public void integrationTest() throws Exception {
        StringWriter stringWriter = new StringWriter();

        // Create a processor that writes its output into a StringWriter rather than stdout
        // so we can examine the output to see that it is what we expect it to be.
        //
        Processor processor = Processor.createProcessor(ArgParser.DEFAULT_PLACEMENTS_FILE,
                                                        ArgParser.DEFAULT_DELIVERY_FILE,
                                                        "dateQuery.txt",
                                                        new PrintWriter(stringWriter));
        processor.process();
        String actualReport = stringWriter.toString();

        String expected =
                "\n\nPrimary Report With PlacementCounts follows...\n" +
                "Sports (11/1/2020-11/30/2020): 1,083,576 impressions @ $5 CPM = $5,418\n" +
                "Business (12/1/2020-12/31/2020): 1,607,958 impressions @ $8 CPM = $12,864\n" +
                "Travel (11/1/2020-11/30/2020): 1,035,966 impressions @ $3 CPM = $3,108\n" +
                "Politics (12/1/2020-12/31/2020): 1,529,821 impressions @ $6 CPM = $9,179\n" +
                "\n" +
                "DateRangeQueryReport follows...\n" +
                "Total (11/22/2020-12/5/2020): 1,126,785 impressions, $6,061\n";

        Assert.assertEquals(expected, actualReport);
    }
}
