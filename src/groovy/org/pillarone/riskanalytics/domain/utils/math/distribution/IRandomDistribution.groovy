package org.pillarone.riskanalytics.domain.utils.math.distribution

import umontreal.iro.lecuyer.probdist.Distribution

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public interface IRandomDistribution {

    Distribution distribution
    Map parameters

    DistributionType getDistributionType();

}