package io.oscoin.app;

import io.oscoin.algo.OsrankNaiveRun;
import io.oscoin.algo.OsrankParams;
import io.oscoin.algo.OsrankResults;
import io.oscoin.graph.Graph;
import io.oscoin.loader.FileGraphLoader;
import io.oscoin.util.OutputUtils;
import io.oscoin.util.Timer;

/**
 * Application class that runs naive Osrank on graphs from the "ecosystem" files in the osrank-rs-ecosystem repository
 * in Oscoin github:
 *
 * https://github.com/oscoin/osrank-rs-ecosystems
 */
public class OsrankNaiveFileGraphApp {

    public int MAX_WINNERS_TO_DISPLAY = 10;

    // Naive Osrank parameter defaults
    public static final int R = 1000;
    public static final double PROJECT_DAMPING_FACTOR = 0.85d;
    public static final double ACCOUNT_DAMPING_FACTOR = 0.85d;
    public static final String METADATA_FILE_PATH = null;
    public static final String DEPENDENCIES_FILE_PATH = null;
    public static final String CONTRIBUTIONS_FILE_PATH = null;
    public static final String RESULTS_FILE_PATH = "./ranks.csv";
    public static final Boolean ADD_MAINTAINERS = false;
    public static final long RANDOM_SEED = 842384239487239l;

    public static final double PROJECT_DEPENDENCY_WEIGHT = 4d;
    public static final double PROJECT_MAINTAINER_WEIGHT = 0d;
    public static final double PROJECT_CONTRIBUTION_WEIGHT = 1d;
    public static final double ACCOUNT_MAINTAINER_WEIGHT = 3d;
    public static final double ACCOUNT_CONTRIBUTION_WEIGHT = 2d;

    public OsrankNaiveFileGraphApp() {
    }

    private void runOsrankNaive(OsrankParams osrankParams) {

        System.out.println("Using params....");
        System.out.println(osrankParams.toString());

        System.out.println("Loading graph....");
        Graph graph = FileGraphLoader.load(osrankParams);
        System.out.println("Done");

        System.out.println("Starting naive algorithm....");
        OsrankNaiveRun osrankNaiveRun = new OsrankNaiveRun(osrankParams, graph);
        OsrankResults results = osrankNaiveRun.runNaiveOsrankAlgorithm();
        System.out.println("Done");

        // Write results to file
        OutputUtils.writeResultsToCSV(results, graph,false, 0, osrankParams.getResultsFilePath());

        // Output results to screen
        if (MAX_WINNERS_TO_DISPLAY > 0 ) {
            OutputUtils.outputResults(results, graph, false, MAX_WINNERS_TO_DISPLAY);
        }
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
                    RESULTS_FILE_PATH,
                    ADD_MAINTAINERS,
                    RANDOM_SEED,
                    PROJECT_DEPENDENCY_WEIGHT,
                    PROJECT_MAINTAINER_WEIGHT,
                    PROJECT_CONTRIBUTION_WEIGHT,
                    ACCOUNT_MAINTAINER_WEIGHT,
                    ACCOUNT_CONTRIBUTION_WEIGHT
                    );

            // overwrite with command-line options
            OsrankParams osrankParams = OsrankParams.getInstance(args, osrankDefaultParams);

            if (osrankParams.validate()) {
                // run app
                new OsrankNaiveFileGraphApp().runOsrankNaive(osrankParams);
            } else {
                System.out.println("Please add mandatory parameters and come again.");
            }

        });

        System.out.println("Run took " + totalSeconds + " seconds");
    }

}
