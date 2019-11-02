package io.oscoin.loader;

import io.oscoin.graph.AccountNode;
import io.oscoin.graph.Graph;
import io.oscoin.graph.ProjectNode;

public class SimpleGraphLoader {

    public static Graph buildSimpleOneProjectOneContributorGraph() {

        Graph graph = new Graph();

        ProjectNode projectNode = new ProjectNode(1);
        projectNode.addProjectContributor(2, 10);
        projectNode.addProjectMaintainer(2);
        projectNode.buildConnectedNodeProbs();
        graph.addProjectNode(projectNode);

        AccountNode accountNode = new AccountNode( 2);
        accountNode.addProjectContributions(1, 10);
        accountNode.addProjectMaintained(1);
        accountNode.buildConnectedNodeProbs();
        graph.addAccountNode(accountNode);

        // Normalize the nodes
        projectNode.normalizeEdgeProbabilities();
        accountNode.normalizeEdgeProbabilities();

        return graph;
    }
}
