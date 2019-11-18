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

    public static final double PROJECT_DEPENDENCY_WEIGHT = 4d;
    public static final double PROJECT_MAINTAINER_WEIGHT = 0d;
    public static final double PROJECT_CONTRIBUTION_WEIGHT = 4d;
    public static final double ACCOUNT_MAINTAINER_WEIGHT = 3d;
    public static final double ACCOUNT_CONTRIBUTION_WEIGHT = 2d;

    public OsrankNaiveSimpleGraphApp() {
    }

    private void runOsrankNaive(OsrankParams osrankParams) {

        Graph simpleGraph = SimpleGraphLoader.buildSimpleOneProjectOneContributorGraph(osrankParams);

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
                null,
                RANDOM_SEED,
                PROJECT_DEPENDENCY_WEIGHT,
                PROJECT_MAINTAINER_WEIGHT,
                PROJECT_CONTRIBUTION_WEIGHT,
                ACCOUNT_MAINTAINER_WEIGHT,
                ACCOUNT_CONTRIBUTION_WEIGHT
        );

        // overwrite with command-line options
        OsrankParams osrankParams = OsrankParams.getInstance(args, osrankDefaultParams);

        // run app
        new OsrankNaiveSimpleGraphApp().runOsrankNaive(osrankParams);
    }
}
