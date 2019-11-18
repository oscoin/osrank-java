package io.oscoin.loader;

import io.oscoin.algo.OsrankParams;
import io.oscoin.graph.Graph;
import io.oscoin.graph.Node;
import io.oscoin.util.MathUtils;
import org.junit.Test;

import java.util.List;

public class SimpleGraphLoaderTest {

    @Test
    public void testSimpleGraphLoader() {

        OsrankParams osrankBasicParams = OsrankParams.buildBasicOsrankParams();

        Graph simpleGraph = SimpleGraphLoader.buildSimpleOneProjectOneContributorGraph(osrankBasicParams);


        List<Node> nodes = simpleGraph.getAllNodes();

        // Check that there are 2 nodes
        assert(nodes.size() == 2);

        // Check that each node's edge probabilities are normalized (i.e. they sum to 1)
        for (Node node : nodes) {
            double sumOfEdgeProbabilities = node.getSumOfEdgeProbabilities();
            assert(MathUtils.approxEqual(sumOfEdgeProbabilities, 1d));
        }
    }
}