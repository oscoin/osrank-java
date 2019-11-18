package io.oscoin.algo;

import org.junit.Test;

public class OsrankParamsTest {

    private static final int R = 1000;
    private static final double PROJECT_DAMPING_FACTOR = 0.85d;
    private static final double ACCOUNT_DAMPING_FACTOR = 0.85d;
    private static final String METADATA_FILE_PATH = "!";
    private static final String DEPENDENCIES_FILE_PATH = "!";
    private static final String CONTRIBUTIONS_FILE_PATH = "!";
    private static final String RESULTS_FILE_PATH = "!";
    private static final Boolean ADD_MAINTAINERS = false;
    private static final long RANDOM_SEED = 842384239487239l;
    private static final double PROJECT_DEPENDENCY_WEIGHT = 4d;
    private static final double PROJECT_MAINTAINER_WEIGHT = 0d;
    private static final double PROJECT_CONTRIBUTION_WEIGHT = 1d;
    private static final double ACCOUNT_MAINTAINER_WEIGHT = 3d;
    private static final double ACCOUNT_CONTRIBUTION_WEIGHT = 2d;

    @Test
    public void testGetInstance() {
        // Get defaults
        OsrankParams osrankDefaultParams = OsrankParams.buildBasicOsrankParams();

        // make sure the properties we're testing are set
        assert(osrankDefaultParams.getR()>0);
        assert(osrankDefaultParams.getProjectDampingFactor()>0);
        assert(osrankDefaultParams.getAccountContributionWeight()>0);
        assert(osrankDefaultParams.getAccountDampingFactor()>0);
        assert(osrankDefaultParams.getAccountMaintainerWeight()>0);

        // Pretend command line args are a little higher than defaults
        String [] args = new String[] {
            "R=" + (osrankDefaultParams.getR()+1),
            "projectDampingFactor=" + (osrankDefaultParams.getProjectDampingFactor()+1),
            "accountContributionWeight=" + (osrankDefaultParams.getAccountContributionWeight()+1)
        };

        // Test just command line args
        OsrankParams osrankParams = OsrankParams.getInstance(args);

        // these come from command line
        assert(osrankParams.getR()==osrankDefaultParams.getR()+1);
        assert(osrankParams.getProjectDampingFactor()==osrankDefaultParams.getProjectDampingFactor()+1);
        assert(osrankParams.getAccountContributionWeight()==osrankDefaultParams.getAccountContributionWeight()+1);

        // these are unset
        assert(osrankParams.getAccountDampingFactor()==0);
        assert(osrankParams.getAccountMaintainerWeight()==0);


        // Now merge with defaults
        OsrankParams osrankMergedParams = OsrankParams.getInstance(args, osrankDefaultParams);

        // these come from command line
        assert(osrankMergedParams.getR()==osrankDefaultParams.getR()+1);
        assert(osrankMergedParams.getProjectDampingFactor()==osrankDefaultParams.getProjectDampingFactor()+1);
        assert(osrankMergedParams.getAccountContributionWeight()==osrankDefaultParams.getAccountContributionWeight()+1);

        // these comes from defaults
        assert(osrankMergedParams.getAccountDampingFactor()==osrankDefaultParams.getAccountDampingFactor());
        assert(osrankMergedParams.getAccountMaintainerWeight()==osrankDefaultParams.getAccountMaintainerWeight());


    }


    @Test
    public void testToString() {
        OsrankParams osrankParams = OsrankParams.buildBasicOsrankParams();
        String s = osrankParams.toString();
        System.out.println(s);

        assert(s.contains("projectDampingFactor"));
        assert(s.contains("accountDampingFactor"));
        assert(s.contains("metadataFilePath"));
        assert(s.contains("dependenciesFilePath"));
        assert(s.contains("contributionsFilePath"));
        assert(s.contains("resultsFilePath"));
        assert(s.contains("addMaintainersFlag"));
        assert(s.contains("randomSeed"));
        assert(s.contains("projectDependencyWeight"));
        assert(s.contains("projectMaintainerWeight"));
        assert(s.contains("projectContributionWeight"));
        assert(s.contains("accountMaintainerWeight"));
        assert(s.contains("accountContributionWeight"));
    }

    @Test
    public void testValidate() {
        Boolean isValid = null;

        // Test with all params
        isValid = new OsrankParams(
                R,
                PROJECT_DAMPING_FACTOR,
                ACCOUNT_DAMPING_FACTOR,
                METADATA_FILE_PATH,
                DEPENDENCIES_FILE_PATH,
                CONTRIBUTIONS_FILE_PATH,
                RESULTS_FILE_PATH,
                ADD_MAINTAINERS,
                RANDOM_SEED,
                PROJECT_DEPENDENCY_WEIGHT,
                PROJECT_MAINTAINER_WEIGHT,
                PROJECT_CONTRIBUTION_WEIGHT,
                ACCOUNT_MAINTAINER_WEIGHT,
                ACCOUNT_CONTRIBUTION_WEIGHT
        ).validate();
        assert(isValid == true);

        // Test with all mandatory params and no optional ones
        isValid = new OsrankParams(
                R,
                PROJECT_DAMPING_FACTOR,
                ACCOUNT_DAMPING_FACTOR,
                METADATA_FILE_PATH,
                DEPENDENCIES_FILE_PATH,
                null,
                RESULTS_FILE_PATH,
                ADD_MAINTAINERS,
                RANDOM_SEED,
                PROJECT_DEPENDENCY_WEIGHT,
                PROJECT_MAINTAINER_WEIGHT,
                PROJECT_CONTRIBUTION_WEIGHT,
                ACCOUNT_MAINTAINER_WEIGHT,
                ACCOUNT_CONTRIBUTION_WEIGHT
        ).validate();
        assert(isValid == true);

        // Test with missing mandatory param: METADATA_FILE_PATH
        isValid = new OsrankParams(
                R,
                PROJECT_DAMPING_FACTOR,
                ACCOUNT_DAMPING_FACTOR,
                null,
                DEPENDENCIES_FILE_PATH,
                null,
                RESULTS_FILE_PATH,
                ADD_MAINTAINERS,
                RANDOM_SEED,
                PROJECT_DEPENDENCY_WEIGHT,
                PROJECT_MAINTAINER_WEIGHT,
                PROJECT_CONTRIBUTION_WEIGHT,
                ACCOUNT_MAINTAINER_WEIGHT,
                ACCOUNT_CONTRIBUTION_WEIGHT
        ).validate();
        assert(isValid == false);

        // Test with missing mandatory param: dependenciesFilePath
        isValid = new OsrankParams(
                R,
                PROJECT_DAMPING_FACTOR,
                ACCOUNT_DAMPING_FACTOR,
                METADATA_FILE_PATH,
                null,
                null,
                RESULTS_FILE_PATH,
                ADD_MAINTAINERS,
                RANDOM_SEED,
                PROJECT_DEPENDENCY_WEIGHT,
                PROJECT_MAINTAINER_WEIGHT,
                PROJECT_CONTRIBUTION_WEIGHT,
                ACCOUNT_MAINTAINER_WEIGHT,
                ACCOUNT_CONTRIBUTION_WEIGHT
        ).validate();
        assert(isValid == false);

        // Test with missing mandatory param: resultsFilePath
        isValid = new OsrankParams(
                R,
                PROJECT_DAMPING_FACTOR,
                ACCOUNT_DAMPING_FACTOR,
                METADATA_FILE_PATH,
                DEPENDENCIES_FILE_PATH,
                null,
                null,
                ADD_MAINTAINERS,
                RANDOM_SEED,
                PROJECT_DEPENDENCY_WEIGHT,
                PROJECT_MAINTAINER_WEIGHT,
                PROJECT_CONTRIBUTION_WEIGHT,
                ACCOUNT_MAINTAINER_WEIGHT,
                ACCOUNT_CONTRIBUTION_WEIGHT
        ).validate();
        assert(isValid == false);

        // Test with missing mandatory param: addMaintainers
        isValid = new OsrankParams(
                R,
                PROJECT_DAMPING_FACTOR,
                ACCOUNT_DAMPING_FACTOR,
                METADATA_FILE_PATH,
                DEPENDENCIES_FILE_PATH,
                null,
                RESULTS_FILE_PATH,
                null,
                RANDOM_SEED,
                PROJECT_DEPENDENCY_WEIGHT,
                PROJECT_MAINTAINER_WEIGHT,
                PROJECT_CONTRIBUTION_WEIGHT,
                ACCOUNT_MAINTAINER_WEIGHT,
                ACCOUNT_CONTRIBUTION_WEIGHT
        ).validate();
        assert(isValid == false);

        // Test with missing mandatory param: randomSeed
        isValid = new OsrankParams(
                R,
                PROJECT_DAMPING_FACTOR,
                ACCOUNT_DAMPING_FACTOR,
                METADATA_FILE_PATH,
                DEPENDENCIES_FILE_PATH,
                null,
                RESULTS_FILE_PATH,
                ADD_MAINTAINERS,
                null,
                PROJECT_DEPENDENCY_WEIGHT,
                PROJECT_MAINTAINER_WEIGHT,
                PROJECT_CONTRIBUTION_WEIGHT,
                ACCOUNT_MAINTAINER_WEIGHT,
                ACCOUNT_CONTRIBUTION_WEIGHT
        ).validate();
        assert(isValid == false);

        // Test with missing mandatory param: projectDependencyWeight
        isValid = new OsrankParams(
                R,
                PROJECT_DAMPING_FACTOR,
                ACCOUNT_DAMPING_FACTOR,
                METADATA_FILE_PATH,
                DEPENDENCIES_FILE_PATH,
                null,
                RESULTS_FILE_PATH,
                ADD_MAINTAINERS,
                RANDOM_SEED,
                null,
                PROJECT_MAINTAINER_WEIGHT,
                PROJECT_CONTRIBUTION_WEIGHT,
                ACCOUNT_MAINTAINER_WEIGHT,
                ACCOUNT_CONTRIBUTION_WEIGHT
        ).validate();
        assert(isValid == false);

        // Test with missing mandatory param: projectMaintainerWeight
        isValid = new OsrankParams(
                R,
                PROJECT_DAMPING_FACTOR,
                ACCOUNT_DAMPING_FACTOR,
                METADATA_FILE_PATH,
                DEPENDENCIES_FILE_PATH,
                null,
                RESULTS_FILE_PATH,
                ADD_MAINTAINERS,
                RANDOM_SEED,
                PROJECT_DEPENDENCY_WEIGHT,
                null,
                PROJECT_CONTRIBUTION_WEIGHT,
                ACCOUNT_MAINTAINER_WEIGHT,
                ACCOUNT_CONTRIBUTION_WEIGHT
        ).validate();
        assert(isValid == false);

        // Test with missing mandatory param: projectContributionWeight
        isValid = new OsrankParams(
                R,
                PROJECT_DAMPING_FACTOR,
                ACCOUNT_DAMPING_FACTOR,
                METADATA_FILE_PATH,
                DEPENDENCIES_FILE_PATH,
                null,
                RESULTS_FILE_PATH,
                ADD_MAINTAINERS,
                RANDOM_SEED,
                PROJECT_DEPENDENCY_WEIGHT,
                PROJECT_MAINTAINER_WEIGHT,
                null,
                ACCOUNT_MAINTAINER_WEIGHT,
                ACCOUNT_CONTRIBUTION_WEIGHT
        ).validate();
        assert(isValid == false);

        // Test with missing mandatory param: accountMaintainerWeight
        isValid = new OsrankParams(
                R,
                PROJECT_DAMPING_FACTOR,
                ACCOUNT_DAMPING_FACTOR,
                METADATA_FILE_PATH,
                DEPENDENCIES_FILE_PATH,
                null,
                RESULTS_FILE_PATH,
                ADD_MAINTAINERS,
                RANDOM_SEED,
                PROJECT_DEPENDENCY_WEIGHT,
                PROJECT_MAINTAINER_WEIGHT,
                PROJECT_CONTRIBUTION_WEIGHT,
                null,
                ACCOUNT_CONTRIBUTION_WEIGHT
        ).validate();
        assert(isValid == false);

        // Test with missing mandatory param: accountContributionWeight
        isValid = new OsrankParams(
                R,
                PROJECT_DAMPING_FACTOR,
                ACCOUNT_DAMPING_FACTOR,
                METADATA_FILE_PATH,
                DEPENDENCIES_FILE_PATH,
                null,
                RESULTS_FILE_PATH,
                ADD_MAINTAINERS,
                RANDOM_SEED,
                PROJECT_DEPENDENCY_WEIGHT,
                PROJECT_MAINTAINER_WEIGHT,
                PROJECT_CONTRIBUTION_WEIGHT,
                ACCOUNT_MAINTAINER_WEIGHT,
                null
        ).validate();
        assert(isValid == false);

    }



}
