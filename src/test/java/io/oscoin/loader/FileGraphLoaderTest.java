package io.oscoin.loader;

import io.oscoin.graph.Graph;
import io.oscoin.graph.Node;
import org.junit.Test;

import java.util.List;

public class FileGraphLoaderTest {

    @Test
    public void testFileGraphLoader() {

        String metadataFilename = "./metadata.csv";
        String dependenciesFilename = "./dependencies.csv";
        String contributionsFilename = "./contributions.csv";

        Graph simpleGraph = FileGraphLoader.load(metadataFilename, dependenciesFilename, contributionsFilename);

        List<Node> nodes = simpleGraph.getAllNodes();

        // Check that there are lots of nodes
        System.out.println("Total nodes in graph " + nodes.size());
        assert(nodes.size() > 10000);

        // Check some node specifics
        System.out.println("Sum of edge probs for node 31115 is " + simpleGraph.getNodeById(31115).getSumOfEdgeProbabilities());
    }
}