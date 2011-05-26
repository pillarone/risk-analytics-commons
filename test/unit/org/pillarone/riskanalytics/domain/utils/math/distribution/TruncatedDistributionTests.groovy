package org.pillarone.riskanalytics.domain.utils.math.distribution

import umontreal.iro.lecuyer.probdist.DiscreteDistribution;


import umontreal.iro.lecuyer.probdist.PoissonDist
import umontreal.iro.lecuyer.probdist.ContinuousDistribution
import umontreal.iro.lecuyer.probdist.NormalDist
import umontreal.iro.lecuyer.probdist.Distribution
import umontreal.iro.lecuyer.probdist.TruncatedDist;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
class TruncatedDistributionTests extends GroovyTestCase {

    double[] obs = [0, 2, 4, 6, 8, 10, 12, 14]
    double[] probs = [1 / 6d, 1 / 6d, 1 / 6d, 1 / 6d, 1 / 12d, 1 / 12d, 1 / 12d, 1 / 12d]
    DiscreteDistribution discreteEmpiricalDist = new DiscreteDistribution(obs, (double[]) probs, (int) 8);

    PoissonDist poissonDist = new PoissonDist(5);
    ContinuousDistribution normalDist = new NormalDist(0, 1)
    TruncatedDistribution truncatedDiscreteDist1
    TruncatedDistribution truncatedDiscreteDist2
    TruncatedDistribution truncatedDiscreteDistInt

    void testDiscreteDistribution() {

        truncatedDiscreteDist1 = new TruncatedDistribution(discreteEmpiricalDist, 1, 4)
        //assertEquals "cdf at 1", 1 / 4, truncatedDiscreteDist.cdf(1)
        assertEquals " prob of untruncated", 1 / 6d, discreteEmpiricalDist.prob(3)
        assertEquals " prob of untruncated", 1 / 12d, discreteEmpiricalDist.prob(4)
        assertEquals "cum prob of untruncated", 3 / 6d, discreteEmpiricalDist.cdf(4)

        assertEquals "cdf at 0", 0d, truncatedDiscreteDist1.cdf(0)
        assertEquals "cdf at 1", 0d, truncatedDiscreteDist1.cdf(1)
        assertEquals "cdf at 2", 1 / 2d, truncatedDiscreteDist1.cdf(2), 1E-8
        assertEquals "cdf at 3", 1 / 2d, truncatedDiscreteDist1.cdf(3), 1E-8
        assertEquals "cdf at 4", 1d, truncatedDiscreteDist1.cdf(4)
        assertEquals "cdf at 5", 1d, truncatedDiscreteDist1.cdf(5)

        assertEquals "inverse F at y=0", 0, truncatedDiscreteDist1.inverseF(0)
        assertEquals "inverse F at y=0.1", 2, truncatedDiscreteDist1.inverseF(0.1)
        // todo(jwa): rounding error gives wrong value, should be
        // truncatedDiscreteDist1.distribution.inverseF(2/6d)=2.0 and
        // NOT truncatedDiscreteDist1.distribution.inverseF(0.33333333333334)=4.0
        assertEquals "inverse F at y=0.5", truncatedDiscreteDist1.distribution.inverseF(0.33333333333334), truncatedDiscreteDist1.inverseF(0.5)
        assertEquals "inverse F at y=0.75", 4, truncatedDiscreteDist1.inverseF(0.75)
        assertEquals "inverse F at y=1", 4, truncatedDiscreteDist1.inverseF(1.0)


        truncatedDiscreteDist2 = new TruncatedDistribution(discreteEmpiricalDist, 2, 8.8)

        assertEquals "cdf at 0", 0d, truncatedDiscreteDist2.cdf(0)
        assertEquals "cdf at 1", 0d, truncatedDiscreteDist2.cdf(1)
        assertEquals "cdf at 2", 2 / 7d, truncatedDiscreteDist2.cdf(2), 1E-8
        assertEquals "cdf at 3", 2 / 7d, truncatedDiscreteDist2.cdf(3), 1E-8
        assertEquals "cdf at 4", 4 / 7d, truncatedDiscreteDist2.cdf(4), 1E-8
        assertEquals "cdf at 5", 4 / 7d, truncatedDiscreteDist2.cdf(5), 1E-8
        assertEquals "cdf at 6", 6 / 7d, truncatedDiscreteDist2.cdf(6), 1E-8
        assertEquals "cdf at 7", 6 / 7d, truncatedDiscreteDist2.cdf(7), 1E-8
        assertEquals "cdf at 8", 1d, truncatedDiscreteDist2.cdf(8)
        assertEquals "cdf at 9", 1d, truncatedDiscreteDist2.cdf(9)

        assertEquals "inverse F at y=0", 0, truncatedDiscreteDist2.inverseF(0)
        assertEquals "inverse F at y=0.1", 2, truncatedDiscreteDist2.inverseF(0.1)
        assertEquals "inverse F at y=2/7", 2, truncatedDiscreteDist2.inverseF(2/7d)
        assertEquals "inverse F at y=0.5", 4, truncatedDiscreteDist2.inverseF(0.5)
        assertEquals "inverse F at y=0.75", 6, truncatedDiscreteDist2.inverseF(0.75)
        assertEquals "inverse F at y=1", 8, truncatedDiscreteDist2.inverseF(1.0)
    }

    void testDiscreteDistributionInt() {

        truncatedDiscreteDistInt = new TruncatedDistribution(poissonDist, 1, 4)
        
        double normalizationConstant = poissonDist.cdf(4)-poissonDist.cdf(0)
        double prob1 = 1/normalizationConstant*(5*Math.exp(-5))
        double prob2 = 1/normalizationConstant*(Math.pow(5,2)*Math.exp(-5)/2d)
        double prob3 = 1/normalizationConstant*(Math.pow(5,3)*Math.exp(-5)/(3d*2d))
        double prob4 = 1/normalizationConstant*(Math.pow(5,4)*Math.exp(-5)/(4d*3d*2d))
        double prob5 = 0d


        assertEquals "cdf at 0", 0d, truncatedDiscreteDistInt.cdf(0)
        assertEquals "cdf at 1", poissonDist.prob(1)/normalizationConstant, truncatedDiscreteDistInt.cdf(1)
        assertEquals "cdf at 2", prob1+prob2 , truncatedDiscreteDistInt.cdf(2), 1E-8
        assertEquals "cdf at 3", prob1+prob2+prob3, truncatedDiscreteDistInt.cdf(3), 1E-8
        assertEquals "cdf at 4", 1d, truncatedDiscreteDistInt.cdf(4)
        assertEquals "cdf at 5", 1d, truncatedDiscreteDistInt.cdf(5)

    }

    void testNormalizationConstantForTruncatedDist(){
        // normalization constant for truncated distribution in library from l'ecuyer was wrongly computed;
        // now it should be correct

        Distribution dist = DistributionType.getLognormalDistribution(1.0, 1.0)
          double partitionFunction = dist.cdf(105000) - dist.cdf(95000)
          assertEquals "partition function on truncated interval", 0.0, partitionFunction


          Distribution distTruncated = new TruncatedDist((ContinuousDistribution) dist, (Double) 95000, (Double) 105000)
          assertEquals " density left from interval", 0d, distTruncated.density(94000)
          assertEquals " density within interval", Double.POSITIVE_INFINITY, distTruncated.density(100000)
          assertEquals " density right from interval", 0d, distTruncated.density(106000)
          assertEquals " inverse F(0.9)", Double.POSITIVE_INFINITY, distTruncated.inverseF(0.9)
          assertEquals " cumulative distribution of 1000000", Double.NaN, distTruncated.cdf(100000)

          Distribution dist2 = DistributionType.getLognormalDistribution(1.0, 1.0)
          double partitionFunction2 = dist2.cdf(5) - dist2.cdf(2)
          assertEquals "partition function on truncated interval", true, partitionFunction2 > 0 && partitionFunction2 < Double.POSITIVE_INFINITY

          Distribution dist2Truncated = new TruncatedDist((ContinuousDistribution) dist2, 2d, 5d)
          assertEquals " density left from interval", 0d, dist2Truncated.density(1)
          assertEquals " density at left boundary", dist2.density(2) / partitionFunction2, dist2Truncated.density(2)
          assertEquals " density within interval", dist2.density(3) / partitionFunction2, dist2Truncated.density(3)
          assertEquals " density at right boundary", dist2.density(5) / partitionFunction2, dist2Truncated.density(5)
          assertEquals " density right from interval", 0d, dist2Truncated.density(6)
          assertEquals " inverse F(0.9)", true, dist2Truncated.inverseF(0.9) > 2 && dist2Truncated.inverseF(0.9) < 5
        
    }
}
