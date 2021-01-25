package com.jefff.exercise.api.response;

import com.jefff.exercise.api.request.DateRange;
import com.jefff.exercise.utility.Parser;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class DateRangeQueryResponseTest  {

    @Test
    public void testAddAndFormat() {
        Parser parser = new Parser(false);
        DateRange dateRange = parser.parseDateRange("11/22/2020-12/5/2020", 1);
        DateRangeQueryResponse queryResponse = new DateRangeQueryResponse(dateRange);

        queryResponse.add(312176, 3);
        queryResponse.add(318677, 5);
        queryResponse.add(217978, 6);
        queryResponse.add(277954, 8);

        String actualReportLine = queryResponse.formatReportLine();
        String expectedReportLine = "Total (11/22/2020-12/5/2020): 1,126,785 impressions, $6,061";
        Assert.assertEquals(expectedReportLine, actualReportLine);

        Assert.assertEquals(312176 + 318677 + 217978 + 277954, queryResponse.getImpressions());
        Assert.assertEquals( (double) 312176 * 3 +
                            318677 * 5 +
                            217978 * 6 +
                            277954 * 8, queryResponse.getCost(), .01);
    }


}