package org.pillarone.riskanalytics.domain.utils.math.distribution

import umontreal.iro.lecuyer.probdist.LognormalDist
import org.pillarone.riskanalytics.core.simulation.InvalidParameterException

/**
 * @author: stefan.kunz (at) intuitive-collaboration (dot) com
 */
// todo(sku): refactor identically to RNGF
class RandomVariateDistributionFactory {
    
    private static IRandomVariateDistribution getLognormalDistribution(double mean, double stDev) {
        double variance = Math.pow(stDev, 2)
        double meanSquare = Math.pow(mean, 2)
        double mu = Math.log(
                (meanSquare) /
                        Math.sqrt(variance + meanSquare)
        )
        double sigma = Math.sqrt(
                Math.log(
                        variance /
                                (meanSquare + 1)
                )
        )
        if (mu == Double.NaN || sigma == Double.NaN) {
            throw new InvalidParameterException("['RandomVariateDistributionFactory.NaNParameter','"
                        +mean+"','"+stDev+"']")
        }
        return new RandomVariateDistribution(distribution: new LognormalDist(mu, sigma))
    }

    static IRandomVariateDistribution getDistribution(RandomDistribution distribution) {
        return new RandomVariateDistribution(distribution: distribution.distribution)
    }

    static IRandomVariateDistribution getDistribution(DistributionType type, Map parameters) {
        return new RandomVariateDistribution(distribution: DistributionType.getStrategy(type, parameters))
    }
}