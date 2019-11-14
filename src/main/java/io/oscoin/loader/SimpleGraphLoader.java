package io.oscoin.loader;

import io.oscoin.algo.OsrankParams;
import io.oscoin.graph.AccountNode;
import io.oscoin.graph.Graph;
import io.oscoin.graph.ProjectNode;

/**
 * Helper class with static methods for creating simple graphs with pre-defined structure that are useful for testing.
 */
public class SimpleGraphLoader {

    /**
     * @return  A very simple graph with one project node and one account node that is both a contributor (with 10
     * contributions) and maintainer for the project.
     */
    public static Graph buildSimpleOneProjectOneContributorGraph(OsrankParams osrankParams) {

        Graph graph = new Graph();

        ProjectNode projectNode = new ProjectNode(1);
        projectNode.addProjectContributor(2, 10);
        projectNode.addProjectMaintainer(2);
        graph.addProjectNode(projectNode);

        AccountNode accountNode = new AccountNode( 2);
        accountNode.addProjectContributions(1, 10);
        accountNode.addProjectMaintained(1);
        graph.addAccountNode(accountNode);

        // Normalize the nodes
        graph.buildAndNormalizeAllNodes(
            osrankParams.getProjectDependencyWeight(),
            osrankParams.getProjectMaintainerWeight(),
            osrankParams.getProjectContributionWeight(),
            osrankParams.getAccountMaintainerWeight(),
            osrankParams.getAccountContributionWeight());

        return graph;
    }
}
