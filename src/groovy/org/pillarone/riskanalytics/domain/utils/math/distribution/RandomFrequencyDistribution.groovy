package org.pillarone.riskanalytics.domain.utils.math.distribution

import org.pillarone.riskanalytics.core.parameterization.AbstractParameterObject
import umontreal.iro.lecuyer.probdist.Distribution
import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
class RandomFrequencyDistribution extends AbstractParameterObject implements IRandomDistribution {

    Distribution distribution
    FrequencyDistributionType type
    Map parameters

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
        }
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
        builder.append(distribution.class)

        def sortedParameters = parameters.entrySet().sort {Map.Entry it -> it.key}

        sortedParameters.each {Map.Entry entry ->
            builder.append(entry.value)
        }
        builder.toHashCode()
    }

    /**
     * regards objects as equal iff their formal types and all parameter values agree
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof RandomFrequencyDistribution) || !distribution.class.equals(((RandomFrequencyDistribution)obj).distribution.class)) {
            return false
        }
        for (Object parameter : parameters.keySet()) {
            if (!parameters[parameter].equals(obj.parameters[parameter])) {
                return false
            }
        }
        return true
    }

}

