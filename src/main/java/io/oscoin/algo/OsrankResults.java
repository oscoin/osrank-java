package io.oscoin.algo;

import io.oscoin.graph.AccountNode;
import io.oscoin.graph.Graph;
import io.oscoin.graph.Node;
import io.oscoin.graph.ProjectNode;
import io.oscoin.util.Counter;

import java.util.HashMap;
import java.util.Map;

public class OsrankResults {

    private OsrankParams osrankParams;
    private Graph graph;
    private Map<Integer, Counter> nodeIdToCounterMap;
    double totalWalks;

    public OsrankResults(OsrankParams osrankParams, Graph graph) {
        this.osrankParams = osrankParams;
        this.graph = graph;
        nodeIdToCounterMap = new HashMap<>();
        this.totalWalks = (double) (graph.getAllNodes().size() * osrankParams.getR());
    }

    private Counter getCounter(int nodeId) {
        Counter counter = nodeIdToCounterMap.get(nodeId);
        if (null == counter) {
            counter = new Counter();
            nodeIdToCounterMap.put(nodeId, counter);
        }
        return counter;
    }

    public void incrementVisitCount(int nodeIdToIncrement) {
        Counter counter = getCounter(nodeIdToIncrement);
        counter.incrementCount();
    }

    public Map<Integer,Double> getOsrankMap() {
        Map<Integer,Double> osrankMap = new HashMap<>();
        for (Map.Entry<Integer,Counter> entry : nodeIdToCounterMap.entrySet()) {

            Node node = graph.getNodeById(entry.getKey());
            double dampingFactor;
            if (node instanceof ProjectNode) dampingFactor = osrankParams.getProjectDampingFactor();
            else dampingFactor = osrankParams.getAccountDampingFactor();

            double osrank = ((double) entry.getValue().getCount()) * (1d - dampingFactor) / ((double) totalWalks);
            osrankMap.put(entry.getKey(), osrank);
        }
        return osrankMap;
    }
}
