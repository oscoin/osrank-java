package io.oscoin.app;

import io.oscoin.algo.OsrankNaiveRun;
import io.oscoin.algo.OsrankParams;
import io.oscoin.algo.OsrankResults;
import io.oscoin.graph.Graph;
import io.oscoin.loader.RandomGraphLoader;
import io.oscoin.loader.SimpleGraphLoader;
import io.oscoin.util.OrderedPair;
import io.oscoin.util.Timer;
import io.oscoin.util.TopList;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Application class that runs naive Osrank on randomly generated graphs with parameters specified in the constant
 * values in this class.
 *
 */
public class OsrankNaiveRandomGraphApp {

    // Random project graph generation parameters
    public int NUM_PROJECTS = 100000;
    public int NUM_EXTRA_ACCOUNTS = 200000;
    int MAX_ADDITIONAL_PROJECTS_TO_CONTRIBUTE_TO = 10;
    int MAX_EXTRA_CONTRIBUTIONS_PER_PROJECT = 20;

    // Naive Osrank parameters
    public static final int R = 100;
    public static final double PROJECT_DAMPING_FACTOR = 0.85d;
    public static final double ACCOUNT_DAMPING_FACTOR = 0.85d;
    public static final long RANDOM_SEED = 842384239487239l;


    public OsrankNaiveRandomGraphApp() {
    }

    private void runOsrankNaive(OsrankParams osrankParams) {

        System.out.println("Building random graph....");
        Random random = new Random(osrankParams.getRandomSeed());
        Graph randomGraph =
            RandomGraphLoader.buildRandomlyGenerattedGraph(
                NUM_PROJECTS,
                NUM_EXTRA_ACCOUNTS,
                MAX_ADDITIONAL_PROJECTS_TO_CONTRIBUTE_TO,
                MAX_EXTRA_CONTRIBUTIONS_PER_PROJECT,
                random);
        System.out.println("Done");

        System.out.println("Starting naive algorithm....");
        OsrankNaiveRun osrankNaiveRun = new OsrankNaiveRun(osrankParams, randomGraph);
        OsrankResults results = osrankNaiveRun.runNaiveOsrankAlgorithm();
        System.out.println("Done");

        System.out.println("Top nodes with Osranks:");
        Map<Integer,Double> osrankMap = results.getOsrankMap();
        TopList topList = new TopList(20);
        for (Map.Entry<Integer,Double> entry : osrankMap.entrySet()) {
            topList.tryToAdd(new OrderedPair<>(entry.getKey(), entry.getValue()));
        }
        List<OrderedPair<Integer,Double>> topNodesAndOsranks = topList.getTop();
        for (OrderedPair<Integer,Double> oneEntry : topNodesAndOsranks) {
            System.out.println("Node ID = " + oneEntry.left + "    Osrank = " + oneEntry.right);
        }
    }

    public static void main(String[] args) {

        long totalSeconds = Timer.getElapsedSeconds(() -> {
            // default params
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
            new OsrankNaiveRandomGraphApp().runOsrankNaive(osrankParams);
        });

        System.out.println("Run took " + totalSeconds + " seconds");
    }
}
