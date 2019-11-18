package io.oscoin.util;

import io.oscoin.graph.AccountNode;
import io.oscoin.graph.ProjectNode;
import org.junit.Test;

public class CounterTest {

    @Test
    public void testIncrementAndGet() {

        Counter counter = new Counter();

        assert(counter.getCount() == 0);

        counter.incrementCount();

        assert(counter.getCount() == 1);

        counter.incrementCount();

        assert(counter.getCount() == 2);
    }
}
