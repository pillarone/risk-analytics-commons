package org.pillarone.riskanalytics.domain.utils.math.generator

import umontreal.iro.lecuyer.probdist.Distribution

interface IRandomNumberGenerator {

    Number nextValue()
    Distribution getDistribution()
}