package io.oscoin.graph;

import io.oscoin.util.OrderedPair;
import java.util.ArrayList;
import java.util.List;

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

    public void normalizeEdgeProbabilities() {

        // Find the starting probability total
        double startingTotal = getSumOfEdgeProbabilities();

        // Now we need to divide each of the probabilities by this, so that they will sum to 1
        for (OrderedPair<Double,Integer> oneProbability : connectedNodeProbs) {
            oneProbability.left = oneProbability.left / startingTotal;
        }
    }

    public Integer walkToNextNodeId(double probability) {

        for (OrderedPair<Double,Integer> oneProbability : connectedNodeProbs) {
            probability -= oneProbability.left;
            if (probability <= 0) return oneProbability.right;
        }

        throw new RuntimeException("Edge probability values sum to less than 1. Perhaps they were not normalized? " + nodeId);
    }

    public abstract void buildConnectedNodeProbs();
}
