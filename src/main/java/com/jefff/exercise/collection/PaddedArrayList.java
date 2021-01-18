package com.jefff.exercise.collection;

import java.util.ArrayList;
import java.util.Collection;

public class PaddedArrayList<T> extends ArrayList<T> {
    public PaddedArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    public PaddedArrayList() {
    }

    public PaddedArrayList(Collection<? extends T> c) {
        super(c);
    }

    /**
     * add()
     * <p>
     * This override version of add() padds out the Array list with null entries if the index is > size
     * before adding the element at the specified index.
     *
     * @param index   - Add the element at this index
     * @param element - The element to add
     */
    @Override
    public void add(int index, T element) {
        int size = this.size();
        if (index <= size) {
            super.add(index, element);
        }
        for (int i = size; i < index; i++) {
            super.add(null);
        }
        super.add(element);
    }

    /**
     * get()
     * <p>
     * A more tolerant get() that  returns null instead of throwing an exception for an
     * out of bounds index.
     *
     * @param index - index to retrieve
     * @return - element at that index or null if nothing at that index
     */
    @Override
    public T get(int index) {
        return index < size() ? super.get(index) : null;
    }
}
