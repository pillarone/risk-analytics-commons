package org.pillarone.riskanalytics.domain.utils.math.distribution;

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
public enum DistributionParams {
    A("a"),
    ALPHA("alpha"),
    B("b"),
    BETA("beta"),
    CONSTANT("constant"),
    CONSTANTS("constants"),
    CUMULATIVE_PROBABILITES("cumulative probabilities"),
    CV("CV"),
    DISCRETE_EMPIRICAL_CUMULATIVE_VALUES("discreteEmpiricalCumulativeValues"),
    DISCRETE_EMPIRICAL_VALUES("discreteEmpiricalValues"),
    DELTA("delta"),
    GAMMA("gamma"),
    LAMBDA("lambda"),
    M("m"),
    MEAN("mean"),
    MU("mu"),
    N("n"),
    OBSERVATIONS("observations"),
    P("p"),
    PERIOD("period"),
    PROBABILITIES("probabilities"),
    SIGMA("sigma"),
    STDEV("stDev"),
    SUPPORT_POINTS("supportPoints"),
    TAU("tau"),
    VALUES("values"),
    XI("xi")
    ;

    String name;

    private DistributionParams(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
