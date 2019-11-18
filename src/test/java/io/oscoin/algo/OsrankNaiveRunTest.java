package io.oscoin.algo;

import io.oscoin.graph.Graph;
import io.oscoin.loader.SimpleGraphLoader;
import org.junit.Test;

import java.util.Map;

public class OsrankNaiveRunTest {

    @Test
    public void testNaiveOsrankRun() {

        OsrankParams basicOsrankParams = OsrankParams.buildBasicOsrankParams();

        Graph simpleGraph = SimpleGraphLoader.buildSimpleOneProjectOneContributorGraph();

        OsrankNaiveRun osrankNaiveRun = new OsrankNaiveRun(basicOsrankParams, simpleGraph);
        OsrankResults osrankResults = osrankNaiveRun.runNaiveOsrankAlgorithm();

        assert(osrankResults.getOsrankMap().size() == 2);
        assert(osrankResults.totalWalks == 20d);
        for (Map.Entry<Integer, Double> entry : osrankResults.getOsrankMap().entrySet()) {
            assert(entry.getValue() > 0);
        }
    }
}
