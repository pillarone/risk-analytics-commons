package org.pillarone.riskanalytics.domain.utils.math.distribution.varyingparams;

import org.apache.commons.lang.ArrayUtils;
import org.pillarone.riskanalytics.core.parameterization.AbstractParameterObject;
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionParams;
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionType;
import org.pillarone.riskanalytics.domain.utils.math.distribution.RandomDistribution;

import java.util.TreeMap;

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
abstract public class AbstractVaryingParameterDistributionStrategy extends AbstractParameterObject implements IVaryingParametersDistributionStrategy {

    protected int periodColumnIndex = 0;

    public static final String PERIOD = "period";

    /** key: period */
    abstract TreeMap<Integer, RandomDistribution> initDistributions();

    /** key: period */
    protected TreeMap<Integer, RandomDistribution> distributions;

    public RandomDistribution getDistribution(int period) {
        if (distributions == null) {
            distributions = initDistributions();
            existingPeriod0(distributions);
        }
        return distributions.floorEntry(period).getValue();
    }

    /**
     * It's necessary to have a distribution for period 0. If none is defined, add constant = 0
     * @param distributionPerPeriod the map gets an additional entry, if none is defined for period 0
     */
    protected void existingPeriod0(TreeMap<Integer, RandomDistribution> distributionPerPeriod) {
        if (distributionPerPeriod.firstKey() > 0) {
            RandomDistribution distribution = DistributionType.getStrategy(DistributionType.CONSTANT,
                    ArrayUtils.toMap(new Object[][]{{DistributionParams.CONSTANT, 0d}}));
            distributionPerPeriod.put(0, distribution);
        }
    }
}
