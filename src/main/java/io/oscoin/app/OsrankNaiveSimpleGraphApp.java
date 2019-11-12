package io.oscoin.app;

import io.oscoin.algo.OsrankNaiveRun;
import io.oscoin.algo.OsrankParams;
import io.oscoin.algo.OsrankResults;
import io.oscoin.graph.Graph;
import io.oscoin.loader.SimpleGraphLoader;
import io.oscoin.util.Timer;

import java.util.Map;
import java.util.Random;

/**
 * Application class that runs naive Osrank on a simple graph with one project node and one account node.
 */
public class OsrankNaiveSimpleGraphApp {

    // Naive Osrank parameters
    public static final int R = 10000;
    public static final double PROJECT_DAMPING_FACTOR = 0.85d;
    public static final double ACCOUNT_DAMPING_FACTOR = 0.85d;
    public static final long RANDOM_SEED = 842384239487239l;

    public OsrankNaiveSimpleGraphApp() {
    }

    private void runOsrankNaive(OsrankParams osrankParams) {

        Graph simpleGraph = SimpleGraphLoader.buildSimpleOneProjectOneContributorGraph();

        OsrankNaiveRun osrankNaiveRun = new OsrankNaiveRun(osrankParams, simpleGraph);

        OsrankResults results = osrankNaiveRun.runNaiveOsrankAlgorithm();

        Map<Integer,Double> osrankMap = results.getOsrankMap();

        for (Integer nodeId : osrankMap.keySet()) {
            Double osrank = osrankMap.get(nodeId);
            System.out.println("Osrank for node " + nodeId + " = " + osrank);
        }
    }

    public static void main(String[] args) {

        OsrankParams osrankDefaultParams = new OsrankParams(
                R,
                PROJECT_DAMPING_FACTOR,
                ACCOUNT_DAMPING_FACTOR,
                null,
                null,
                null,
                null,
                RANDOM_SEED);

        // overwrite with command-line options
        OsrankParams osrankParams = OsrankParams.getInstance(args, osrankDefaultParams);

        // run app
        new OsrankNaiveSimpleGraphApp().runOsrankNaive(osrankParams);
    }
}
