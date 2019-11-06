package io.oscoin.algo;

import io.oscoin.graph.Graph;
import io.oscoin.graph.Node;
import io.oscoin.graph.ProjectNode;

import java.util.Random;

/**
 * This class contains implementation logic for the "naive" Osrank algorithm -- i.e. an Osrank algorithm that computes
 * Osranks for each node in the graph in a non-incremental fashion.
 *
 * Each instance of this class represents a starting state for the algorithm, including the graph it is to be run on,
 * the parameters to use for the run, and a source of randomness.
 */
public class OsrankNaiveRun {

    private OsrankParams osrankParams;
    private Graph graph;
    private Random random;

    public OsrankNaiveRun(OsrankParams osrankParams, Graph graph, Random random) {
        this.osrankParams = osrankParams;
        this.graph = graph;
        this.random = random;
    }

    /**
     * Runs the Osrank algorithm, given the parameters, graph and source of randomness that this class instance
     * was created with.
     *
     * @return  The results of the run.
     */
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

    /**
     * Simulates a single random walk in the graph, noting which nodes were visited in the OsrankResults
     *
     * @param node  The node to start the random walk from
     * @param osrankResults  The results object where results are recorded. In particular, numbers of node visits
     */
    private void doOneRandomWalk(Node node, OsrankResults osrankResults) {

        Node currentNode = node;
        while (true) {

            // Increment the visit count
            osrankResults.incrementVisitCount(currentNode.getNodeId());

            // If we get to a node that has no outbound edges, we end the walk
            if (currentNode.getConnectedNodeCount() == 0) break;

            // Get the correct damping factor based on the node type
            double dampingFactor;
            if (currentNode instanceof ProjectNode) dampingFactor = osrankParams.getProjectDampingFactor();
            else dampingFactor = osrankParams.getAccountDampingFactor();

            // Check against the damping factor to see if the random walk continues
            double dampingCheck = random.nextDouble();
            if (dampingCheck >= dampingFactor) break;

            // Get the next node
            double randomValueForNextNode = random.nextDouble();
            Integer nextNodeId = currentNode.walkToNextNodeId(randomValueForNextNode);
            Node nextNode = graph.getNodeById(nextNodeId);

            currentNode = nextNode;
        }
    }
}
