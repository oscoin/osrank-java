package io.oscoin.loader;

import io.oscoin.graph.AccountNode;
import io.oscoin.graph.Graph;
import io.oscoin.graph.Node;
import io.oscoin.graph.ProjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGraphLoader {

    public static Graph buildRandomlyGenerattedGraph(
            int numProjects,
            int numExtraAccounts,
            int maxAdditionalProjectsToContributeTo,
            int maxExtraContributionsPerProject,
            Random random) {

        Graph graph = new Graph();

        List<ProjectNode> projectNodes = new ArrayList<>();

        // Each project has one contributor/maintainer by default
        for (int i=0; i<numProjects; i++) {
            ProjectNode projectNode = new ProjectNode(i);
            projectNodes.add(projectNode);
            projectNode.addProjectContributor(i+1, 10);
            projectNode.addProjectMaintainer(i+1);
            graph.addProjectNode(projectNode);

            AccountNode accountNode = new AccountNode( i+1);
            accountNode.addProjectContributions(i, 10);
            accountNode.addProjectMaintained(i);
            graph.addAccountNode(accountNode);
        }

        // Then there are a number of extra accounts that each contribute to an additional random number of projects
        // between 1 and maxAdditionalProjectsToContributeTo
        for (int i=0; i<numExtraAccounts; i++) {
            AccountNode extraAccountNode = new AccountNode( (numProjects * 2) + i);

            int numProjectsToContributeTo = (Math.abs(random.nextInt()) % maxAdditionalProjectsToContributeTo) + 1;
            for (int j = 0; j < numProjectsToContributeTo; j++) {
                int projectIndexToContributeTo = Math.abs(random.nextInt()) % projectNodes.size();

                ProjectNode projectNode = projectNodes.get(projectIndexToContributeTo);
                int contributions = (Math.abs(random.nextInt()) % maxExtraContributionsPerProject) + 1;
                projectNode.addProjectContributor(extraAccountNode.getNodeId(), contributions);

                extraAccountNode.addProjectContributions(projectNode.getNodeId(), contributions);
            }

            graph.addAccountNode(extraAccountNode);
        }

        // Build the probabilities and normalize them for all nodes
        for (Node node : graph.getAllNodes()) {
            node.buildConnectedNodeProbs();
            node.normalizeEdgeProbabilities();
        }

        return graph;
    }
}
