package io.oscoin.graph;

import io.oscoin.util.OrderedPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectNode extends Node {

    private Map<Integer,Integer> contributorToContribCount;

    private List<Integer> maintainers;

    private List<Integer> dependencies;

    public ProjectNode(Integer nodeId) {
        super(nodeId);
        contributorToContribCount = new HashMap<>();
        maintainers = new ArrayList<>();
        dependencies = new ArrayList<>();
    }

    public void addProjectContributor(Integer accountId, Integer numContributions) {
        Integer currentContributions = contributorToContribCount.get(accountId);
        if (null == currentContributions) contributorToContribCount.put(accountId, numContributions);
        else contributorToContribCount.put(accountId, currentContributions.intValue() + numContributions.intValue());
    }

    public void addProjectMaintainer(Integer maintainerId) {
        maintainers.add(maintainerId);
    }

    public void buildConnectedNodeProbs() {

        // First get the total contrib count
        int totalContribs = 0;
        for (Integer count : contributorToContribCount.values()) {
            totalContribs += count.intValue();
        }
        double totalContribsDouble = (double) totalContribs;

        // Now build the dependency links
        double numDependencies = (double) dependencies.size();
        for (Integer dependencyId : dependencies) {
            double weight = (4d / 7d) / (numDependencies);
            OrderedPair<Double,Integer> weightAndProjectId = new OrderedPair<>(weight, dependencyId);
            connectedNodeProbs.add(weightAndProjectId);
        }

        // Now add the maintainer links
        double numMaintainers = (double) maintainers.size();
        for (Integer maintainerId : maintainers) {
            double weight = (2d / 7d) / (numMaintainers);
            OrderedPair<Double,Integer> weightAndProjectId = new OrderedPair<>(weight, maintainerId);
            connectedNodeProbs.add(weightAndProjectId);
        }

        // Finally add the contrib links weighted by count
        for (Map.Entry<Integer,Integer> projectAndContribCount : contributorToContribCount.entrySet()) {
            double contribs = (double) projectAndContribCount.getValue();
            double weight = 1d * contribs / totalContribs;
            OrderedPair<Double,Integer> weightAndProjectId = new OrderedPair<>(weight, projectAndContribCount.getKey());
            connectedNodeProbs.add(weightAndProjectId);
        }
    }
}
