package io.oscoin.util;

import io.oscoin.algo.OsrankResults;
import io.oscoin.graph.Graph;
import io.oscoin.graph.ProjectNode;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility to help us output OsrankResult objects to screen/file
 * Option to output all nodes or just the project nodes
 * Option to use only the top N nodes after sorting by osrank
 *
 */
public class OutputUtils {

    /**
     * Write project names, ids, and osranks to file, sorted by osrank
     */
    public static void writeResultsToCSV(OsrankResults results, Graph graph, boolean includeAccountNodes, int numTopNodes, String filename) {

        System.out.format("Writing Osrank results to %s, includeAccountNodes: %b, numTopNodes: %d%n", filename, includeAccountNodes, numTopNodes);

        Stream<Map.Entry<Integer,Double>> sortedResultsAsStream = prepareResultsStream(results, graph, includeAccountNodes, numTopNodes);

        try {
            FileWriter csvWriter = new FileWriter(filename);

            // Write out header
            csvWriter.append("Name, Id, Osrank\n");

            // Write out each node
            sortedResultsAsStream.forEach((entry) -> {
                int nodeId = entry.getKey();
                String nodeName = graph.getNodeById(nodeId).getNodeName();
                double nodeOsrank = entry.getValue();

                try {
                    csvWriter.append(String.format("%s, %d, %.6f\n", nodeName, nodeId, nodeOsrank));
                } catch (IOException e) {
                    System.out.format("Problem writing node with id: %d, osrank: %.4f", nodeId, nodeOsrank);
                }
            });

            // Finish up
            csvWriter.flush();
            csvWriter.close();

            System.out.println("Done.");

        } catch (IOException e) {
            System.out.format("Problem writing results to %s%n", filename);
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Write project names, ids, and osranks to screen, sorted by osrank
     */
    public static void outputResults(OsrankResults results, Graph graph, boolean includeAccountNodes, int numTopNodes) {

        System.out.format("Printing Osrank results, includeAccountNodes: %b, numTopNodes: %d%n", includeAccountNodes, numTopNodes);

        Stream<Map.Entry<Integer,Double>> sortedResultsAsStream = prepareResultsStream(results, graph, includeAccountNodes, numTopNodes);

        // Print header
        System.out.format("Name, Id, Osrank\n");

        // Print out the nodes
        sortedResultsAsStream.forEach((entry) -> {
            int nodeId = entry.getKey();
            String nodeName = graph.getNodeById(nodeId).getNodeName();
            double nodeOsrank = entry.getValue();

            System.out.format("%s, %d, %.6f\n", nodeName, nodeId, nodeOsrank);
        });

    }

    /**
     * Helper function to filter and truncate OsrankResult map, if necessary
     */
    private static Stream<Map.Entry<Integer,Double>> prepareResultsStream(OsrankResults results, Graph graph, boolean includeAccountNodes, int numTopNodes) {
        Map<Integer,Double> osrankMap;

        // Exclude account nodes if desired
        if (!includeAccountNodes) {
            osrankMap = results.getOsrankMap().entrySet().stream()
                    .filter(entry -> graph.getNodeById(entry.getKey()) instanceof ProjectNode)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } else {
            osrankMap = results.getOsrankMap();
        }

        // Sort the list
        Stream<Map.Entry<Integer,Double>> sortedResultsAsStream = osrankMap.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));

        // Truncate if asked to do so
        if (numTopNodes > 0) { sortedResultsAsStream = sortedResultsAsStream.limit(numTopNodes); }

        return sortedResultsAsStream;
    }
}