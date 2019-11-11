package io.oscoin.loader;

import io.oscoin.graph.Graph;
import io.oscoin.graph.Node;
import io.oscoin.graph.ProjectNode;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class FileGraphLoaderTest {

    @Test
    public void testFileGraphLoaderOnlyMetadata() {

        String metadataFilename = "./metadata.csv";

        Graph graph = FileGraphLoader.load(metadataFilename, null, null, false);

        // Check that there are lots of nodes
        List<Node> nodes = graph.getAllNodes();
        System.out.println("Total nodes in graph " + nodes.size());
        assert(nodes.size()==16221);
    }

    @Test
    public void testFileGraphLoaderMetadataAndDependencies() {

        String metadataFilename = "./metadata.csv";
        String dependenciesFilename = "./dependencies.csv";

        Graph graph = FileGraphLoader.load(metadataFilename, dependenciesFilename, null, false);

        // Check that there are lots of dependencies
        Integer numTotalDependencies = graph
                .getAllNodes()
                .stream()
                .filter(node -> node instanceof ProjectNode)
                .map(node -> ((ProjectNode) node).getDependencies().size())
                .reduce(0, Integer::sum);


        System.out.println("Total dependencies in graph " + numTotalDependencies);
        assert(numTotalDependencies>20000);
    }

    @Test
    public void testFileGraphLoaderMetadataDependenciesAndContributors() {

        String metadataFilename = "./metadata.csv";
        String dependenciesFilename = "./dependencies.csv";
        String contributionsFilename = "./contributions.csv";

        Graph graph = FileGraphLoader.load(metadataFilename, dependenciesFilename, contributionsFilename, false);

        // Check that there are lots of nodes
        List<Node> nodes = graph.getAllNodes();
        System.out.println("Total nodes in graph " + nodes.size());
        assert(nodes.size()==20960);
    }

    @Test
    public void testFileGraphLoaderAll() {

        String metadataFilename = "./metadata.csv";
        String dependenciesFilename = "./dependencies.csv";
        String contributionsFilename = "./contributions.csv";

        Graph graph = FileGraphLoader.load(metadataFilename, dependenciesFilename, contributionsFilename, true);

        // Check that there are lots of nodes
        List<Node> nodes = graph.getAllNodes();
        System.out.println("Total nodes in graph " + nodes.size());
        assert(nodes.size()==24640);
    }

}