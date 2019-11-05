package io.oscoin.app;

import io.oscoin.algo.OsrankNaiveRun;
import io.oscoin.algo.OsrankParams;
import io.oscoin.algo.OsrankResults;
import io.oscoin.graph.Graph;
import io.oscoin.loader.FileGraphLoader;
import io.oscoin.util.OrderedPair;
import io.oscoin.util.TopList;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class OsrankNaiveFileGraphApp {

    public long RANDOM_SEED = 842384239487239l;

    // Naive Osrank parameters
    public static final int R = 1000;
    public static final double PROJECT_DAMPING_FACTOR = 0.85d;
    public static final double ACCOUNT_DAMPING_FACTOR = 0.85d;

    public OsrankNaiveFileGraphApp() {
    }

    private void runOsrankNaive() {

        System.out.println("Loading graph....");
        Graph graph = FileGraphLoader.load(
            "./metadata.csv",
            "./dependencies.csv",
            "./contributions.csv"
        );
        Random random = new Random(RANDOM_SEED);
        System.out.println("Done");

        OsrankParams params = new OsrankParams(R, PROJECT_DAMPING_FACTOR, ACCOUNT_DAMPING_FACTOR);

        System.out.println("Starting naive algorithm....");
        OsrankNaiveRun osrankNaiveRun = new OsrankNaiveRun(params, graph, random);
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

        new OsrankNaiveFileGraphApp().runOsrankNaive();
    }
}
