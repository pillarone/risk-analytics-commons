package org.pillarone.riskanalytics.domain.utils.math.distribution

import org.pillarone.riskanalytics.domain.utils.constraint.DoubleConstraints
import org.pillarone.riskanalytics.core.parameterization.*
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

    public static Distribution getDiscreteEmpiricalDistribution(double[] obs, double[] prob) {
        double probSum = 0
        for (double value: prob) {probSum += value}
        for (int i = 0; i < prob.size(); i++) {
            prob[i] = prob[i] / probSum
        }
        return new DiscreteDistribution(obs, prob, obs.length)
    }

    public static Distribution getDiscreteEmpiricalCumulativeDistribution(double[] obs, double[] cumprob) {
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
     */
    static RandomDistribution getStrategy(DistributionType type, Map parameters) {
        RandomDistribution distribution = new RandomDistribution(type: type, parameters: parameters)
        //TODO msp move initialization to RD.getDistribution()
        try {
            switch (type) {
                case DistributionType.NORMAL:
                    distribution.distribution = new NormalDist(
                            extractParam(parameters, DistributionParams.MEAN, 0d),
                            extractParam(parameters, DistributionParams.STDEV, 1d))
                    break
                case DistributionType.LOGNORMAL:
                    distribution.distribution = getLognormalDistribution(
                            extractParam(parameters, DistributionParams.MEAN, 1d),
                            extractParam(parameters, DistributionParams.STDEV, 1d))
                    break
                case DistributionType.LOGNORMAL_MEAN_CV:
                    distribution.distribution = getLognormalCVDistribution(
                            extractParam(parameters, DistributionParams.MEAN, 1d),
                            extractParam(parameters, DistributionParams.CV, 1d))
                    break
                case DistributionType.LOGNORMAL_MU_SIGMA:
                    distribution.distribution = new LognormalDist(
                            extractParam(parameters, DistributionParams.MU, 1d),
                            extractParam(parameters, DistributionParams.SIGMA, 1d))
                    break
                case DistributionType.POISSON:
                    distribution.distribution = new PoissonDist(extractParam(parameters, DistributionParams.LAMBDA, 0d))
                    break
                case DistributionType.EXPONENTIAL:
                    distribution.distribution = new ExponentialDist(extractParam(parameters, DistributionParams.LAMBDA, 1d))
                    break
                case DistributionType.NEGATIVEBINOMIAL:
                    distribution.distribution = new NegativeBinomialDist(
                            extractParam(parameters, DistributionParams.GAMMA, 1d),
                            extractParam(parameters, DistributionParams.P, 1d))
                    break
                case DistributionType.PARETO:
                    distribution.distribution = new ParetoDist(
                            extractParam(parameters, DistributionParams.ALPHA, 1d),
                            extractParam(parameters, DistributionParams.BETA, 1d))
                    break
                case DistributionType.BETA:
                    distribution.distribution = new BetaDist(
                            extractParam(parameters, DistributionParams.ALPHA, 1d),
                            extractParam(parameters, DistributionParams.BETA, 1d))
                    break
                case DistributionType.UNIFORM:
                    distribution.distribution = new UniformDist(
                            extractParam(parameters, DistributionParams.A, 0d),
                            extractParam(parameters, DistributionParams.B, 1d))
                    break
                case DistributionType.CONSTANT:
                    distribution.distribution = new ConstantDistribution(extractParam(parameters, DistributionParams.CONSTANT, 0d))
                    break
                case DistributionType.DISCRETEEMPIRICAL:
                    distribution.distribution = getDiscreteEmpiricalDistribution(
                            extractParam(parameters, DistributionParams.DISCRETE_EMPIRICAL_VALUES, DistributionParams.OBSERVATIONS, 0d),
                            extractParam(parameters, DistributionParams.DISCRETE_EMPIRICAL_VALUES, DistributionParams.PROBABILITIES, 0d))
                    break
                case DistributionType.DISCRETEEMPIRICALCUMULATIVE:
                    distribution.distribution = getDiscreteEmpiricalCumulativeDistribution(
                            extractParam(parameters, DistributionParams.DISCRETE_EMPIRICAL_CUMULATIVE_VALUES, DistributionParams.OBSERVATIONS, 0d),
                            extractParam(parameters, DistributionParams.DISCRETE_EMPIRICAL_CUMULATIVE_VALUES, DistributionParams.CUMULATIVE_PROBABILITES, 0d))
                    break
                case DistributionType.PIECEWISELINEAREMPIRICAL:
                    distribution.distribution = new PiecewiseLinearEmpiricalDist(
                            extractParam(parameters, DistributionParams.OBSERVATIONS, DistributionParams.OBSERVATIONS, 0d))
                    break
                case DistributionType.PIECEWISELINEAR:
                    distribution.distribution = new PiecewiseLinearDistribution(
                            extractParam(parameters, DistributionParams.SUPPORT_POINTS, DistributionParams.VALUES, 0d),
                            extractParam(parameters, DistributionParams.SUPPORT_POINTS, DistributionParams.CUMULATIVE_PROBABILITES, 0d),)
                    break
                case DistributionType.TRIANGULARDIST:
                    distribution.distribution = new TriangularDist(
                            extractParam(parameters, DistributionParams.A, 0d),
                            extractParam(parameters, DistributionParams.B, 1d),
                            extractParam(parameters, DistributionParams.M, 0.01d))
                    break
                case DistributionType.CHISQUAREDIST:
                    distribution.distribution = new ChiSquareDist(extractParam(parameters, DistributionParams.N, 1))
                    break
                case DistributionType.STUDENTDIST:
                    distribution.distribution = new StudentDist(extractParam(parameters, DistributionParams.N, 1))
                    break
                case DistributionType.BINOMIALDIST:
                    distribution.distribution = new BinomialDist(
                            extractParam(parameters, DistributionParams.N, 1),
                            extractParam(parameters, DistributionParams.P, 0d))
                    break
                case DistributionType.INVERSEGAUSSIANDIST:
                    distribution.distribution = getLognormalCVDistribution(
                            extractParam(parameters, DistributionParams.MU, 1d),
                            extractParam(parameters, DistributionParams.LAMBDA, 1d))
                    break
                case DistributionType.CONSTANTS:
                    distribution.distribution = new ConstantsDistribution(extractParam(parameters, DistributionParams.CONSTANTS, DistributionParams.CONSTANTS, 0d))
                    break
                case DistributionType.GAMMA:
                    distribution.distribution = new GammaDist(
                            extractParam(parameters, DistributionParams.ALPHA, 2d),
                            extractParam(parameters, DistributionParams.LAMBDA, 2d))
                    break
                case DistributionType.GUMBEL:
                    distribution.distribution = new GumbelDist(
                            extractParam(parameters, DistributionParams.BETA, 1d),
                            extractParam(parameters, DistributionParams.DELTA, 0d))
                    break
                case DistributionType.LOGLOGISTIC:
                    distribution.distribution = new LoglogisticDist(
                            extractParam(parameters, DistributionParams.ALPHA, 2d),
                            extractParam(parameters, DistributionParams.BETA, 1d))
                    break
                case DistributionType.GPD:
                    distribution.distribution = new GeneralizedParetoDistribution(
                            extractParam(parameters, DistributionParams.XI, 1/2d),
                            extractParam(parameters, DistributionParams.BETA, 1d),
                            extractParam(parameters, DistributionParams.TAU, 1d))
                    break
                case DistributionType.SHIFTEDPARETOII:
                    distribution.distribution = new TypeIIParetoDistribution(
                            extractParam(parameters, DistributionParams.ALPHA, 2d),
                            extractParam(parameters, DistributionParams.BETA, 1d),
                            extractParam(parameters, DistributionParams.LAMBDA, 0d))
                    break
                case DistributionType.PARETOII:
                    distribution.distribution = new TypeIIParetoDistribution(
                            extractParam(parameters, DistributionParams.ALPHA, 2d),
                            extractParam(parameters, DistributionParams.LAMBDA, 0d))
                    break
                case DistributionType.LOGNORMALPARETO:
                    distribution.distribution = new LognormalParetoDistribution(
                            extractParam(parameters, DistributionParams.SIGMA, 1d),
                            extractParam(parameters, DistributionParams.ALPHA, 2d),
                            extractParam(parameters, DistributionParams.BETA, 1d),
                            extractParam(parameters, DistributionParams.MU, -2d))
                    break
                case DistributionType.LOGNORMALTYPEIIPARETO:
                    distribution.distribution = new LognormalTypeIIParetoDistribution(
                            extractParam(parameters, DistributionParams.SIGMA, 1d),
                            extractParam(parameters, DistributionParams.ALPHA, 2d),
                            extractParam(parameters, DistributionParams.BETA, 1d),
                            extractParam(parameters, DistributionParams.LAMBDA, 0d),
                            extractParam(parameters, DistributionParams.MU, -2d))
                    break
                case DistributionType.LOGNORMALPARETO_SMOOTH:
                    distribution.distribution = new LognormalParetoDistribution(
                            extractParam(parameters, DistributionParams.SIGMA, 1d),
                            extractParam(parameters, DistributionParams.ALPHA, 2d),
                            extractParam(parameters, DistributionParams.BETA, 1d))
                    break
                case DistributionType.LOGNORMALTYPEIIPARETO_SMOOTH:
                    distribution.distribution = new LognormalParetoDistribution(
                            extractParam(parameters, DistributionParams.SIGMA, 1d),
                            extractParam(parameters, DistributionParams.ALPHA, 2d),
                            extractParam(parameters, DistributionParams.BETA, 1d),
                            extractParam(parameters, DistributionParams.LAMBDA, 0d))
                    break
            }
        }
        catch (IllegalArgumentException ex) {
            // see PMO-1619
        }

        return distribution
    }

    static double extractParam(Map params, DistributionParams name, double defaultValue) {
        Double param = params.containsKey(name) ? params[name] : params[(name.toString())]
        (param == null ? defaultValue : param)
    }

    static int extractParam(Map params, DistributionParams name, int defaultValue) {
        Integer param = params.containsKey(name) ? params[name] : params[(name.toString())]
        (param == null ? defaultValue : param)
    }

    static double[] extractParam(Map params, DistributionParams tableName, DistributionParams columnName, double defaultValues) {
        String key = params.containsKey(tableName) ? tableName : tableName.toString()
        double[] param = GroovyUtils.asDouble((params[key]).getColumnByName(columnName.toString()))
        (param == null ? {defaultValues} : param)
    }
}
