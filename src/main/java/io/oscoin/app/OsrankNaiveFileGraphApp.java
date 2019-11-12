package io.oscoin.app;

import io.oscoin.algo.OsrankNaiveRun;
import io.oscoin.algo.OsrankParams;
import io.oscoin.algo.OsrankResults;
import io.oscoin.graph.AccountNode;
import io.oscoin.graph.Graph;
import io.oscoin.graph.ProjectNode;
import io.oscoin.loader.FileGraphLoader;
import io.oscoin.util.OrderedPair;
import io.oscoin.util.OutputUtils;
import io.oscoin.util.Timer;
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
    public int MAX_WINNERS_TO_DISPLAY = 100;

    // Naive Osrank parameters
    public static final int R = 1000;
    public static final double PROJECT_DAMPING_FACTOR = 0.85d;
    public static final double ACCOUNT_DAMPING_FACTOR = 0.85d;
    public static final String METADATA_FILE_PATH = "./metadata.csv";
    public static final String DEPENDENCIES_FILE_PATH = "./dependencies.csv";
    public static final String CONTRIBUTIONS_FILE_PATH = "./contributions.csv";
    public static final Boolean ADD_MAINTAINERS = false;

    public OsrankNaiveFileGraphApp() {
    }

    private void runOsrankNaive(OsrankParams osrankParams) {

        System.out.println("Using params....");
        System.out.println(osrankParams.toString());

        System.out.println("Loading graph....");
        Graph graph = FileGraphLoader.load(
            osrankParams.getMetadataFilePath(),
            osrankParams.getDependenciesFilePath(),
            osrankParams.getContributionsFilePath(),
            osrankParams.getAddMaintainersFlag());
        Random random = new Random(RANDOM_SEED);
        System.out.println("Done");

        System.out.println("Starting naive algorithm....");
        OsrankNaiveRun osrankNaiveRun = new OsrankNaiveRun(osrankParams, graph, random);
        OsrankResults results = osrankNaiveRun.runNaiveOsrankAlgorithm();
        System.out.println("Done");

        // Write results to file
        OutputUtils.writeResultsToCSV(results, graph,false, 1000, "./results/results-projects-1000.csv");
        OutputUtils.writeResultsToCSV(results, graph,false, 0, "./results/results-projects-all.csv");

        // Output results to screen
        OutputUtils.outputResults(results, graph,false, MAX_WINNERS_TO_DISPLAY);
    }



    public static void main(String[] args) {

        long totalSeconds = Timer.getElapsedSeconds(() -> {
            // default params
            OsrankParams osrankDefaultParams = new OsrankParams(
                    R,
                    PROJECT_DAMPING_FACTOR,
                    ACCOUNT_DAMPING_FACTOR,
                    METADATA_FILE_PATH,
                    DEPENDENCIES_FILE_PATH,
                    CONTRIBUTIONS_FILE_PATH,
                    ADD_MAINTAINERS);

            // overwrite with command-line options
            OsrankParams osrankParams = OsrankParams.getInstance(args, osrankDefaultParams);

            // run app
            new OsrankNaiveFileGraphApp().runOsrankNaive(osrankParams);
        });

        System.out.println("Run took " + totalSeconds + " seconds");
    }

}
