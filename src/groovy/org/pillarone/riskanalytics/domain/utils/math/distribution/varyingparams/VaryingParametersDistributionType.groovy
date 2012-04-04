package org.pillarone.riskanalytics.domain.utils.math.distribution.varyingparams

import org.pillarone.riskanalytics.domain.utils.constraint.PeriodDistributionsConstraints
import org.pillarone.riskanalytics.domain.utils.constraint.PeriodNDistributionsConstraints
import org.pillarone.riskanalytics.core.parameterization.*
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionParams
import org.pillarone.riskanalytics.domain.utils.math.distribution.RandomDistribution

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
class VaryingParametersDistributionType extends AbstractParameterObjectClassifier {

    public static final VaryingParametersDistributionType POISSON = new VaryingParametersDistributionType(
            "poisson", "POISSON", ["lambda": new ConstrainedMultiDimensionalParameter([[1], [0d]], ['period', 'lambda'],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType EXPONENTIAL = new VaryingParametersDistributionType(
            "exponential", "EXPONENTIAL", ["lambda": new ConstrainedMultiDimensionalParameter([[1], [1d]], ['period', 'lambda'],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType NEGATIVEBINOMIAL = new VaryingParametersDistributionType(
            "negative binomial", "NEGATIVEBINOMIAL", ["gammaAndP": new ConstrainedMultiDimensionalParameter([[1], [1d], [1d]], ['period', 'gamma', 'p'],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    // period has to be specified for each observation
    public static final VaryingParametersDistributionType DISCRETEEMPIRICAL = new VaryingParametersDistributionType(
            "discrete empirical", "DISCRETEEMPIRICAL", ["discreteEmpiricalValues": new ConstrainedMultiDimensionalParameter(
                    [[1], [0.0], [1.0]], ['period', 'observations', 'probabilities'],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType DISCRETEEMPIRICALCUMULATIVE = new VaryingParametersDistributionType(
            "discrete empirical cumulative", "DISCRETEEMPIRICALCUMULATIVE",
            ["discreteEmpiricalCumulativeValues": new ConstrainedMultiDimensionalParameter(
                    [[0], [0.0], [1.0]], ['period', 'observations', 'cumulative probabilities'],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType NORMAL = new VaryingParametersDistributionType(
            "normal", "NORMAL", ["meanAndStDev": new ConstrainedMultiDimensionalParameter([[1], [0d], [1d]], ['period', 'mean', 'stDev'],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType LOGNORMAL = new VaryingParametersDistributionType(
            "log normal (mean, stdev)", "LOGNORMAL", ["meanAndStDev": new ConstrainedMultiDimensionalParameter([[1], [1d], [1d]],
                    ['period', 'mean', 'stDev'], ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType LOGNORMAL_MEAN_CV = new VaryingParametersDistributionType(
            "log normal (mean, CV)", "LOGNORMAL_MEAN_CV", [
                    "meanAndCV": new ConstrainedMultiDimensionalParameter([[1], [1d], [1d]], ['period', 'mean', 'CV'],
                            ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType LOGNORMAL_MU_SIGMA = new VaryingParametersDistributionType(
            "log normal (mu, sigma)", "LOGNORMAL_MU_SIGMA", ["muAndSigma": new ConstrainedMultiDimensionalParameter(
                    [[1], [1d], [1d]], ['period', 'mu', 'sigma'],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType BETA = new VaryingParametersDistributionType(
            "beta", "BETA", ["alphaAndBeta": new ConstrainedMultiDimensionalParameter([[1], [1d], [1d]], 
                    [DistributionParams.PERIOD, DistributionParams.ALPHA, DistributionParams.BETA],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType PARETO = new VaryingParametersDistributionType(
            "pareto", "PARETO", ["alphaAndBeta": new ConstrainedMultiDimensionalParameter(
                    [[1], [1d], [1d]], ['period', 'alpha', 'beta'],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType UNIFORM = new VaryingParametersDistributionType(
            "uniform", "UNIFORM", ["boundaries": new ConstrainedMultiDimensionalParameter([[1], [0d], [1d]], ['period', 'a', 'b'],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType CONSTANT = new VaryingParametersDistributionType(
            "constant", "CONSTANT", ["constant": new ConstrainedMultiDimensionalParameter([[1], [0d]],
                    [DistributionParams.PERIOD.toString(), DistributionParams.CONSTANT.toString()],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType PIECEWISELINEAREMPIRICAL = new VaryingParametersDistributionType(
            "piecewise linear empirical", "PIECEWISELINEAREMPIRICAL", [
                    "observations": new ConstrainedMultiDimensionalParameter([[1,1], [0d, 1d]], ['period', 'observations'],
                            ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType PIECEWISELINEAR = new VaryingParametersDistributionType(
            "piecewise linear", "PIECEWISELINEAR", ["supportPoints": new ConstrainedMultiDimensionalParameter(
                    [[1, 1], [0d, 1d], [0d, 1d]], ['period', 'values', 'cumulative probabilities'],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType TRIANGULARDIST = new VaryingParametersDistributionType(
            "triangular dist", "TRIANGULARDIST", ["supportPoints": new ConstrainedMultiDimensionalParameter(
                    [[1], [0d], [1d], [0.01]], ['period', 'a', 'b', 'm'],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType CHISQUAREDIST = new VaryingParametersDistributionType(
            "chi square dist", "CHISQUAREDIST", ["n": new ConstrainedMultiDimensionalParameter([[1], [1]], ['period', 'n'],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType STUDENTDIST = new VaryingParametersDistributionType(
            "student dist", "STUDENTDIST", ["n": new ConstrainedMultiDimensionalParameter([[1], [1]], ['period', 'n'],
                    ConstraintsFactory.getConstraints(PeriodNDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType BINOMIALDIST = new VaryingParametersDistributionType(
            "binomial dist", "BINOMIALDIST", ["nAndP": new ConstrainedMultiDimensionalParameter([[1], [1], [0d]], ['period', 'n', 'p'],
                    ConstraintsFactory.getConstraints(PeriodNDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType INVERSEGAUSSIANDIST = new VaryingParametersDistributionType(
            "inverse gaussian dist", "INVERSEGAUSSIANDIST",
            ["muAndLambda": new ConstrainedMultiDimensionalParameter([[1], [1d], [1d]], ['period', 'mu', 'lambda'],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])

    public static final VaryingParametersDistributionType CONSTANTS = new VaryingParametersDistributionType(
            "constant values", "CONSTANTS", ["constants": new ConstrainedMultiDimensionalParameter([[1, 1], [0d, 1d]],
                    ['period', 'constants'], ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])


    public static final VaryingParametersDistributionType GAMMA = new VaryingParametersDistributionType(
            "gamma", "GAMMA", ["alphaAndLambda": new ConstrainedMultiDimensionalParameter([[1], [2d], [2d]], ['period', 'alpha', 'lambda'],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType GUMBEL = new VaryingParametersDistributionType(
            "gumbel", "GUMBEL", ["betaAndDelta": new ConstrainedMultiDimensionalParameter([[1], [1d], [0d]], ['period', 'beta', 'delta'],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])
    public static final VaryingParametersDistributionType LOGLOGISTIC = new VaryingParametersDistributionType(
            "log logistic", "LOGLOGISTIC", ["alphaAndBeta": new ConstrainedMultiDimensionalParameter([[1], [2d], [1d]], ['period', 'alpha', 'beta'],
                    ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER))])

    public static final all = [
            BETA,
            CHISQUAREDIST,
            CONSTANT,
            CONSTANTS,
            BINOMIALDIST,
//            DISCRETEEMPIRICAL,
//            DISCRETEEMPIRICALCUMULATIVE,
            EXPONENTIAL,
            GAMMA,
            GUMBEL,
            INVERSEGAUSSIANDIST,
            LOGLOGISTIC,
            LOGNORMAL,
            LOGNORMAL_MEAN_CV,
            LOGNORMAL_MU_SIGMA,
            NEGATIVEBINOMIAL,
            NORMAL,
            PARETO,
            POISSON,
//            PIECEWISELINEAR,
//            PIECEWISELINEAREMPIRICAL,
            STUDENTDIST,
            TRIANGULARDIST,
            UNIFORM
    ]

    protected static Map types = [:]

    static {
        VaryingParametersDistributionType.all.each {
            VaryingParametersDistributionType.types[it.toString()] = it
        }
    }

    protected VaryingParametersDistributionType(String typeName, Map parameters) {
        this(typeName, typeName, parameters)
    }

    protected VaryingParametersDistributionType(String displayName, String typeName, Map parameters) {
        super(displayName, typeName, parameters)
    }

    public static VaryingParametersDistributionType valueOf(String type) {
        types[type]
    }

    public List<IParameterObjectClassifier> getClassifiers() {
        all
    }

    public IParameterObject getParameterObject(Map parameters) {
        return VaryingParametersDistributionType.getStrategy(this, parameters)
    }

    static IVaryingParametersDistributionStrategy getDefault() {
        return new ConstantVaryingParametersDistributionStrategy(constant:  new ConstrainedMultiDimensionalParameter([[1], [0d]],
                Arrays.asList(DistributionParams.PERIOD.toString(), DistributionParams.CONSTANT.toString()),
                ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER)));
    }

    static RandomDistribution getDistribution(VaryingParametersDistributionType type, Map parameters, int period) {
        getStrategy(type, parameters).getDistribution(period)
    }

    static IVaryingParametersDistributionStrategy getStrategy(VaryingParametersDistributionType type, Map parameters) {
        switch (type) {
            case VaryingParametersDistributionType.POISSON:
                return new PoissonVaryingParametersDistributionStrategy(lambda: (ConstrainedMultiDimensionalParameter) parameters['lambda'])
            case VaryingParametersDistributionType.NORMAL:
                return new NormalVaryingParametersDistributionStrategy(meanAndStDev: (ConstrainedMultiDimensionalParameter) parameters['meanAndStDev'])
            case VaryingParametersDistributionType.LOGNORMAL:
                return new LogNormalVaryingParametersDistributionStrategy(meanAndStDev: (ConstrainedMultiDimensionalParameter) parameters['meanAndStDev'])
            case VaryingParametersDistributionType.LOGNORMAL_MU_SIGMA:
                return new LogNormalMuSigmaVaryingParametersDistributionStrategy(muAndSigma: (ConstrainedMultiDimensionalParameter) parameters['muAndSigma'])
            case VaryingParametersDistributionType.LOGNORMAL_MEAN_CV:
                return new LogNormalMuCVVaryingParametersDistributionStrategy(meanAndCV: (ConstrainedMultiDimensionalParameter) parameters['meanAndCV'])
            case VaryingParametersDistributionType.EXPONENTIAL:
                return new ExponentialVaryingParametersDistributionStrategy(lambda: (ConstrainedMultiDimensionalParameter) parameters['lambda'])
            case VaryingParametersDistributionType.NEGATIVEBINOMIAL:
                return new NegativeBinomialVaryingParametersDistributionStrategy(gammaAndP: (ConstrainedMultiDimensionalParameter) parameters['gammaAndP'])
            case VaryingParametersDistributionType.BETA:
                return new BetaVaryingParametersDistributionStrategy(alphaAndBeta: (ConstrainedMultiDimensionalParameter) parameters['alphaAndBeta'])
            case VaryingParametersDistributionType.PARETO:
                return new ParetoVaryingParametersDistributionStrategy(alphaAndBeta: (ConstrainedMultiDimensionalParameter) parameters['alphaAndBeta'])
            case VaryingParametersDistributionType.UNIFORM:
                return new UniformVaryingParametersDistributionStrategy(boundaries: (ConstrainedMultiDimensionalParameter) parameters['boundaries'])
            case VaryingParametersDistributionType.CONSTANT:
                return new ConstantVaryingParametersDistributionStrategy(constant: (ConstrainedMultiDimensionalParameter) parameters['constant'])
            case VaryingParametersDistributionType.TRIANGULARDIST:
                return new TriangularVaryingParametersDistributionStrategy(supportPoints: (ConstrainedMultiDimensionalParameter) parameters['supportPoints'])
            case VaryingParametersDistributionType.CHISQUAREDIST:
                return new ChiSquareVaryingParametersDistributionStrategy(n: (ConstrainedMultiDimensionalParameter) parameters['n'])
            case VaryingParametersDistributionType.STUDENTDIST:
                return new StudentVaryingParametersDistributionStrategy(n: (ConstrainedMultiDimensionalParameter) parameters['n'])
            case VaryingParametersDistributionType.BINOMIALDIST:
                return new BinomialVaryingParametersDistributionStrategy(nAndP: (ConstrainedMultiDimensionalParameter) parameters['nAndP'])
            case VaryingParametersDistributionType.INVERSEGAUSSIANDIST:
                return new InverseGaussianVaryingParametersDistributionStrategy(muAndLambda: (ConstrainedMultiDimensionalParameter) parameters['muAndLambda'])
            case VaryingParametersDistributionType.GAMMA:
                return new GammaVaryingParametersDistributionStrategy(alphaAndLambda: (ConstrainedMultiDimensionalParameter) parameters['alphaAndLambda'])
            case VaryingParametersDistributionType.GUMBEL:
                return new GumbelVaryingParametersDistributionStrategy(betaAndDelta: (ConstrainedMultiDimensionalParameter) parameters['betaAndDelta'])
            case VaryingParametersDistributionType.LOGLOGISTIC:
                return new LogLogisticVaryingParametersDistributionStrategy(alphaAndBeta: (ConstrainedMultiDimensionalParameter) parameters['alphaAndBeta'])
            // todo(sku): implement
//            case VaryingParametersDistributionType.CONSTANTS:
//                distribution = new ConstantsVaryingParametersDistributionStrategy(constants: (TableMultiDimensionalParameter) parameters['constants'])
//                break
//            case VaryingParametersDistributionType.PIECEWISELINEAREMPIRICAL:
//                distribution = new PiecewiseLinearEmpiricalVaryingParametersDistributionStrategy(observations: (TableMultiDimensionalParameter) parameters['observations'])
//                break
//            case VaryingParametersDistributionType.PIECEWISELINEAR:
//                distribution = new PiecewiseLinearVaryingParametersDistributionStrategy(supportPoints: (TableMultiDimensionalParameter) parameters['supportPoints'])
//                break
//            case VaryingParametersDistributionType.DISCRETEEMPIRICAL:
//                distribution = new DiscreteEmpiricalVaryingParametersDistributionStrategy(discreteEmpiricalValues: (TableMultiDimensionalParameter) parameters['discreteEmpiricalValues'])
//                break
//            case VaryingParametersDistributionType.DISCRETEEMPIRICALCUMULATIVE:
//                distribution = new DiscreteEmpiricalCumulativeVaryingParametersDistributionStrategy(discreteEmpiricalCumulativeValues: (TableMultiDimensionalParameter) parameters['discreteEmpiricalCumulativeValues'])
//                break
        }
        throw new IllegalArgumentException("Distribution $type not implemented")
    }
}