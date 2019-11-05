package io.oscoin.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void buildAndNormalizeAllNodes() {
        for (Node node : allNodes) {
            node.buildConnectedNodeProbs();
            node.normalizeEdgeProbabilities();
        }
    }
}
