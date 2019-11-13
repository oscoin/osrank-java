package io.oscoin.graph;

import io.oscoin.loader.SimpleGraphLoader;
import io.oscoin.util.MathUtils;
import io.oscoin.util.OrderedPair;
import org.junit.Test;

import java.util.List;

public class ProjectNodeTest {

    @Test
    public void testBuildConnectedNodeProbabilitiesAndNormalize() {

        ProjectNode projectNode1 = new ProjectNode(1);
        ProjectNode projectNode2 = new ProjectNode(2);

        projectNode1.addProjectDependency(2);
        projectNode1.addProjectContributor(3, 10000);
        projectNode1.addProjectMaintainer(4);

        projectNode1.buildConnectedNodeProbs();

        assert(projectNode1.connectedNodeProbs.size() == 3);

        for (OrderedPair<Double,Integer> oneConnectedNodeProb : projectNode1.connectedNodeProbs) {
            if (oneConnectedNodeProb.right == 2) assert(oneConnectedNodeProb.left.equals((4d / 7d)));
            else if (oneConnectedNodeProb.right == 3) assert(oneConnectedNodeProb.left.equals((1d / 7d)));
            else if (oneConnectedNodeProb.right == 4) assert(oneConnectedNodeProb.left.equals((2d / 7d)));
        }

        projectNode1.normalizeEdgeProbabilities();
        double sumOfEdgeProbabilities = projectNode1.getSumOfEdgeProbabilities();
        assert(MathUtils.approxEqual(sumOfEdgeProbabilities, 1d));
    }
}