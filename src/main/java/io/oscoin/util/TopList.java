package io.oscoin.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection class that maintains a list of "top N" OrderedPairs, based on a sort order using the double value
 * in the right side of the ordered pair.
 *
 */
public class TopList {

    private static OrderedPair.OneSideOrderedPairComparator<Integer,Double> orderedPairComparator =
            new OrderedPair.OneSideOrderedPairComparator<>(false);

    private int maxSize;
    private List<OrderedPair<Integer,Double>> top;

    public TopList(int maxSize) {
        this.maxSize = maxSize;
        top = new ArrayList<>();
    }

    public void tryToAdd(OrderedPair<Integer,Double> candidate) {
        if (top.size() < maxSize) {
            top.add(candidate);
            top.sort(orderedPairComparator);
        } else if (candidate.right.doubleValue() > top.get(0).right.doubleValue()) {
            top.remove(0);
            top.add(candidate);
            top.sort(orderedPairComparator);
        }
    }

    public List<OrderedPair<Integer,Double>> getTop() {
        return top;
    }
}
