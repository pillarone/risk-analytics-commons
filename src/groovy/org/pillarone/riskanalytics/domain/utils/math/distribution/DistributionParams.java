package org.pillarone.riskanalytics.domain.utils.math.distribution;

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
public enum DistributionParams {
    A("a"), ALPHA("alpha"), B("b"), BETA("beta"), CONSTANT("constant"), CONSTANTS("constants"), CUMULATIVE_PROBABILITES("cumulative probabilities"),
    DISCRETE_EMPIRICAL_CUMULATIVE_VALUES("discreteEmpiricalCumulativeValues"), DISCRETE_EMPIRICAL_VALUES("discreteEmpiricalValues"),
    DELTA("delta"), GAMMA("gamma"), LAMBDA("lambda"), MEAN("mean"), MU("mu"), SIGMA("sigma"), STDEV("stDev"), P("p"),
    OBSERVATIONS("observations"), PERIOD("period"), PROBABILITIES("probabilities"), M("m"), N("n"), CV("CV"),
    SUPPORT_POINTS("supportPoints"), XI("xi"), TAU("tau"), VALUES("values");

    String name;

    private DistributionParams(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
