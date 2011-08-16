package org.pillarone.riskanalytics.domain.utils.math.distribution

import org.pillarone.riskanalytics.domain.utils.constraint.DoubleConstraints
import org.pillarone.riskanalytics.core.parameterization.*
import static org.pillarone.riskanalytics.core.util.GroovyUtils.asDouble
import umontreal.iro.lecuyer.probdist.*
import org.pillarone.riskanalytics.core.util.GroovyUtils

class DistributionType extends AbstractParameterObjectClassifier implements Serializable {

    public static final DistributionType POISSON = new DistributionType(
            "poisson", "POISSON", ["lambda": 0d])
    public static final DistributionType EXPONENTIAL = new DistributionType(
            "exponential", "EXPONENTIAL", ["lambda": 1d])
    public static final DistributionType NEGATIVEBINOMIAL = new DistributionType(
            "negative binomial", "NEGATIVEBINOMIAL", ["gamma": 1d, "p": 1d])
    public static final DistributionType DISCRETEEMPIRICAL = new DistributionType(
//            "discrete empirical", "DISCRETEEMPIRICAL", ["discreteEmpiricalValues": new TableMultiDimensionalParameter([[0.0], [1.0]], ['observations', 'probabilities'])])
            "discrete empirical", "DISCRETEEMPIRICAL", ["discreteEmpiricalValues": new ConstrainedMultiDimensionalParameter([[0.0], [1.0]],
                    ['observations', 'probabilities'], ConstraintsFactory.getConstraints(DoubleConstraints.IDENTIFIER))])
    public static final DistributionType DISCRETEEMPIRICALCUMULATIVE = new DistributionType(
//            "discrete empirical cumulative", "DISCRETEEMPIRICALCUMULATIVE", ["discreteEmpiricalCumulativeValues": new TableMultiDimensionalParameter([[0.0], [1.0]], ['observations', 'cumulative probabilities'])])
            "discrete empirical cumulative", "DISCRETEEMPIRICALCUMULATIVE", ["discreteEmpiricalCumulativeValues":
            new ConstrainedMultiDimensionalParameter([[0.0], [1.0]], ['observations', 'cumulative probabilities'],
                    ConstraintsFactory.getConstraints(DoubleConstraints.IDENTIFIER))])
    public static final DistributionType NORMAL = new DistributionType(
            "normal", "NORMAL", ["mean": 0d, "stDev": 1d])
    public static final DistributionType LOGNORMAL = new DistributionType(
            "log normal (mean, stdev)", "LOGNORMAL", ["mean": 1d, "stDev": 1d])
    public static final DistributionType LOGNORMAL_MU_SIGMA = new DistributionType(
            "log normal (mu, sigma)", "LOGNORMAL_MU_SIGMA", ["mu": 1d, "sigma": 1d])
    public static final DistributionType LOGNORMAL_MEAN_CV = new DistributionType(
            "log normal (mean, cv)", "LOGNORMAL_MEAN_CV", ["mean": 1d, "CV": 1d])
    public static final DistributionType BETA = new DistributionType(
            "beta", "BETA", ["alpha": 1d, "beta": 1d])
    public static final DistributionType PARETO = new DistributionType(
            "pareto", "PARETO", ["alpha": 1d, "beta": 1d])
    public static final DistributionType UNIFORM = new DistributionType(
            "uniform", "UNIFORM", ["a": 0d, "b": 1d])
    public static final DistributionType CONSTANT = new DistributionType(
            "constant", "CONSTANT", ["constant": 0d])
    public static final DistributionType PIECEWISELINEAREMPIRICAL = new DistributionType(
//            "piecewise linear empirical", "PIECEWISELINEAREMPIRICAL", ["observations": new TableMultiDimensionalParameter([0d, 1d], ['observations'])])
            "piecewise linear empirical", "PIECEWISELINEAREMPIRICAL", ["observations": new ConstrainedMultiDimensionalParameter([0d, 1d],
                    ['observations'], ConstraintsFactory.getConstraints(DoubleConstraints.IDENTIFIER))])
    public static final DistributionType PIECEWISELINEAR = new DistributionType(
//            "piecewise linear", "PIECEWISELINEAR", ["supportPoints": new TableMultiDimensionalParameter([[0d, 1d], [0d, 1d]], ['values', 'cumulative probabilities'])])
            "piecewise linear", "PIECEWISELINEAR", ["supportPoints": new ConstrainedMultiDimensionalParameter([[0d, 1d], [0d, 1d]],
                    ['values', 'cumulative probabilities'], ConstraintsFactory.getConstraints(DoubleConstraints.IDENTIFIER))])
    public static final DistributionType TRIANGULARDIST = new DistributionType(
            "triangular dist", "TRIANGULARDIST", ["a": 0d, "b": 1d, "m": 0.01])
    public static final DistributionType CHISQUAREDIST = new DistributionType(
            "chi square dist", "CHISQUAREDIST", ["n": 1])
    public static final DistributionType STUDENTDIST = new DistributionType(
            "student dist", "STUDENTDIST", ["n": 1])
    public static final DistributionType BINOMIALDIST = new DistributionType(
            "binomial dist", "BINOMIALDIST", ["n": 1, "p": 0d])
    public static final DistributionType INVERSEGAUSSIANDIST = new DistributionType(
            "inverse gaussian dist", "INVERSEGAUSSIANDIST", ["mu": 1d, "lambda": 1d])
    public static final DistributionType CONSTANTS = new DistributionType(
//            "constant values", "CONSTANTS", ["constants": new TableMultiDimensionalParameter([0d, 1d], ['constants'])])
            "constant values", "CONSTANTS", ["constants": new ConstrainedMultiDimensionalParameter([0d, 1d],
                    ['constants'], ConstraintsFactory.getConstraints(DoubleConstraints.IDENTIFIER))])
    public static final DistributionType GAMMA = new DistributionType(
            "gamma", "GAMMA", ["alpha": 2d, "lambda": 2d])
    public static final DistributionType GUMBEL = new DistributionType(
            "gumbel", "GUMBEL", ["beta": 1d, "delta": 0d])
    public static final DistributionType LOGLOGISTIC = new DistributionType(
            "log logistic", "LOGLOGISTIC", ["alpha": 2d, "beta": 1d])
    public static final DistributionType GPD = new DistributionType(
            "generalized pareto", "GPD", ["xi": 1 / 2d, "beta": 1d, "tau": 1d])
    public static final DistributionType SHIFTEDPARETOII = new DistributionType(
            "shifted pareto II", "SHIFTEDPARETOII", ["alpha": 2d, "beta": 1d, "lambda": 0d])
    public static final DistributionType PARETOII = new DistributionType(
            "pareto II", "PARETOII", ["alpha": 2d, "lambda": 0d])
    public static final DistributionType LOGNORMALPARETO = new DistributionType(
            "lognormal pareto", "LOGNORMALPARETO", ["sigma": 1d, "alpha": 2d, "beta": 1d, "mu": -2d])
    public static final DistributionType LOGNORMALTYPEIIPARETO = new DistributionType(
            "lognormal pareto II", "LOGNORMALTYPEIIPARETO", ["sigma": 1d, "alpha": 2d, "beta": 1d, "lambda": 0d, "mu": -2d])
    public static final DistributionType LOGNORMALPARETO_SMOOTH = new DistributionType(
            "lognormal pareto smooth", "LOGNORMALPARETO_SMOOTH", ["sigma": 1d, "alpha": 2d, "beta": 1d])
    public static final DistributionType LOGNORMALTYPEIIPARETO_SMOOTH = new DistributionType(
            "lognormal pareto II smooth", "LOGNORMALTYPEIIPARETO_SMOOTH", ["sigma": 1d, "alpha": 2d, "beta": 1d, "lambda": 0d])

    public static final all = [
            BETA,
            CHISQUAREDIST,
            CONSTANT,
            CONSTANTS,
            BINOMIALDIST,
            DISCRETEEMPIRICAL,
            DISCRETEEMPIRICALCUMULATIVE,
            EXPONENTIAL,
            GAMMA,
            GPD,
            GUMBEL,
            INVERSEGAUSSIANDIST,
            LOGLOGISTIC,
            LOGNORMAL,
            LOGNORMAL_MEAN_CV,
            LOGNORMAL_MU_SIGMA,
            LOGNORMALPARETO,
            LOGNORMALPARETO_SMOOTH,
            LOGNORMALTYPEIIPARETO,
            LOGNORMALTYPEIIPARETO_SMOOTH,
            NEGATIVEBINOMIAL,
            NORMAL,
            PARETO,
            PARETOII,
            POISSON,
            PIECEWISELINEAR,
            PIECEWISELINEAREMPIRICAL,
            SHIFTEDPARETOII,
            STUDENTDIST,
            TRIANGULARDIST,
            UNIFORM
    ]

    protected static Map types = [:]

    static {
        DistributionType.all.each {
            DistributionType.types[it.toString()] = it
        }
    }

    protected DistributionType(String typeName, Map parameters) {
        this(typeName, typeName, parameters)
    }

    protected DistributionType(String displayName, String typeName, Map parameters) {
        super(displayName, typeName, parameters)
    }

    public static DistributionType valueOf(String type) {
        types[type]
    }

    public List<IParameterObjectClassifier> getClassifiers() {
        all
    }

    public IParameterObject getParameterObject(Map parameters) {
        return DistributionType.getStrategy(this, parameters)
    }

    static RandomDistribution getDefault() {
        return DistributionType.getStrategy(DistributionType.CONSTANT, ['constant': 0d])
    }

    public String getConstructionString(Map parameters) {
        TreeMap sortedParameters = new TreeMap()
        sortedParameters.putAll(parameters)
        "org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionType.getStrategy(${this.class.name}.${typeName.toUpperCase()}, $sortedParameters)"
    }

    private Object readResolve() throws java.io.ObjectStreamException {
        return types[displayName]
    }

    private static Distribution getLognormalDistribution(double mean, double stDev) {
        double variance = stDev * stDev
        double meanSquare = mean * mean
        double t = Math.log(1 + (variance / meanSquare))
        double sigma = Math.sqrt(t)
        double mu = Math.log(mean) - 0.5 * t
        if (mu == Double.NaN || sigma == Double.NaN) {
            throw new IllegalArgumentException("['DistributionType.NaNParameter','"
                    + mean + "','" + stDev + "']")
        }
        return new LognormalDist(mu, sigma)
    }

    private static Distribution getLognormalCVDistribution(double mean, double cv) {
        double stdev = mean * cv
        double variance = stdev * stdev
        double meanSquare = mean * mean
        double t = Math.log(1 + (variance / meanSquare))
        double sigma = Math.sqrt(t)
        double mu = Math.log(mean) - 0.5 * t
        if (mu == Double.NaN || mu == 0) {
            throw new IllegalArgumentException("['DistributionType.NaNParameter','" + mu + "']")
        }
        return new LognormalDist(mu, sigma)
    }

    private static Distribution getDiscreteEmpiricalDistribution(double[] obs, double[] prob) {
        double probSum = 0
        for (double value: prob) {probSum += value}
        for (int i = 0; i < prob.size(); i++) {
            prob[i] = prob[i] / probSum
        }
        return new DiscreteDistribution(obs, prob, obs.length)
    }

    private static Distribution getDiscreteEmpiricalCumulativeDistribution(double[] obs, double[] cumprob) {
        double lastcell = 0, ret = 0
        def prob = cumprob.collect {cell -> ret = cell - lastcell; lastcell = cell; ret }
        return getDiscreteEmpiricalDistribution(obs, prob as double[])
    }

    static RandomDistribution getUniformDistribution() {
        return getStrategy(DistributionType.UNIFORM, ['a': 0, 'b': 1])
    }

    /**
     * @return new RandomDistribution with distribution==null if parameters are invalid. Use ValidationService for better error messages.
     *
     * */
    static RandomDistribution getStrategy(DistributionType type, Map parameters) {
        RandomDistribution distribution = new RandomDistribution(type: type, parameters: parameters)
        //TODO msp move initialization to RD.getDistribution()
        switch (type) {
            case DistributionType.NORMAL:
                try {
                    distribution.distribution = new NormalDist(
                            (double) (parameters.containsKey("mean") ? parameters["mean"] : 0),
                            (double) (parameters.containsKey("stDev") ? parameters["stDev"] : 1))
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }

                break
            case DistributionType.LOGNORMAL:
                try {
                    distribution.distribution = getLognormalDistribution((double) parameters["mean"], (double) parameters["stDev"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.LOGNORMAL_MEAN_CV:
                try {
                    distribution.distribution = getLognormalCVDistribution((double) parameters["mean"], (double) parameters["CV"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.LOGNORMAL_MU_SIGMA:
                try {
                    distribution.distribution = new LognormalDist((double) parameters["mu"], (double) parameters["sigma"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.POISSON:
                try {
                    distribution.distribution = new PoissonDist((double) (parameters.containsKey("lambda") ? parameters["lambda"] : 0))
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.EXPONENTIAL:
                try {
                    distribution.distribution = new ExponentialDist((double) (parameters.containsKey("lambda") ? parameters["lambda"] : 1))
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.NEGATIVEBINOMIAL:
                try {
                    distribution.distribution = new NegativeBinomialDist((double) parameters["gamma"], (double) parameters["p"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.PARETO:
                try {
                    distribution.distribution = new ParetoDist((double) parameters["alpha"], (double) parameters["beta"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.BETA:
                try {
                    distribution.distribution = new BetaDist((double) parameters["alpha"], (double) parameters["beta"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.UNIFORM:
                try {
                    distribution.distribution = new UniformDist((double) parameters["a"], (double) parameters["b"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.CONSTANT:
                try {
                    distribution.distribution = new ConstantDistribution((double) parameters["constant"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.DISCRETEEMPIRICAL:
                try {
                    distribution.distribution = getDiscreteEmpiricalDistribution(GroovyUtils.asDouble(parameters["discreteEmpiricalValues"].getColumnByName("observations")),
                            GroovyUtils.asDouble(parameters["discreteEmpiricalValues"].getColumnByName("probabilities")))
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.DISCRETEEMPIRICALCUMULATIVE:
                try {
                    distribution.distribution = getDiscreteEmpiricalCumulativeDistribution(GroovyUtils.asDouble(parameters["discreteEmpiricalCumulativeValues"].getColumnByName("observations")),
                            GroovyUtils.asDouble(parameters["discreteEmpiricalCumulativeValues"].getColumnByName("cumulative probabilities")))
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.PIECEWISELINEAREMPIRICAL:
                try {
                    distribution.distribution = new PiecewiseLinearEmpiricalDist((double[]) GroovyUtils.asDouble(parameters["observations"].getColumnByName("observations")))
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.PIECEWISELINEAR:
                try {
                    distribution.distribution = new PiecewiseLinearDistribution(GroovyUtils.asDouble(parameters["supportPoints"].getColumnByName("values")),
                            GroovyUtils.asDouble(parameters["supportPoints"].getColumnByName("cumulative probabilities")))
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.TRIANGULARDIST:
                try {
                    distribution.distribution = new TriangularDist((double) parameters["a"], (double) parameters["b"], (double) parameters["m"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.CHISQUAREDIST:
                try {
                    distribution.distribution = new ChiSquareDist((int) parameters["n"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.STUDENTDIST:
                try {
                    distribution.distribution = new StudentDist((int) parameters["n"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.BINOMIALDIST:
                try {
                    distribution.distribution = new BinomialDist((int) parameters["n"], (double) parameters["p"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.INVERSEGAUSSIANDIST:
                try {
                    distribution.distribution = new InverseGaussianDist((double) parameters["mu"], (double) parameters["lambda"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.CONSTANTS:
                try {
                    distribution.distribution = new ConstantsDistribution(GroovyUtils.asDouble(parameters["constants"].getColumnByName("constants")))
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.GAMMA:
                try {
                    distribution.distribution = new GammaDist((double) parameters["alpha"], (double) parameters["lambda"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.GUMBEL:
                try {
                    distribution.distribution = new GumbelDist((double) parameters["beta"], (double) parameters["delta"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.LOGLOGISTIC:
                try {
                    distribution.distribution = new LoglogisticDist((double) parameters["alpha"], (double) parameters["beta"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.GPD:
                try {
                    distribution.distribution = new GeneralizedParetoDistribution((double) parameters["xi"],
                            (double) parameters["beta"], (double) parameters["tau"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.SHIFTEDPARETOII:
                try {
                    distribution.distribution = new TypeIIParetoDistribution((double) parameters["alpha"],
                            (double) parameters["beta"], (double) parameters["lambda"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.PARETOII:
                try {
                    distribution.distribution = new TypeIIParetoDistribution((double) parameters["alpha"],
                            (double) parameters["lambda"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.LOGNORMALPARETO:
                try {
                    distribution.distribution = new LognormalParetoDistribution((double) parameters["sigma"],
                            (double) parameters["alpha"], (double) parameters["beta"], (double) parameters["mu"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.LOGNORMALTYPEIIPARETO:
                try {
                    distribution.distribution = new LognormalTypeIIParetoDistribution((double) parameters["sigma"],
                            (double) parameters["alpha"], (double) parameters["beta"], (double) parameters["lambda"], (double) parameters["mu"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.LOGNORMALPARETO_SMOOTH:
                try {
                    distribution.distribution = new LognormalParetoDistribution((double) parameters["sigma"],
                            (double) parameters["alpha"], (double) parameters["beta"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
            case DistributionType.LOGNORMALTYPEIIPARETO_SMOOTH:
                try {
                    distribution.distribution = new LognormalTypeIIParetoDistribution((double) parameters["sigma"],
                            (double) parameters["alpha"], (double) parameters["beta"], (double) parameters["lambda"])
                }
                catch (IllegalArgumentException ex) {
                    // see PMO-1619
                }
                break
        }

        return distribution
    }
}
