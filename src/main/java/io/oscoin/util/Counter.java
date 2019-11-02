package io.oscoin.util;

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
