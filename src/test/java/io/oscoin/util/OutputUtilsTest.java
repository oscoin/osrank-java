package io.oscoin.util;

import io.oscoin.algo.OsrankParams;
import io.oscoin.algo.OsrankResults;
import io.oscoin.graph.AccountNode;
import io.oscoin.graph.Graph;
import io.oscoin.graph.Node;
import io.oscoin.graph.ProjectNode;
import io.oscoin.loader.SimpleGraphLoader;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;

public class OutputUtilsTest {

    @Test
    public void testWriteAllNodesToCSV() throws Exception {
        // Init
        OsrankParams osrankBasicParams = OsrankParams.buildBasicOsrankParams();
        Graph simpleGraph = SimpleGraphLoader.buildSimpleOneProjectOneContributorGraph(osrankBasicParams);
        OsrankResults osrankResults = new OsrankResults(osrankBasicParams, simpleGraph);
        for (Node node : simpleGraph.getAllNodes()) {
            for (int i = 0; i < node.getNodeId(); i++) {
                osrankResults.incrementVisitCount(node.getNodeId());
            }
        }

        // Write all nodes to file
        String outputUtilsPath = "outputUtilsTest.csv";
        OutputUtils.writeResultsToCSV(osrankResults, simpleGraph,true, 0, outputUtilsPath);

        // Read test values from file
        BufferedReader reader = new BufferedReader(new FileReader(outputUtilsPath));
        String header = reader.readLine();
        String [] first = reader.readLine().split(",");
        String [] second = reader.readLine().split(",");
        String end = reader.readLine();
        reader.close();

        // Check header
        assert(header.equals("Name,Id,Osrank"));

        // Check top two entries having matching names and ids
        assert(first[0].equals(simpleGraph.getNodeById(Integer.parseInt(first[1])).getNodeName()));
        assert(second[0].equals(simpleGraph.getNodeById(Integer.parseInt(second[1])).getNodeName()));

        // Check osrank of first is higher than second
        assert(Double.parseDouble(first[2]) > Double.parseDouble(second[2]));

        // Check only 2 entries in file
        assert(end==null);
    }

    @Test
    public void testWriteProjectNodesToCSV() throws Exception {
        // Init
        OsrankParams osrankBasicParams = OsrankParams.buildBasicOsrankParams();
        Graph simpleGraph = SimpleGraphLoader.buildSimpleOneProjectOneContributorGraph(osrankBasicParams);
        OsrankResults osrankResults = new OsrankResults(osrankBasicParams, simpleGraph);
        for (Node node : simpleGraph.getAllNodes()) {
            for (int i = 0; i < node.getNodeId(); i++) {
                osrankResults.incrementVisitCount(node.getNodeId());
            }
        }

        // Write all nodes to file
        String outputUtilsPath = "outputUtilsTest.csv";
        OutputUtils.writeResultsToCSV(osrankResults, simpleGraph,false, 0, outputUtilsPath);

        // Read test values from file
        BufferedReader reader = new BufferedReader(new FileReader(outputUtilsPath));
        String header = reader.readLine();
        String [] first = reader.readLine().split(",");
        String end = reader.readLine();
        reader.close();

        // Check header
        assert(header.equals("Name,Id,Osrank"));

        // Check top entry has matching name and id
        assert(first[0].equals(simpleGraph.getNodeById(Integer.parseInt(first[1])).getNodeName()));

        // Check top entry is project
        assert(simpleGraph.getNodeById(Integer.parseInt(first[1])) instanceof ProjectNode);

        // Check only 1 entry in file
        assert(end==null);
    }

    @Test
    public void testWriteTopNodeToCSV() throws Exception {
        // Init
        OsrankParams osrankBasicParams = OsrankParams.buildBasicOsrankParams();
        Graph simpleGraph = SimpleGraphLoader.buildSimpleOneProjectOneContributorGraph(osrankBasicParams);
        OsrankResults osrankResults = new OsrankResults(osrankBasicParams, simpleGraph);
        for (Node node : simpleGraph.getAllNodes()) {
            for (int i = 0; i < node.getNodeId(); i++) {
                osrankResults.incrementVisitCount(node.getNodeId());
            }
        }

        // Write all nodes to file
        String outputUtilsPath = "outputUtilsTest.csv";
        OutputUtils.writeResultsToCSV(osrankResults, simpleGraph,true, 1, outputUtilsPath);

        // Read test values from file
        BufferedReader reader = new BufferedReader(new FileReader(outputUtilsPath));
        String header = reader.readLine();
        String [] first = reader.readLine().split(",");
        String end = reader.readLine();
        reader.close();

        // Check header
        assert(header.equals("Name,Id,Osrank"));

        // Check top two entries having matching names and ids
        assert(first[0].equals(simpleGraph.getNodeById(Integer.parseInt(first[1])).getNodeName()));

        // Check top entry is account
        assert(simpleGraph.getNodeById(Integer.parseInt(first[1])) instanceof AccountNode);

        // Check only 2 entries in file
        assert(end==null);
    }

}
