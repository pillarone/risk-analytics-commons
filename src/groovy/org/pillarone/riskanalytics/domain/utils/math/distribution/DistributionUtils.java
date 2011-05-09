package org.pillarone.riskanalytics.domain.utils.math.distribution;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public class DistributionUtils {

    public static RandomDistribution getIdiosyncraticDistribution(RandomDistribution overallDistribution,
                                                                  RandomDistribution systematicDistribution) {
        if (systematicDistribution == null || systematicDistribution.getDistribution() == null) {
            return overallDistribution;
        }
        if (!systematicDistribution.getType().equals(overallDistribution.getType())) {
            throw new IllegalArgumentException("systematic and overall distribution are not of same type!");
        }
        if (overallDistribution.getDistribution().getMean() < systematicDistribution.getDistribution().getMean()) {
            throw new IllegalArgumentException("mean of systematic frequencies is greater than mean of claims numbers!");
        }
        Map<String, Double> params = getIdiosyncraticParams(overallDistribution.getType(), overallDistribution.getParameters(),
                systematicDistribution.getParameters());
        return FrequencyDistributionType.getStrategy(FrequencyDistributionType.POISSON, params);
    }

    public static Map<String, Double> getIdiosyncraticParams(DistributionType type, Map<String, Double> paramsOverall, Map<String, Double> paramsSystematic) {

        Map<String, Double> params = new HashMap<String, Double>();
        double controllParameter = 0;
        if (type.equals(FrequencyDistributionType.POISSON)) {
            params.put("lambda", paramsOverall.get("lambda") - paramsSystematic.get("lambda"));
        }
        else if (type.equals(FrequencyDistributionType.BINOMIALDIST)) {
            params.put("p", paramsOverall.get("p"));
            params.put("n", paramsOverall.get("n") - paramsSystematic.get("n"));
            controllParameter = paramsOverall.get("p") - paramsSystematic.get("p");
        }
        else if (type.equals(FrequencyDistributionType.NEGATIVEBINOMIAL)) {
            params.put("p", paramsOverall.get("p"));
            params.put("gamma", paramsOverall.get("gamma")-paramsSystematic.get("gamma"));
            controllParameter = paramsOverall.get("p")-paramsSystematic.get("p");
        }
        if (controllParameter!=0) {
                throw new IllegalArgumentException("event generator and claims generators: parameters for frequency distribution are not consistent");
            }
        return params;
    }
}
