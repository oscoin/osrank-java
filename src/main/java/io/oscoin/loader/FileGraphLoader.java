package io.oscoin.loader;

import io.oscoin.graph.AccountNode;
import io.oscoin.graph.Graph;
import io.oscoin.graph.Node;
import io.oscoin.graph.ProjectNode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileGraphLoader {


    public static Graph load(String metadataFilename, String dependenciesFilename, String contributionsFilename) {

        Graph graph = new Graph();

        // Add project metadata to Graph
        FileGraphLoader.addMetadataToGraph(graph, metadataFilename);

        // Add project dependencies to Graph
        FileGraphLoader.addDependenciesToGraph(graph, dependenciesFilename);

        // Add contributions to Graph
        FileGraphLoader.addContributionsToGraph(graph, contributionsFilename);

        // Add maintainers to Graph
        FileGraphLoader.addMaintainersToGraph(graph);

        // Preprocess
        graph.buildAndNormalizeAllNodes();

        return graph;
    }

    // Create one ProjectNode for each line in the metadata CSV
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

            System.out.println("# Projects From Metadata " + graph.getAllNodes().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create a dependency link for each line in the CSV, if both nodes already exist
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

            System.out.println("# Rejected Dependencies " + numRejectedDependencies);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Add a bi-directional contributor links for each line in CSV, if project exists
    // Must create AccountNode for each new contributor
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

                // Add contributions to project's node, if project exists
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

            System.out.println("# Contributors " + contributorNameToNodeMap.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Since we don't have maintainers information, we just pick the top contributor as a maintainer
    // For projects that have no contributors, we invent a maintainer
    private static void addMaintainersToGraph(Graph graph) {

        Integer numMaintainersInvented = 0;

        // Get only project nodes out of graph
        List<ProjectNode> projectNodeList = graph
            .getAllNodes()
            .stream()
            .filter(node -> node instanceof ProjectNode)
            .map(node -> (ProjectNode) node)
            .collect(Collectors.toList());

        // Go through all projects and find a maintainer for each one
        Map<Integer,Integer> contributorsMap;
        AccountNode maintainer;
        for (ProjectNode projectNode : projectNodeList) {
            contributorsMap = projectNode.getContributorsMap();
            if (contributorsMap.isEmpty()) {
                // When there are no contributors, invent one.
                maintainer = new AccountNode(-projectNode.getNodeId(), "Fake maintainer for " + projectNode.getNodeName());
                graph.addAccountNode(maintainer);

                // Add contribution links to fake maintainer
                projectNode.addProjectContributor(maintainer.getNodeId(), 1); // Init with a single contribution.
                maintainer.addProjectContributions(projectNode.getNodeId(), 1);

                numMaintainersInvented++;
            } else {
                // When there are contributors, choose the one with the most contributions.
                Map.Entry<Integer, Integer> maxContributor = Collections.max(
                        projectNode.getContributorsMap().entrySet(),
                        (Map.Entry<Integer, Integer> e1, Map.Entry<Integer, Integer> e2) -> e1.getValue().compareTo(e2.getValue())
                );

                maintainer = (AccountNode) graph.getNodeById(maxContributor.getKey());
            }

            // Add maintainer links
            projectNode.addProjectMaintainer(maintainer.getNodeId());
            maintainer.addProjectMaintained(projectNode.getNodeId());
        }

        System.out.println("# Invented Maintainers " + numMaintainersInvented);

    }


}
