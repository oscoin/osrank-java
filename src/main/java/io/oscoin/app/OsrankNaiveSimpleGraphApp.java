package io.oscoin.app;

import io.oscoin.algo.OsrankNaiveRun;
import io.oscoin.algo.OsrankParams;
import io.oscoin.algo.OsrankResults;
import io.oscoin.graph.Graph;
import io.oscoin.loader.SimpleGraphLoader;

import java.util.Map;
import java.util.Random;

/**
 * Application class that runs naive Osrank on a simple graph with one project node and one account node.
 */
public class OsrankNaiveSimpleGraphApp {

    public static final long RANDOM_SEED = 842384239487239l;

    // Naive Osrank parameters
    public static final int R = 10000;
    public static final double PROJECT_DAMPING_FACTOR = 0.85d;
    public static final double ACCOUNT_DAMPING_FACTOR = 0.85d;

    public OsrankNaiveSimpleGraphApp() {
    }

    private void runOsrankNaive() {

        OsrankParams params = new OsrankParams(R, PROJECT_DAMPING_FACTOR, ACCOUNT_DAMPING_FACTOR);
        Graph simpleGraph = SimpleGraphLoader.buildSimpleOneProjectOneContributorGraph();
        Random random = new Random(RANDOM_SEED);

        OsrankNaiveRun osrankNaiveRun = new OsrankNaiveRun(params, simpleGraph, random);

        OsrankResults results = osrankNaiveRun.runNaiveOsrankAlgorithm();

        Map<Integer,Double> osrankMap = results.getOsrankMap();

        for (Integer nodeId : osrankMap.keySet()) {
            Double osrank = osrankMap.get(nodeId);
            System.out.println("Osrank for node " + nodeId + " = " + osrank);
        }
    }

    public static void main(String[] args) {

        new OsrankNaiveSimpleGraphApp().runOsrankNaive();
    }
}
