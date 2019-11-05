package io.oscoin.util;

/**
 * Helper class that maintains a mutable counter whose value can be incremented and queried.
 */
public class Counter {

    private long count;

    public Counter() {
        count = 0;
    }

    public long getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
    }
}
