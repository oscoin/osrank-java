package io.oscoin.util;

import org.junit.Test;

import java.util.List;

public class TopListTest {

    @Test
    public void testTopList() {

        TopList topList = new TopList(3);

        OrderedPair<Integer,Double> one = new OrderedPair<>(5, 1d);
        OrderedPair<Integer,Double> two = new OrderedPair<>(4, 2d);
        OrderedPair<Integer,Double> three = new OrderedPair<>(3, 3d);
        OrderedPair<Integer,Double> four = new OrderedPair<>(2, 4d);
        OrderedPair<Integer,Double> five = new OrderedPair<>(1, 5d);

        topList.tryToAdd(one);
        topList.tryToAdd(two);
        topList.tryToAdd(three);
        topList.tryToAdd(four);
        topList.tryToAdd(five);

        assert(topList.getSize() == 3);

        List<OrderedPair<Integer,Double>> list = topList.getTop();
        assert(list.get(0).equals(new OrderedPair<Integer,Double>(3,3d)));
        assert(list.get(1).equals(new OrderedPair<Integer,Double>(2,4d)));
        assert(list.get(2).equals(new OrderedPair<Integer,Double>(1,5d)));
    }
}
