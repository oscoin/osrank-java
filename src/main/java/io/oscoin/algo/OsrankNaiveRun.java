package io.oscoin.algo;

import io.oscoin.graph.Graph;
import io.oscoin.graph.Node;
import io.oscoin.graph.ProjectNode;

import java.util.Random;

public class OsrankNaiveRun {

    private OsrankParams osrankParams;
    private Graph graph;
    private Random random;

    public OsrankNaiveRun(OsrankParams osrankParams, Graph graph, Random random) {
        this.osrankParams = osrankParams;
        this.graph = graph;
        this.random = random;
    }

    public OsrankResults runNaiveOsrankAlgorithm() {

        OsrankResults osrankResults = new OsrankResults(osrankParams, graph);
        int count = 0;
        for (Node node : graph.getAllNodes()) {
            for (int i=0; i<osrankParams.getR(); i++) {
                doOneRandomWalk(node, osrankResults);
            }
            count++;
            if (count % 10000 == 0) System.out.println("Finished simulating walks for " + count + " nodes");
        }
        return osrankResults;
    }

    private void doOneRandomWalk(Node node, OsrankResults osrankResults) {

        Node currentNode = node;
        while (true) {

            // Increment the visit count
            osrankResults.incrementVisitCount(currentNode.getNodeId());

            // If we get to a node that has no outbound edges, we end the walk
            if (node.getConnectedNodeCount() == 0) break;

            // Get the correct damping factor based on the node type
            double dampingFactor;
            if (currentNode instanceof ProjectNode) dampingFactor = osrankParams.getProjectDampingFactor();
            else dampingFactor = osrankParams.getAccountDampingFactor();

            // Check against the damping factor to see if the random walk continues
            double dampingCheck = random.nextDouble();
            if (dampingCheck >= dampingFactor) break;

            // Get the next node
            Integer nextNodeId = currentNode.walkToNextNodeId(random.nextDouble());
            currentNode = graph.getNodeById(nextNodeId);
        }
    }
}
