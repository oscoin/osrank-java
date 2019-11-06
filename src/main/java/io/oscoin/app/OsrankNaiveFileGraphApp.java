package io.oscoin.app;

import io.oscoin.algo.OsrankNaiveRun;
import io.oscoin.algo.OsrankParams;
import io.oscoin.algo.OsrankResults;
import io.oscoin.graph.AccountNode;
import io.oscoin.graph.Graph;
import io.oscoin.graph.ProjectNode;
import io.oscoin.loader.FileGraphLoader;
import io.oscoin.util.OrderedPair;
import io.oscoin.util.TopList;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Application class that runs naive Osrank on graphs from the "ecosystem" files in the osrank-rs-ecosystem repository
 * in Oscoin github:
 *
 * https://github.com/oscoin/osrank-rs-ecosystems
 */
public class OsrankNaiveFileGraphApp {

    // Random seed to use
    public long RANDOM_SEED = 842384239487239l;

    // Naive Osrank parameters
    public static final int R = 100;
    public static final double PROJECT_DAMPING_FACTOR = 0.85d;
    public static final double ACCOUNT_DAMPING_FACTOR = 0.85d;

    // File paths to load graph of projects and accounts from
    public static final String METADATA_FILE_PATH = "./metadata.csv";
    public static final String DEPENDENCIES_FILE_PATH = "./dependencies.csv";
    public static final String CONTRIBUTIONS_FILE_PATH = "./contributions.csv";

    public OsrankNaiveFileGraphApp() {
    }

    private void runOsrankNaive() {

        System.out.println("Loading graph....");
        Graph graph = FileGraphLoader.load(
            METADATA_FILE_PATH,
            DEPENDENCIES_FILE_PATH,
            CONTRIBUTIONS_FILE_PATH,
            true);
        Random random = new Random(RANDOM_SEED);
        System.out.println("Done");

        OsrankParams params = new OsrankParams(R, PROJECT_DAMPING_FACTOR, ACCOUNT_DAMPING_FACTOR);

        System.out.println("Starting naive algorithm....");
        OsrankNaiveRun osrankNaiveRun = new OsrankNaiveRun(params, graph, random);
        OsrankResults results = osrankNaiveRun.runNaiveOsrankAlgorithm();
        System.out.println("Done");

        // Split results into Project Nodes and Account Nodes
        Map<Integer,Double> osrankMap = results.getOsrankMap();

        Map<Integer, Double> projectNodesAndRanks = osrankMap.entrySet().stream()
                .filter(entry -> graph.getNodeById(entry.getKey()) instanceof ProjectNode)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<Integer, Double> accountNodesAndRanks = osrankMap.entrySet().stream()
                .filter(entry -> graph.getNodeById(entry.getKey()) instanceof AccountNode)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Print Top Project Nodes
        printTopNodes(graph, projectNodesAndRanks);

        // Print Top Account Nodes
        printTopNodes(graph, accountNodesAndRanks);
    }

    private void printTopNodes(Graph graph, Map<Integer,Double> osrankMap) {
        TopList topList = new TopList(100);
        for (Map.Entry<Integer,Double> entry : osrankMap.entrySet()) {
            topList.tryToAdd(new OrderedPair<>(entry.getKey(), entry.getValue()));
        }
        List<OrderedPair<Integer,Double>> topNodesAndOsranks = topList.getTop();
        System.out.println("Name, Id, Osrank");
        Collections.reverse(topNodesAndOsranks);
        for (OrderedPair<Integer,Double> oneEntry : topNodesAndOsranks) {
            System.out.format("%s, %d, %.4f\n", graph.getNodeById(oneEntry.left).getNodeName(), oneEntry.left, oneEntry.right);
        }
        System.out.println("");
    }

    public static void main(String[] args) {

        long startTimestamp = System.currentTimeMillis();
        new OsrankNaiveFileGraphApp().runOsrankNaive();
        long endTimestamp = System.currentTimeMillis();

        long totalTime = endTimestamp - startTimestamp;
        long totalSeconds = totalTime / 1000;

        System.out.println("Run took " + totalSeconds + " seconds");
    }
}
