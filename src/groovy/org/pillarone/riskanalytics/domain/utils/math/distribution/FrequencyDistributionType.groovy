package org.pillarone.riskanalytics.domain.utils.math.distribution

import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier
import org.pillarone.riskanalytics.core.parameterization.AbstractParameterObjectClassifier
import org.pillarone.riskanalytics.domain.utils.constraint.DoubleConstraints
import org.pillarone.riskanalytics.core.parameterization.ConstraintsFactory
import org.pillarone.riskanalytics.core.parameterization.ConstrainedMultiDimensionalParameter
import umontreal.iro.lecuyer.probdist.BinomialDist
import umontreal.iro.lecuyer.probdist.NegativeBinomialDist
import umontreal.iro.lecuyer.probdist.PoissonDist
import org.pillarone.riskanalytics.core.parameterization.IParameterObject
import umontreal.iro.lecuyer.probdist.Distribution
import umontreal.iro.lecuyer.probdist.DiscreteDistribution
import org.pillarone.riskanalytics.core.util.GroovyUtils
import org.pillarone.riskanalytics.core.simulation.InvalidParameterException

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
class FrequencyDistributionType extends AbstractParameterObjectClassifier implements Serializable {

    public static final FrequencyDistributionType POISSON = new FrequencyDistributionType(
            "poisson", "POISSON", ["lambda": 0d])
    public static final FrequencyDistributionType NEGATIVEBINOMIAL = new FrequencyDistributionType(
            "negative binomial", "NEGATIVEBINOMIAL", ["gamma": 1d, "p": 1d])
    public static final FrequencyDistributionType DISCRETEEMPIRICAL = new FrequencyDistributionType(
            "discrete empirical", "DISCRETEEMPIRICAL", ["discreteEmpiricalValues": new ConstrainedMultiDimensionalParameter([[0.0], [1.0]],
                    ['observations', 'probabilities'], ConstraintsFactory.getConstraints(DoubleConstraints.IDENTIFIER))])
    public static final FrequencyDistributionType DISCRETEEMPIRICALCUMULATIVE = new FrequencyDistributionType(
            "discrete empirical cumulative", "DISCRETEEMPIRICALCUMULATIVE", ["discreteEmpiricalCumulativeValues":
            new ConstrainedMultiDimensionalParameter([[0.0], [1.0]], ['observations', 'cumulative probabilities'],
                    ConstraintsFactory.getConstraints(DoubleConstraints.IDENTIFIER))])
    public static final FrequencyDistributionType CONSTANT = new FrequencyDistributionType(
            "constant", "CONSTANT", ["constant": 0d])
    public static final FrequencyDistributionType BINOMIALDIST = new FrequencyDistributionType(
            "binomial dist", "BINOMIALDIST", ["n": 1, "p": 0d])
    public static final FrequencyDistributionType CONSTANTS = new FrequencyDistributionType(
            "constant values", "CONSTANTS", ["constants": new ConstrainedMultiDimensionalParameter([0d, 1d],
                    ['constants'], ConstraintsFactory.getConstraints(DoubleConstraints.IDENTIFIER))])

    public static final all = [
            CONSTANT,
            CONSTANTS,
            BINOMIALDIST,
            DISCRETEEMPIRICAL,
            DISCRETEEMPIRICALCUMULATIVE,
            NEGATIVEBINOMIAL,
            POISSON,
    ]

    protected static Map types = [:]

    static {
        FrequencyDistributionType.all.each {
            FrequencyDistributionType.types[it.toString()] = it
        }
    }

    protected FrequencyDistributionType(String typeName, Map parameters) {
        this(typeName, typeName, parameters)
    }

    protected FrequencyDistributionType(String displayName, String typeName, Map parameters) {
        super(displayName, typeName, parameters)
    }

    public static FrequencyDistributionType valueOf(String type) {
        types[type]
    }

    public List<IParameterObjectClassifier> getClassifiers() {
        all
    }

    public IParameterObject getParameterObject(Map parameters) {
        return FrequencyDistributionType.getStrategy(this, parameters)
    }

    static RandomFrequencyDistribution getDefault() {
        return FrequencyDistributionType.getStrategy(FrequencyDistributionType.CONSTANT, ['constant': 0d])
    }

    public String getConstructionString(Map parameters) {
        TreeMap sortedParameters = new TreeMap()
        sortedParameters.putAll(parameters)
        "org.pillarone.riskanalytics.domain.utils.math.distribution.FrequencyDistributionType.getStrategy(${this.class.name}.${typeName.toUpperCase()}, $sortedParameters)"
    }

    private Object readResolve() throws java.io.ObjectStreamException {
        return types[displayName]
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

    /**
     * @return new RandomDistribution with distribution==null if parameters are invalid. Use ValidationService for better error messages.
     *
     * */
    static RandomFrequencyDistribution getStrategy(FrequencyDistributionType type, Map parameters) {
        RandomFrequencyDistribution distribution = new RandomFrequencyDistribution(type: type, parameters: parameters)
        try {
            switch (type) {
                case FrequencyDistributionType.POISSON:
                    distribution.distribution = new PoissonDist((double) (parameters.containsKey("lambda") ? parameters["lambda"] : 0))
                    break
                case FrequencyDistributionType.NEGATIVEBINOMIAL:
                    distribution.distribution = new NegativeBinomialDist((double) parameters["gamma"], (double) parameters["p"])
                    break
                case FrequencyDistributionType.CONSTANT:
                    distribution.distribution = new ConstantDistribution((double) parameters["constant"])
                    break
                case FrequencyDistributionType.DISCRETEEMPIRICAL:
                    distribution.distribution = getDiscreteEmpiricalDistribution(GroovyUtils.asDouble(parameters["discreteEmpiricalValues"].getColumnByName("observations")),
                            GroovyUtils.asDouble(parameters["discreteEmpiricalValues"].getColumnByName("probabilities")))
                    break
                case FrequencyDistributionType.DISCRETEEMPIRICALCUMULATIVE:
                    distribution.distribution = getDiscreteEmpiricalCumulativeDistribution(GroovyUtils.asDouble(parameters["discreteEmpiricalCumulativeValues"].getColumnByName("observations")),
                            GroovyUtils.asDouble(parameters["discreteEmpiricalCumulativeValues"].getColumnByName("cumulative probabilities")))
                    break
                case FrequencyDistributionType.BINOMIALDIST:
                    distribution.distribution = new BinomialDist((int) parameters["n"], (double) parameters["p"])
                    break
                case FrequencyDistributionType.CONSTANTS:
                    distribution.distribution = new ConstantsDistribution(GroovyUtils.asDouble(parameters["constants"].getColumnByName("constants")))
                    break
                default:
                    throw new InvalidParameterException("FrequencyDistributionType $type not implemented")
            }
        }
        catch (IllegalArgumentException ex) {
            throw new InvalidParameterException(ex.toString())
        }

        return distribution
    }
}
