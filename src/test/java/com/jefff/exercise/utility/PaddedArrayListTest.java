package com.jefff.exercise.utility;

import com.jefff.exercise.utility.PaddedArrayList;
import org.junit.Assert;
import org.junit.Test;

public class PaddedArrayListTest {
    @Test
    public void testScenario() {
        PaddedArrayList<Integer> list = new PaddedArrayList<>();
        // Create an ArrayList with gaps.
        list.add(1, 1);
        list.add(4, 4);
        list.add(7, 7);

        Assert.assertEquals(8, list.size());
        // See that we can retrieve the elements as expected
        for (int i = 0; i < list.size(); i++) {
            switch (i) {
                case 1:
                case 4:
                case 7:
                    Assert.assertEquals(i, list.get(i).intValue());
                    break;
                default:
                    Assert.assertNull(list.get(i));
            }
        }

        // test our resilience to indexes that are too large.
        Assert.assertNull(list.get(11232));
    }

}