package com.jefff.exercise.utility;

import com.jefff.exercise.utility.FieldMapper;
import org.junit.Assert;
import org.junit.Test;

public class FieldMapperTest {
    @Test
    public void testFormatDate() {
        Assert.assertEquals("3/1/2020", FieldMapper.formatDate(FieldMapper.toDate(2020, 3, 1)));
        Assert.assertEquals("11/7/2020", FieldMapper.formatDate(FieldMapper.toDate(2020, 11, 7)));
        Assert.assertEquals("11/17/2020", FieldMapper.formatDate(FieldMapper.toDate(2020, 11, 17)));
    }

    @Test
    public void roundDouble() {
        Assert.assertEquals(5418, FieldMapper.roundDouble(5417.601));
        Assert.assertEquals(5418, FieldMapper.roundDouble(5417.5));
        Assert.assertEquals(5418, FieldMapper.roundDouble(5418.01));
        Assert.assertEquals(5417, FieldMapper.roundDouble(5417.4999));
    }

    @Test
    public void testParseDate() {
        Assert.assertEquals(FieldMapper.toDate(2020, 3, 1), FieldMapper.toDate("3/1/20"));
        Assert.assertEquals(FieldMapper.toDate(2020, 3, 1), FieldMapper.toDate("3/1/2020"));
        Assert.assertEquals(FieldMapper.toDate(2020, 3, 1), FieldMapper.toDate("3/01/2020"));
        Assert.assertEquals(FieldMapper.toDate(2020, 3, 1), FieldMapper.toDate("03/01/2020"));
    }

    @Test
    public void testNormalizeDateString() {
        Assert.assertEquals("03/01/2020", FieldMapper.normalizeDateString("3/1/20"));
        Assert.assertEquals("03/01/2020", FieldMapper.normalizeDateString("3/1/2020"));
        Assert.assertEquals("03/01/2020", FieldMapper.normalizeDateString("3/01/2020"));
        Assert.assertEquals("03/01/2020", FieldMapper.normalizeDateString("03/01/2020"));
    }

    @Test
    public void testNumberFormat() {
        Assert.assertEquals("5,148", FieldMapper.formatNumber(5148));
        Assert.assertEquals("5", FieldMapper.formatNumber(5));
    }

}