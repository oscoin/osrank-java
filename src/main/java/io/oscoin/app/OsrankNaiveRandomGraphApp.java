package io.oscoin.app;

import io.oscoin.algo.OsrankNaiveRun;
import io.oscoin.algo.OsrankParams;
import io.oscoin.algo.OsrankResults;
import io.oscoin.graph.Graph;
import io.oscoin.loader.RandomGraphLoader;
import io.oscoin.loader.SimpleGraphLoader;
import io.oscoin.util.OrderedPair;
import io.oscoin.util.TopList;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class OsrankNaiveRandomGraphApp {

    public long RANDOM_SEED = 842384239487239l;

    // Random project graph generation parameters
    public int NUM_PROJECTS = 10000;
    public int NUM_EXTRA_ACCOUNTS = 20000;
    int MAX_ADDITIONAL_PROJECTS_TO_CONTRIBUTE_TO = 10;
    int MAX_EXTRA_CONTRIBUTIONS_PER_PROJECT = 20;

    // Naive Osrank parameters
    public static final int R = 100;
    public static final double PROJECT_DAMPING_FACTOR = 0.85d;
    public static final double ACCOUNT_DAMPING_FACTOR = 0.85d;

    public OsrankNaiveRandomGraphApp() {
    }

    private void runOsrankNaive() {

        System.out.println("Building random graph....");
        Random random = new Random(RANDOM_SEED);
        Graph randomGraph =
            RandomGraphLoader.buildRandomlyGenerattedGraph(
                NUM_PROJECTS,
                NUM_EXTRA_ACCOUNTS,
                MAX_ADDITIONAL_PROJECTS_TO_CONTRIBUTE_TO,
                MAX_EXTRA_CONTRIBUTIONS_PER_PROJECT,
                random);
        System.out.println("Done");

        OsrankParams params = new OsrankParams(R, PROJECT_DAMPING_FACTOR, ACCOUNT_DAMPING_FACTOR);

        System.out.println("Starting naive algorithm....");
        OsrankNaiveRun osrankNaiveRun = new OsrankNaiveRun(params, randomGraph, random);
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

        new OsrankNaiveRandomGraphApp().runOsrankNaive();
    }
}
