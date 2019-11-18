package io.oscoin.graph;

import io.oscoin.algo.OsrankParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates data and logic for an entire graph of projects and accounts, as defined in the Oscoin whitepaper.
 *
 */
public class Graph {

    private Map<Integer,Node> idToNodeMap;
    private List<Node> allNodes;

    public Graph() {
        this.idToNodeMap = new HashMap<>();
        this.allNodes = new ArrayList<>();
    }

    public Node getNodeById(Integer id) {
        return idToNodeMap.get(id);
    }

    public List<Node> getAllNodes() {
        return allNodes;
    }

    public void addProjectNode(ProjectNode projectNode) {
        idToNodeMap.put(projectNode.getNodeId(), projectNode);
        allNodes.add(projectNode);
    }

    public void addAccountNode(AccountNode accountNode) {
        idToNodeMap.put(accountNode.getNodeId(), accountNode);
        allNodes.add(accountNode);
    }

    public void buildAndNormalizeAllNodes(OsrankParams osrankParams) {
        for (Node node : allNodes) {
            node.buildConnectedNodeProbs(osrankParams);
            node.normalizeEdgeProbabilities();
        }
    }
}
