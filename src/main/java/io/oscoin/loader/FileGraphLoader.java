package io.oscoin.loader;

import io.oscoin.graph.AccountNode;
import io.oscoin.graph.Graph;
import io.oscoin.graph.Node;
import io.oscoin.graph.ProjectNode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileGraphLoader {


    public static Graph load(String metadataFilename, String dependenciesFilename, String contributionsFilename) {

        Graph graph = new Graph();

        // Add project metadata to Graph
        FileGraphLoader.addMetadataToGraph(graph, metadataFilename);

        // Add project dependencies to Graph
        FileGraphLoader.addDependenciesToGraph(graph, dependenciesFilename);

        // Add contributions to Graph
        FileGraphLoader.addContributionsToGraph(graph, contributionsFilename);

        // Preprocess
        graph.buildAndNormalizeAllNodes();

        return graph;
    }

    private static void addMetadataToGraph(Graph graph, String metadataFilename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(metadataFilename));
            String line = reader.readLine(); // This is CSV header, discard TODO make this optional
            line = reader.readLine();
            while (line != null) {
                // Prepare CSV line
                String[] tokens = line.split(",");
                Integer nodeId = Integer.parseInt(tokens[0]);
                String nodeName = tokens[1]; // TODO check if this is defined

                // Add to Graph
                ProjectNode projectNode = new ProjectNode(nodeId, nodeName);
                graph.addProjectNode(projectNode);

                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addDependenciesToGraph(Graph graph, String dependenciesFilename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dependenciesFilename));
            Integer numRejectedDependencies = 0;
            String line = reader.readLine(); // This is CSV header, discard TODO make this optional
            line = reader.readLine();
            while (line != null) {
                // Prepare CSV line
                String[] tokens = line.split(",");
                Integer fromId = Integer.parseInt(tokens[0]);
                Integer toId = Integer.parseInt(tokens[1]);

                // Add to Graph
                if ((graph.getNodeById(fromId) != null) && (graph.getNodeById(toId) != null)) {
                    ((ProjectNode)graph.getNodeById(fromId)).addProjectDependency(toId);
                } else {
                    //System.out.println("Did not like dependency! " + line);
                    numRejectedDependencies++;
                }

                // read next line
                line = reader.readLine();
            }
            reader.close();

            System.out.println("Did not like dependencies " + numRejectedDependencies);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void addContributionsToGraph(Graph graph, String contributionsFilename) {
        try {
            Map<String,AccountNode> contributorNameToNodeMap = new HashMap<>();
            AccountNode contributorNode;
            ProjectNode projectNode;

            BufferedReader reader = new BufferedReader(new FileReader(contributionsFilename));
            Integer nextContributorId = 1;
            String line = reader.readLine(); // This is CSV header, discard TODO make this optional
            line = reader.readLine();

            while (line != null) {
                // Prepare CSV line
                String[] tokens = line.split(",");
                Integer projectId = Integer.parseInt(tokens[0]);
                String contributorName = tokens[1];
                Integer numContributions = Integer.parseInt(tokens[3]);

                // Make sure contributor is in the graph
                contributorNode = contributorNameToNodeMap.get(contributorName);
                if (null == contributorNode) {
                    contributorNode = new AccountNode(nextContributorId++, contributorName);
                    contributorNameToNodeMap.put(contributorName, contributorNode);
                    graph.addAccountNode(contributorNode);
                }

                // Add contributions to project's node, if it exists
                projectNode = (ProjectNode) graph.getNodeById(projectId);
                if (projectNode != null) {
                    projectNode.addProjectContributor(contributorNode.getNodeId(), numContributions);

                    // Add contributions to contributor's node
                    contributorNode.addProjectContributions(projectId, numContributions);
                }

                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
