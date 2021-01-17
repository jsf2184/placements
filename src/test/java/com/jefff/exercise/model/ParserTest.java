package com.jefff.exercise.model;

import com.jefff.exercise.FieldMapper;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    Parser parser = new Parser();

    @Test
    public void testGoodImpressionParse() {
        ImpressionRecord expected = new ImpressionRecord(3, FieldMapper.toDate(2020, 1, 24), 2345);
        final ImpressionRecord actual = parser.parseImpression("3,01/24/2020,2345", 1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBadImpressionParse() {
        Assert.assertNull(parser.parseImpression(null, 1));
        Assert.assertNull(parser.parseImpression("", 2));
        Assert.assertNull(parser.parseImpression(",,", 3));
        Assert.assertNull(parser.parseImpression("3,,", 4));
        Assert.assertNull(parser.parseImpression("3,01/24/2020,", 5));
        Assert.assertNull(parser.parseImpression("3,01/24/202x,2345", 6));
        Assert.assertNull(parser.parseImpression("3,01/24/2020,abc", 7));
        Assert.assertNull(parser.parseImpression("abc,01/24/2020,2345", 8));
        Assert.assertNull(parser.parseImpression("3,01/24/2020,2345,4", 9));
    }

    @Test
    public void testGoodPlacementParse() {
        PlacementRecord expected = new PlacementRecord(1,
                                                       "Sports",
                                                       FieldMapper.toDate(2020, 11, 1),
                                                       FieldMapper.toDate(2020, 11, 30),
                                                       5);
        final PlacementRecord actual = parser.parsePlacement("1,Sports,11/1/20,11/30/20,5", 1);
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
}