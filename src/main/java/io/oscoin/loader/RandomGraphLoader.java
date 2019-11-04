package io.oscoin.loader;

import io.oscoin.graph.AccountNode;
import io.oscoin.graph.Graph;
import io.oscoin.graph.Node;
import io.oscoin.graph.ProjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGraphLoader {

    public static final int TIER_2_MAX_DEPENDENCIES = 10;

    public static final int TIER_3_MAX_DEPENDENCIES = 50;

    public static Graph buildRandomlyGenerattedGraph(
            int numProjects,
            int numExtraAccounts,
            int maxAdditionalProjectsToContributeTo,
            int maxExtraContributionsPerProject,
            Random random) {

        Graph graph = new Graph();

        List<ProjectNode> projectNodes = new ArrayList<>();

        // Each project has one contributor/maintainer by default
        for (int i=0; i<numProjects * 2; i=i+2) {
            ProjectNode projectNode = new ProjectNode(i);
            projectNodes.add(projectNode);
            projectNode.addProjectContributor(i+1, 10);
            projectNode.addProjectMaintainer(i+1);
            graph.addProjectNode(projectNode);

            AccountNode accountNode = new AccountNode( i+1);
            accountNode.addProjectContributions(i, 10);
            accountNode.addProjectMaintained(i);
            graph.addAccountNode(accountNode);

            // Each project after the first 100 has a chance of having some dependencies
            if (i > 100) {
                double dependenciesRand = random.nextDouble();
                if (dependenciesRand >= 0.3 && dependenciesRand < 0.7) {
                    addOneProjectDependency(projectNode, projectNodes, i, random);
                } else if (dependenciesRand > 0.7 && dependenciesRand < 0.9){
                    for (int j=0; j<random.nextInt() % TIER_2_MAX_DEPENDENCIES; j++) {
                        addOneProjectDependency(projectNode, projectNodes, i, random);
                    }
                } else if (dependenciesRand > 0.9){
                    for (int j=0; j<random.nextInt() % TIER_3_MAX_DEPENDENCIES; j++) {
                        addOneProjectDependency(projectNode, projectNodes, i, random);
                    }
                }
            }
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

    private static void addOneProjectDependency(
            ProjectNode projectNode,
            List<ProjectNode> projectNodes,
            int maxProjectIndex,
            Random random) {

        int dependencyIndex = 2 * (Math.abs(random.nextInt()) % (maxProjectIndex - 1));
        projectNode.addProjectDependency(dependencyIndex);
    }
}
