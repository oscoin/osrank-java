package io.oscoin.graph;

import io.oscoin.algo.OsrankParams;
import io.oscoin.algo.OsrankResults;
import io.oscoin.util.OrderedPair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Instances of AccountNode handle state for a single account node as it is defined in the Oscoin whitepaper.
 * Account nodes can be connected to projects by edges that are "contributor" edges, or "maintainer" edges
 * (representing the corresponding types of relationships) or both.
 *
 */
public class AccountNode extends Node {

    private Map<Integer,Integer> projectToContribCount;

    private Set<Integer> projectsMaintained;

    public AccountNode(Integer nodeId) {
        this(nodeId, null);
    }

    public AccountNode(Integer nodeId, String nodeName) {
        super(nodeId, nodeName);
        projectToContribCount = new HashMap<>();
        projectsMaintained = new HashSet<>();
    }

    public void addProjectContributions(Integer projectId, Integer numContributions) {
        Integer currentContributions = projectToContribCount.get(projectId);
        if (null == currentContributions) projectToContribCount.put(projectId, numContributions);
        else projectToContribCount.put(projectId, currentContributions.intValue() + numContributions.intValue());
    }

    public void addProjectMaintained(Integer projectId) {
        projectsMaintained.add(projectId);
    }

    public void buildConnectedNodeProbs(OsrankParams osrankParams) {

        // Reset connected node probs in case this was called before
        connectedNodeProbs.clear();

        // Convert account weights into ratios; Ignore project weights
        double totalWeight = osrankParams.getAccountMaintainerWeight() + osrankParams.getAccountContributionWeight();
        double maintainerRatio = osrankParams.getAccountMaintainerWeight() / totalWeight;
        double contributionRatio = osrankParams.getAccountContributionWeight() / totalWeight;

        // First get the total contrib count
        int totalContribs = 0;
        for (Integer count : projectToContribCount.values()) {
            totalContribs += count.intValue();
        }

        // Now add the contrib links weighted by count
        for (Map.Entry<Integer,Integer> projectAndContribCount : projectToContribCount.entrySet()) {
            double contribs = (double) projectAndContribCount.getValue();
            double weight = contributionRatio * contribs / totalContribs;
            OrderedPair<Double,Integer> weightAndProjectId = new OrderedPair<>(weight, projectAndContribCount.getKey());
            connectedNodeProbs.add(weightAndProjectId);
        }

        // Now add the maintain links weighted by count
        for (Integer projectId : projectsMaintained) {
            Integer contribsInteger = projectToContribCount.get(projectId);

            // If there are no contributions, we treat it as though there was 1 contribution for the purposes of the algorithm
            double contribs;
            if (contribsInteger == null) contribs = 1;
            else contribs = (double) contribsInteger;

            double weight = maintainerRatio * contribs / totalContribs;
            OrderedPair<Double,Integer> weightAndProjectId = new OrderedPair<>(weight, projectId);
            connectedNodeProbs.add(weightAndProjectId);
        }
    }
}
