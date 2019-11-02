package io.oscoin.algo;

public class OsrankParams {

    private int R;

    private double projectDampingFactor;

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
