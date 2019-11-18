package io.oscoin.algo;

import io.oscoin.graph.Graph;
import io.oscoin.loader.SimpleGraphLoader;
import org.junit.Test;

import java.util.Map;

public class OsrankResultsTest {

    @Test
    public void testOsrankResults() {

        OsrankParams basicOsrankParams = OsrankParams.buildBasicOsrankParams();

        Graph simpleGraph = SimpleGraphLoader.buildSimpleOneProjectOneContributorGraph();

        OsrankResults osrankResults = new OsrankResults(basicOsrankParams, simpleGraph);

        for (int i=0; i<10; i++) {
            osrankResults.incrementVisitCount(1);
            osrankResults.incrementVisitCount(2);
        }

        assert(osrankResults.totalWalks == 20);
        assert(osrankResults.getOsrankMap().size() == 2);

        for (Map.Entry<Integer, Double> entry : osrankResults.getOsrankMap().entrySet()) {
            if (entry.getKey() != 1 && entry.getKey() != 2) assert(false);
        }

        double currentWalks = osrankResults.getOsrankMap().get(1);
        osrankResults.incrementVisitCount(1);
        double newWalks = osrankResults.getOsrankMap().get(1);
        assert (newWalks > currentWalks);
    }
}
