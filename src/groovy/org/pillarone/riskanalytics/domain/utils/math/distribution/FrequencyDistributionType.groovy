package org.pillarone.riskanalytics.domain.utils.math.distribution

import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
class FrequencyDistributionType extends DistributionType {

    protected FrequencyDistributionType(String typeName, Map parameters) {
        super(typeName, parameters)
    }

    protected FrequencyDistributionType(String displayName, String typeName, Map parameters) {
        super(displayName, typeName, parameters)
    }

    static RandomDistribution getDefault() {
        return FrequencyDistributionType.getStrategy(FrequencyDistributionType.CONSTANT, ['constant': 0d])
    }

    public List<IParameterObjectClassifier> getClassifiers() {
        [
                CONSTANT,
                DISCRETEEMPIRICAL,
                DISCRETEEMPIRICALCUMULATIVE,
                NEGATIVEBINOMIAL,
                POISSON,
                BINOMIALDIST
        ]
    }
}
