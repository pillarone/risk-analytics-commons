package org.pillarone.riskanalytics.domain.utils.math.generator

import umontreal.iro.lecuyer.probdist.BinomialDist
import umontreal.iro.lecuyer.probdist.DiscreteDistribution
import umontreal.iro.lecuyer.probdist.DiscreteDistributionInt
import umontreal.iro.lecuyer.probdist.Distribution
import umontreal.iro.lecuyer.probdist.NegativeBinomialDist
import umontreal.iro.lecuyer.probdist.PoissonDist
import umontreal.iro.lecuyer.randvar.RandomVariateGen
import umontreal.iro.lecuyer.randvar.RandomVariateGenInt
import org.pillarone.riskanalytics.core.parameterization.AbstractParameterObject

import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionType
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionModifier

class RandomNumberGenerator extends AbstractParameterObject implements IRandomNumberGenerator {

    protected RandomVariateGen generator
    DistributionType type
    DistributionModifier modifier
    Map parameters

    DistributionType getType() {
        type
    }

    public Number nextValue() {
        assert generator != null
        if (generator instanceof RandomVariateGenInt) {
            return generator.nextInt()
        }
        return generator.nextDouble()
    }

    /**
     *
     * @param scale factor applied to the random number for most distribution, for some discrete distribution the
     *              distribution itself is modified with this factor before drawing the number
     * @return
     */
    @Override
    Number nextValue(double scale) {
        if (scale == 1) {
            return nextValue()
        }
        else {
            if (generator instanceof RandomVariateGen) {
                Distribution distribution = generator.getDistribution()
                if (distribution instanceof PoissonDist) {
                    double lambda = distribution.lambda
                    distribution.setLambda(lambda * scale)
                    double generatedValue = generator.nextDouble()
                    distribution.setLambda(lambda)
                    return generatedValue
                }
                else if (distribution instanceof BinomialDist) {
                    int n = distribution.getN()
                    distribution.setParams((int) Math.round(n * scale), distribution.getP())
                    double generatedValue = generator.nextDouble()
                    distribution.setParams((int) Math.round(n), distribution.getP())
                    return generatedValue
                }
                else if (distribution instanceof NegativeBinomialDist) {
                    double gamma = distribution.gamma
                    distribution.setParams(gamma * scale, distribution.p)
                    double generatedValue = generator.nextDouble()
                    distribution.setParams(gamma, distribution.p)
                    return generatedValue
                }
                else {
                    int number = 0;
                    for (int i = 0; i < (int) scale; i++) {
                        number += generator.nextDouble();
                    }
                    return number
                }
            }
            return generator.nextDouble() * scale
        }
    }

    public Distribution getDistribution() {
        generator.distribution
    }
}
