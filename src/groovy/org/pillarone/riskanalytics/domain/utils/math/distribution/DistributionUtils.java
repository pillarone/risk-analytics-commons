package org.pillarone.riskanalytics.domain.utils.math.distribution;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public class DistributionUtils {

    public static RandomDistribution getIdiosyncraticPart(RandomDistribution overallDistribution,
                                                          RandomDistribution systematicDistribution) {
        if (systematicDistribution == null || systematicDistribution.getType() == null) {
            return overallDistribution;
        }
        if (!systematicDistribution.getType().equals(overallDistribution.getType())) {
            throw new IllegalArgumentException("systematic and overall distribution are not of same type!");
        }
        if (!systematicDistribution.getType().equals(DistributionType.POISSON)) {
            throw new IllegalArgumentException("systematic distribution not of type poisson!");
        }
        double lambda = (Double) overallDistribution.getParameters().get("lambda");
        double lambdaSystematic = (Double) systematicDistribution.getParameters().get("lambda");
        Map<String, Double> params = new HashMap<String, Double>();
        params.put("lambda", lambda - lambdaSystematic);
        return DistributionType.getStrategy(FrequencyDistributionType.POISSON, params);
    }
}
