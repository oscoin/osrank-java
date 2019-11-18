package io.oscoin.loader;

import io.oscoin.algo.OsrankParams;
import io.oscoin.graph.Graph;
import io.oscoin.graph.Node;
import io.oscoin.util.MathUtils;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class RandomGraphLoaderTest {

    public static final long RANDOM_SEED = 12345l;

    @Test
    public void testSimpleGraphLoader() {

        OsrankParams basicOsrankParams = OsrankParams.buildBasicOsrankParams();

        Graph simpleGraph =
                RandomGraphLoader.buildRandomlyGeneratedGraph(
                        10, 10, 5, 100, basicOsrankParams, new Random(RANDOM_SEED));

        List<Node> nodes = simpleGraph.getAllNodes();

        // Check that there are 2 nodes
        assert(nodes.size() == 30);

        // Check that each node's edge probabilities are normalized (i.e. they sum to 1)
        for (Node node : nodes) {
            double sumOfEdgeProbabilities = node.getSumOfEdgeProbabilities();
            assert(MathUtils.approxEqual(sumOfEdgeProbabilities, 1d));
        }
    }
}