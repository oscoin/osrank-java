package io.oscoin.graph;

import io.oscoin.util.OrderedPair;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class representing a single graph node in terms of the types of graphs considered in the Oscoin whitepaper.
 *
 * The member variable connectedNodeProbs maintains a list of OrderedPairs that represent the edges traveling
 * away from the node, along with the relative probabilities that those edges should be followed by a random walk.
 *
 * The methods buildConnectedNodeProbs() and normalizeEdgeProbabilities() are used to build and normalize this list.
 *
 */
public abstract class Node {

    private Integer nodeId;
    private String nodeName;
    protected List<OrderedPair<Double,Integer>> connectedNodeProbs;

    public Node(Integer nodeId) {
        this(nodeId, null);
    }

    public Node(Integer nodeId, String nodeName) {
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        connectedNodeProbs = new ArrayList<>();
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public String getNodeName() { return nodeName; }

    public int getConnectedNodeCount() {
        return connectedNodeProbs.size();
    }

    public double getSumOfEdgeProbabilities() {
        double total = 0d;
        for (OrderedPair<Double,Integer> oneProbability : connectedNodeProbs) {
            total += oneProbability.left;
        }
        return total;
    }

    public Integer walkToNextNodeId(double probability) {

        for (OrderedPair<Double,Integer> oneProbability : connectedNodeProbs) {
            probability -= oneProbability.left;
            if (probability <= 0) return oneProbability.right;
        }

        throw new RuntimeException("Edge probability values sum to less than 1. Perhaps they were not normalized? " + nodeId);
    }

    public abstract void buildConnectedNodeProbs();

    /**
     * Calling this "method normalizes" the edges in connectedNodeProbs so that all of double values in the
     * OrderedPairs in the list sum to 1.
     *
     * NOTE: This method should be called after buildConnectedNodeProbs() has already been called.
     */
    public void normalizeEdgeProbabilities() {

        // Find the starting probability total
        double startingTotal = getSumOfEdgeProbabilities();

        // Now we need to divide each of the probabilities by this, so that they will sum to 1
        for (OrderedPair<Double,Integer> oneProbability : connectedNodeProbs) {
            oneProbability.left = oneProbability.left / startingTotal;
        }
    }
}
