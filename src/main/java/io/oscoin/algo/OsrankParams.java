package io.oscoin.algo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Data structure class that holds configuration parameters for running the Osrank algorithm.
 */
public class OsrankParams {

    // The R value to use for the Osrank run. In particular, R is the number of random walks that start from each node
    private Integer R = null;

    // The probability that a random walk continues after it has visited each project node
    private Double projectDampingFactor = null;

    // The probability that a random walk continues after it has visited each account node
    private Double accountDampingFactor = null;

    // Relative path from project root to location of metadata csv
    private String metadataFilePath = null;

    // Relative path from project root to location of dependencies csv
    private String dependenciesFilePath = null;

    // Relative path from project root to location of contributions csv
    private String contributionsFilePath = null;

    // Relative path from project root to location of output file containing osrank results
    private String resultsFilePath = null;

    // Flag if we want to impute/invent maintainers or not
    private Boolean addMaintainersFlag = null;

    // Seed to use for generating randomnesss
    private Long randomSeed = null;

    // Weights
    private Double projectDependencyWeight = null;
    private Double projectMaintainerWeight = null;
    private Double projectContributionWeight = null;
    private Double accountMaintainerWeight = null;
    private Double accountContributionWeight = null;

    public OsrankParams() {
    }

    public OsrankParams(
            Integer R,
            Double projectDampingFactor,
            Double accountDampingFactor,
            String metadataFilePath,
            String dependenciesFilePath,
            String contributionsFilePath,
            String resultsFilePath,
            Boolean addMaintainersFlag,
            Long randomSeed,
            Double projectDependencyWeight,
            Double projectMaintainerWeight,
            Double projectContributionWeight,
            Double accountMaintainerWeight,
            Double accountContributionWeight) {
        this.R = R;
        this.projectDampingFactor = projectDampingFactor;
        this.accountDampingFactor = accountDampingFactor;
        this.metadataFilePath = metadataFilePath;
        this.dependenciesFilePath = dependenciesFilePath;
        this.contributionsFilePath = contributionsFilePath;
        this.resultsFilePath = resultsFilePath;
        this.addMaintainersFlag = addMaintainersFlag;
        this.randomSeed = randomSeed;
        this.projectDependencyWeight = projectDependencyWeight;
        this.projectMaintainerWeight = projectMaintainerWeight;
        this.projectContributionWeight = projectContributionWeight;
        this.accountMaintainerWeight = accountMaintainerWeight;
        this.accountContributionWeight = accountContributionWeight;
    }

    public int getR() {
        return (R != null) ? R : 0;
    }

    public double getProjectDampingFactor() {
        return (projectDampingFactor != null) ? projectDampingFactor : 0d;
    }

    public double getAccountDampingFactor() {
        return (accountDampingFactor != null) ? accountDampingFactor : 0d;
    }

    public String getContributionsFilePath() {
        return contributionsFilePath;
    }

    public String getDependenciesFilePath() {
        return dependenciesFilePath;
    }

    public String getMetadataFilePath() {
        return metadataFilePath;
    }

    public String getResultsFilePath() {
        return resultsFilePath;
    }

    public Boolean getAddMaintainersFlag() {
        return addMaintainersFlag;
    }

    public Long getRandomSeed() {
        return (randomSeed != null) ? randomSeed : 0;
    }

    public Double getProjectDependencyWeight() {
        return (projectDependencyWeight != null) ? projectDependencyWeight : 0d;
    }

    public Double getProjectMaintainerWeight() {
        return (projectMaintainerWeight != null) ? projectMaintainerWeight : 0d;
    }

    public Double getProjectContributionWeight() {
        return (projectContributionWeight != null) ? projectContributionWeight : 0d;
    }

    public Double getAccountMaintainerWeight() {
        return (accountMaintainerWeight != null) ? accountMaintainerWeight : 0d;
    }

    public Double getAccountContributionWeight() {
        return (accountContributionWeight != null) ? accountContributionWeight : 0d;
    }

    /**
     * @return Pretty string representation of parameter set
     */
    public String toString() {
        // TODO add weights
        return String.format("R:%s,\nprojectDampingFactor:%s,\naccountDampingFactor:%s,\nmetadataFilePath:%s,\ndependenciesFilePath:%s,\ncontributionsFilePath:%s,\nresultsFilePath:%s,\naddMaintainersFlag:%s,\nrandomSeed:%s\n",
            this.getR(), this.getProjectDampingFactor(), this.getAccountDampingFactor(), this.getMetadataFilePath(), this.getDependenciesFilePath(), this.getContributionsFilePath(), this.getResultsFilePath(), this.getAddMaintainersFlag(), this.getRandomSeed());
    }

    /**
     * Validates parameters, outputs messages to console for invalid parameters
     * @return True if params are valid, False otherwise
     */
    public boolean validate() {
        boolean valid = true;

        if (null == R) {
            valid = false;
            System.out.println("Missing parameter: R");
        }

        if (null == projectDampingFactor) {
            valid = false;
            System.out.println("Missing parameter: projectDampingFactor");
        }

        if (null == accountDampingFactor) {
            valid = false;
            System.out.println("Missing parameter: accountDampingFactor");
        }

        if (null == metadataFilePath) {
            valid = false;
            System.out.println("Missing parameter: metadataFilePath");
        }

        if (null == dependenciesFilePath) {
            valid = false;
            System.out.println("Missing parameter: dependenciesFilePath");
        }

        if (null == resultsFilePath) {
            valid = false;
            System.out.println("Missing parameter: resultsFilePath");
        }

        if (null == addMaintainersFlag) {
            valid = false;
            System.out.println("Missing parameter: addMaintainersFlag");
        }

        if (null == randomSeed) {
            valid = false;
            System.out.println("Missing parameter: randomSeed");
        }

        // TODO add weights!

        return valid;
    }

    // Factory method to combine default parameter values and command-line parameter values.
    // Command-line values take precedence.
    public static OsrankParams getInstance(String [] args, OsrankParams defaultOsRankParams) {
        OsrankParams commandLineParams = getInstance(args);

        // Merge the params.
        Integer R = commandLineParams.getR() > 0 ? commandLineParams.getR() : defaultOsRankParams.getR();
        Double projectDampingFactor = commandLineParams.getProjectDampingFactor() > 0.0 ? commandLineParams.getProjectDampingFactor() : defaultOsRankParams.getProjectDampingFactor();
        Double accountDampingFactor = commandLineParams.getAccountDampingFactor() > 0.0 ? commandLineParams.getAccountDampingFactor() : defaultOsRankParams.getAccountDampingFactor();
        String metadataFilePath = commandLineParams.getMetadataFilePath() != null ? commandLineParams.getMetadataFilePath() : defaultOsRankParams.getMetadataFilePath();
        String dependenciesFilePath = commandLineParams.getDependenciesFilePath() != null ? commandLineParams.getDependenciesFilePath() : defaultOsRankParams.getDependenciesFilePath();
        String contributionsFilePath = commandLineParams.getContributionsFilePath() != null ? commandLineParams.getContributionsFilePath() : defaultOsRankParams.getContributionsFilePath();
        String resultsFilePath = commandLineParams.getResultsFilePath() != null ? commandLineParams.getResultsFilePath() : defaultOsRankParams.getResultsFilePath();
        Boolean addMaintainersFlag = commandLineParams.getAddMaintainersFlag() != null ? commandLineParams.getAddMaintainersFlag() : defaultOsRankParams.getAddMaintainersFlag();
        Long randomSeed = commandLineParams.getRandomSeed() > 0 ? commandLineParams.getRandomSeed() : defaultOsRankParams.getRandomSeed();
        Double projectDependencyWeight = commandLineParams.getProjectDependencyWeight() > 0 ? commandLineParams.getProjectDependencyWeight() : defaultOsRankParams.getProjectDependencyWeight();
        Double projectMaintainerWeight = commandLineParams.getProjectMaintainerWeight() > 0 ? commandLineParams.getProjectMaintainerWeight() : defaultOsRankParams.getProjectMaintainerWeight();
        Double projectContributionWeight = commandLineParams.getProjectContributionWeight() > 0 ? commandLineParams.getProjectContributionWeight() : defaultOsRankParams.getProjectContributionWeight();
        Double accountMaintainerWeight = commandLineParams.getAccountMaintainerWeight() > 0 ? commandLineParams.getAccountMaintainerWeight() : defaultOsRankParams.getAccountMaintainerWeight();
        Double accountContributionWeight = commandLineParams.getAccountContributionWeight() > 0 ? commandLineParams.getAccountContributionWeight() : defaultOsRankParams.getAccountContributionWeight();

        // Create a new object with the merged params.
        return new OsrankParams(
            R,
            projectDampingFactor,
            accountDampingFactor,
            metadataFilePath,
            dependenciesFilePath,
            contributionsFilePath,
            resultsFilePath,
            addMaintainersFlag,
            randomSeed,
            projectDependencyWeight,
            projectMaintainerWeight,
            projectContributionWeight,
            accountMaintainerWeight,
            accountMaintainerWeight);
    }

    // Factory method to generate params object from command-line values.
    // Use reflection to determine if command-line values are valid options.
    public static OsrankParams getInstance(String [] args) {
        OsrankParams params = new OsrankParams();

        Class<?> paramsClass = params.getClass();
        Field field = null;

        for (String arg: args) {
            // Is the argument correctly formatted?
            if (arg.contains("=")) {
                String[] tokenizedArgument = arg.split("=", 2);
                // Do we recognize the parameter?
                try {
                    field = paramsClass.getDeclaredField(tokenizedArgument[0]);
                } catch (NoSuchFieldException x) {
                    field = null;
                }
                if (field != null) {
                    // Is the value valid?
                    try {
                        field.set(params, field.getType().getConstructor(new Class[] {String.class }).newInstance(tokenizedArgument[1]));
                    } catch (IllegalAccessException | IllegalArgumentException | ClassCastException | NoSuchMethodException | InstantiationException | InvocationTargetException x) {
                        System.out.format("Illegal value %s passed for parameter %s", tokenizedArgument[1], tokenizedArgument[0]);
                    }
                }
            }
        }

        return params;
    }

}
