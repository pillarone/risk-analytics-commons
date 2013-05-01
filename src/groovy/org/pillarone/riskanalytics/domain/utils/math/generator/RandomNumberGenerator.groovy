package org.pillarone.riskanalytics.domain.utils.math.generator

import umontreal.iro.lecuyer.probdist.BinomialDist
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
            if (generator instanceof RandomVariateGenInt) {
                DiscreteDistributionInt distribution = generator.getDistribution()
                if (distribution instanceof PoissonDist) {
                    distribution.lambda *= scale
                    return generator.nextInt()
                }
                else if (distribution instanceof BinomialDist) {
                    distribution.setParams((int) Math.round(distribution.getN() * scale), distribution.getP())
                    return generator.nextInt()
                }
                else if (distribution instanceof NegativeBinomialDist) {
                    distribution.setParams(distribution.gamma * scale, distribution.p)
                    return generator.nextInt()
                }
                else {
                    int number = 0;
                    for (int i = 0; i < (int) scale; i++) {
                        number += generator.nextInt();
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
