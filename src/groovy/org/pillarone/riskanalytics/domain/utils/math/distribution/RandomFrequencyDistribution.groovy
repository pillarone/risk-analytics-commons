package org.pillarone.riskanalytics.domain.utils.math.distribution

import org.pillarone.riskanalytics.core.simulation.InvalidParameterException

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
class RandomFrequencyDistribution extends AbstractRandomDistribution {

    FrequencyDistributionType type

    FrequencyDistributionType getType() {
        type
    }

    DistributionType getDistributionType(){
        switch (type) {
            case FrequencyDistributionType.POISSON:
                return DistributionType.POISSON
            case FrequencyDistributionType.NEGATIVEBINOMIAL:
                return DistributionType.NEGATIVEBINOMIAL
            case FrequencyDistributionType.CONSTANT:
                return DistributionType.CONSTANT
            case FrequencyDistributionType.DISCRETEEMPIRICAL:
                return DistributionType.DISCRETEEMPIRICAL
            case FrequencyDistributionType.DISCRETEEMPIRICALCUMULATIVE:
                return DistributionType.DISCRETEEMPIRICALCUMULATIVE
            case FrequencyDistributionType.BINOMIALDIST:
                return DistributionType.BINOMIALDIST
            case FrequencyDistributionType.CONSTANTS:
                return DistributionType.CONSTANTS
            default:
                throw new InvalidParameterException("DistributionType $type not implemented")
        }
    }

}

