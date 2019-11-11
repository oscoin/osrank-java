package io.oscoin.util;

public class Timer {

    public static long getElapsedSeconds(Runnable block) {
        long startTimestamp = System.currentTimeMillis();

        block.run();

        long endTimestamp = System.currentTimeMillis();
        long totalTime = endTimestamp - startTimestamp;

        // return elapsed time in seconds
        return totalTime / 1000;
    }
}
