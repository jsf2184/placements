package com.jefff.exercise.model;

import com.jefff.exercise.utility.FieldMapper;
import org.junit.Assert;
import org.junit.Test;

public class PlacementRecordTest {

    @Test
    public void includesDate() {
        PlacementRecord placementRecord = new PlacementRecord(1,
                                                              "Sports",
                                                              FieldMapper.toDate(2020, 12, 1),
                                                              FieldMapper.toDate(2020, 12, 31),
                                                              5);
        Assert.assertTrue(placementRecord.includesDate(FieldMapper.toDate(2020, 12, 1)));
        Assert.assertTrue(placementRecord.includesDate(FieldMapper.toDate(2020, 12, 31)));
        Assert.assertTrue(placementRecord.includesDate(FieldMapper.toDate(2020, 12, 15)));
        Assert.assertFalse(placementRecord.includesDate(FieldMapper.toDate(2020, 11, 15)));
        Assert.assertFalse(placementRecord.includesDate(FieldMapper.toDate(2021, 1, 15)));
    }
}