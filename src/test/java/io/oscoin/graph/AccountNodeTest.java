package io.oscoin.graph;

import io.oscoin.algo.OsrankParams;
import io.oscoin.util.MathUtils;
import io.oscoin.util.OrderedPair;
import org.junit.Test;

public class AccountNodeTest {

    @Test
    public void testBuildConnectedNodeProbabilitiesAndNormalize() {

        AccountNode accountNode = new AccountNode(1);
        ProjectNode projectNode = new ProjectNode(2);

        accountNode.addProjectContributions(1, 10000);
        accountNode.addProjectMaintained(1);

        accountNode.buildConnectedNodeProbs(OsrankParams.buildBasicOsrankParams());

        assert(accountNode.connectedNodeProbs.size() == 2);

        for (OrderedPair<Double,Integer> oneConnectedNodeProb : accountNode.connectedNodeProbs) {
            if (oneConnectedNodeProb.right == 2) assert(oneConnectedNodeProb.left.equals(2d) || oneConnectedNodeProb.left.equals(3d));
        }

        accountNode.normalizeEdgeProbabilities();
        double sumOfEdgeProbabilities = accountNode.getSumOfEdgeProbabilities();
        assert(MathUtils.approxEqual(sumOfEdgeProbabilities, 1d));
    }
}
