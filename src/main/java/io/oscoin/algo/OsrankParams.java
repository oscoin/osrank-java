package io.oscoin.algo;

/**
 * Data structure class that holds configuration parameters for running the Osrank algorithm.
 */
public class OsrankParams {

    // The R value to use for the Osrank run. In particular, R is the number of random walks that start from each node
    private int R;

    // The probability that a random walk continues after it has visited each project node
    private double projectDampingFactor;

    // The probability that a random walk continues after it has visited each account node
    private double accountDampingFactor;

    public OsrankParams(int R, double projectDampingFactor, double accountDampingFactor) {
        this.R = R;
        this.projectDampingFactor = projectDampingFactor;
        this.accountDampingFactor = accountDampingFactor;
    }

    public int getR() {
        return R;
    }

    public double getProjectDampingFactor() {
        return projectDampingFactor;
    }

    public double getAccountDampingFactor() {
        return accountDampingFactor;
    }
}
