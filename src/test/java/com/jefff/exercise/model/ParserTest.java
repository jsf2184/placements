package com.jefff.exercise.model;

import com.jefff.exercise.utility.FieldMapper;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    Parser parser = new Parser();
    public static final String SPORTS_PLACEMENT_LINE = "1,Sports,11/1/20,11/30/20,5";

    @Test
    public void testGoodImpressionParse() {
        DeliveryRecord expected = new DeliveryRecord(3, FieldMapper.toDate(2020, 1, 24), 2345);
        final DeliveryRecord actual = parser.parseDelivery("3,01/24/2020,2345", 1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBadImpressionParse() {
        Assert.assertNull(parser.parseDelivery(null, 1));
        Assert.assertNull(parser.parseDelivery("", 2));
        Assert.assertNull(parser.parseDelivery(",,", 3));
        Assert.assertNull(parser.parseDelivery("3,,", 4));
        Assert.assertNull(parser.parseDelivery("3,01/24/2020,", 5));
        Assert.assertNull(parser.parseDelivery("3,01/24/202x,2345", 6));
        Assert.assertNull(parser.parseDelivery("3,01/24/2020,abc", 7));
        Assert.assertNull(parser.parseDelivery("abc,01/24/2020,2345", 8));
        Assert.assertNull(parser.parseDelivery("3,01/24/2020,2345,4", 9));
    }


    @Test
    public void testGoodPlacementParse() {
        PlacementRecord expected = new PlacementRecord(1,
                                                       "Sports",
                                                       FieldMapper.toDate(2020, 11, 1),
                                                       FieldMapper.toDate(2020, 11, 30),
                                                       5);
        final PlacementRecord actual = parser.parsePlacement(SPORTS_PLACEMENT_LINE, 1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBadPlacementParse() {
        Assert.assertNull(parser.parsePlacement("1,Sports,11/1/20,11/30/20", 1));
        Assert.assertNull(parser.parsePlacement("1,Sports,11/1/20,,5", 2));
        Assert.assertNull(parser.parsePlacement("1,Sports,,11/30/20,5", 3));
        Assert.assertNull(parser.parsePlacement("1,,11/1/20,11/30/20,5", 4));
        Assert.assertNull(parser.parsePlacement(",Sports,11/1/20,11/30/20,5", 1));

        Assert.assertNull(parser.parsePlacement("1,Sports,11/1/20,5", 6));
        Assert.assertNull(parser.parsePlacement("1,Sports,11/30/20,5", 7));
        Assert.assertNull(parser.parsePlacement("1,11/1/20,11/30/20,5", 8));
        Assert.assertNull(parser.parsePlacement("Sports,11/1/20,11/30/20,5", 9));

        Assert.assertNull(parser.parsePlacement("1a,Sports,11/1/20,11/30/20,5", 10));
        Assert.assertNull(parser.parsePlacement("1,Sports,11/x/20,11/30/20,5", 11));
        Assert.assertNull(parser.parsePlacement("1,Sports,11/1/20,11/34/20,5", 12));
        Assert.assertNull(parser.parsePlacement("1,Sports,11/1/20,11/30/20,x", 13));
    }

    @Test
    public void testGoodDateRangeParse() {
        DateRange expected = new DateRange(FieldMapper.toDate(2020, 11, 1), FieldMapper.toDate(2020, 11, 30));
        DateRange actual = parser.parseDateRange("11/1/2020-11/30/2020", 1);
        Assert.assertEquals(expected, actual);
        actual = parser.parseDateRange("11/01/20-11/30/2020", 1);
        Assert.assertEquals(expected, actual);
    }

}