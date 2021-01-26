package com.jefff.exercise.utility;

import org.junit.Assert;
import org.junit.Test;

public class DynamicArrayTest {
    @Test
    public void testScenario() {
        DynamicArray<Integer> dynamicArray = new DynamicArray<>();
        // Create an ArrayList with gaps.
        dynamicArray.add(1, 1);
        dynamicArray.add(4, 4);
        dynamicArray.add(7, 7);

        Assert.assertEquals(8, dynamicArray.size());
        // See that we can retrieve the elements as expected
        for (int i = 0; i < dynamicArray.size(); i++) {
            switch (i) {
                case 1:
                case 4:
                case 7:
                    Assert.assertEquals(i, dynamicArray.get(i).intValue());
                    break;
                default:
                    Assert.assertNull(dynamicArray.get(i));
            }
        }

        // test our resilience to indexes that are too large.
        Assert.assertNull(dynamicArray.get(11232));
    }

}