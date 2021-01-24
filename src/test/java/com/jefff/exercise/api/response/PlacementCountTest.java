package com.jefff.exercise.api.response;

import com.jefff.exercise.utility.Parser;
import com.jefff.exercise.utility.ParserTest;
import com.jefff.exercise.api.response.PlacementCount;
import com.jefff.exercise.entity.PlacementRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlacementCountTest {
    PlacementCount placementCount;

    @Before
    public void testSetup() {
        PlacementRecord placementRecord = new Parser(false).parsePlacement(ParserTest.SPORTS_PLACEMENT_LINE, 2);
        placementCount = new PlacementCount(placementRecord);
        placementCount.setImpressionCount(1083566);
        placementCount.incrementImpressionCount(10);
    }

    @Test
    public void testToString() {
        String expected = "Sports (11/1/2020-11/30/2020): 1,083,576 impressions @ $5 CPM = $5,418";
        Assert.assertEquals(expected, placementCount.toString());
    }

}