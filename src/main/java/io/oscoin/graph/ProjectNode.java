package io.oscoin.graph;

import io.oscoin.util.OrderedPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Extends Node to handle state and behavior for graph nodes that represent software projects in the graph, as
 * outlined in the Oscoin whitepaper.
 *
 */
public class ProjectNode extends Node {

    private Map<Integer,Integer> contributorToContribCount;

    private List<Integer> maintainers;

    private List<Integer> dependencies;

    public ProjectNode(Integer nodeId) {
        this(nodeId, null);
    }

    public ProjectNode(Integer nodeId, String nodeName) {
        super(nodeId, nodeName);
        contributorToContribCount = new HashMap<>();
        maintainers = new ArrayList<>();
        dependencies = new ArrayList<>();
    }

    public void addProjectContributor(Integer accountId, Integer numContributions) {
        Integer currentContributions = contributorToContribCount.get(accountId);
        if (null == currentContributions) contributorToContribCount.put(accountId, numContributions);
        else contributorToContribCount.put(accountId, currentContributions.intValue() + numContributions.intValue());
    }

    public Map<Integer,Integer> getContributorsMap() {
        return contributorToContribCount;
    }

    public List<Integer> getDependencies() { return dependencies; }

    public void addProjectMaintainer(Integer maintainerId) {
        maintainers.add(maintainerId);
    }

    public void addProjectDependency(Integer dependencyId) {dependencies.add(dependencyId); }

    /**
     * Use the given relative weights to build node probabilities.
     */
    public void buildConnectedNodeProbs(
            int projectDependencyWeight,
            int projectMaintainerWeight,
            int projectContributionWeight,
            int accountMaintainerWeight,
            int accountContributionWeight) {
        // Clear connected node probs in case this method was called before
        connectedNodeProbs.clear();

        // Convert project weights into ratios, ignore account weights
        double totalWeight = projectDependencyWeight + projectMaintainerWeight + projectContributionWeight;
        double dependencyRatio = projectDependencyWeight / totalWeight;
        double maintainerRatio = projectMaintainerWeight / totalWeight;
        double contributionRatio = projectContributionWeight / totalWeight;

        // First get the total contrib count
        int totalContribs = 0;
        for (Integer count : contributorToContribCount.values()) {
            totalContribs += count.intValue();
        }
        double totalContribsDouble = (double) totalContribs;

        // Now build the dependency links
        double numDependencies = (double) dependencies.size();
        for (Integer dependencyId : dependencies) {
            double weight = dependencyRatio / numDependencies;
            OrderedPair<Double,Integer> weightAndProjectId = new OrderedPair<>(weight, dependencyId);
            connectedNodeProbs.add(weightAndProjectId);
        }

        // Now add the maintainer links
        double numMaintainers = (double) maintainers.size();
        for (Integer maintainerId : maintainers) {
            double weight = maintainerRatio / numMaintainers;
            OrderedPair<Double,Integer> weightAndProjectId = new OrderedPair<>(weight, maintainerId);
            connectedNodeProbs.add(weightAndProjectId);
        }

        // Finally add the contrib links weighted by count
        for (Map.Entry<Integer,Integer> projectAndContribCount : contributorToContribCount.entrySet()) {
            double contribs = (double) projectAndContribCount.getValue();
            double weight = contributionRatio * contribs / totalContribs;
            OrderedPair<Double,Integer> weightAndProjectId = new OrderedPair<>(weight, projectAndContribCount.getKey());
            connectedNodeProbs.add(weightAndProjectId);
        }
    }
}
