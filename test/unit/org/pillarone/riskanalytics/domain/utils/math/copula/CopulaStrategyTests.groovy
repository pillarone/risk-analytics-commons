package org.pillarone.riskanalytics.domain.utils.math.copula

import org.pillarone.riskanalytics.core.util.MathUtils
import umontreal.iro.lecuyer.rng.RandomStreamBase
import org.pillarone.riskanalytics.domain.utils.math.randomnumber.UniformDoubleList
import org.pillarone.riskanalytics.core.parameterization.ComboBoxTableMultiDimensionalParameter
import org.pillarone.riskanalytics.domain.utils.marker.ICorrelationMarker

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
class CopulaStrategyTests extends GroovyTestCase {

    // todo(jwa): compare with existing tests in property-casualty. They seem to be pretty redundant.
    ICopulaStrategy independentStrategy
    ICopulaStrategy frechetUpperBoundStrategy
    ICopulaStrategy normalStrategy

    void testIndependentStrategy(){
       independentStrategy = CopulaType.getStrategy(CopulaType.INDEPENDENT,
            ["targets": new ComboBoxTableMultiDimensionalParameter(["Fire", "Hull", "Legal"],["Targets"], ICorrelationMarker)])

        MathUtils.initRandomStreamBase(1234)
        RandomStreamBase referenceStream = MathUtils.getRandomStream(MathUtils.getRandomStreamBase(), 0).clone()
        List<Double> randomNumbers = UniformDoubleList.getDoubles(3, false, referenceStream);

        List<Number> probabilities = independentStrategy.getRandomVector()

        assertEquals "probabilities associated to targets", true, randomNumbers.containsAll(probabilities)
        assertEquals "size probabilities", 3, probabilities.size()

    }

    void testFrechetUpperBoundStrategy(){
        frechetUpperBoundStrategy = CopulaType.getStrategy(CopulaType.FRECHETUPPERBOUND,
            ["targets": new ComboBoxTableMultiDimensionalParameter(["Fire", "Hull", "Legal"],["Targets"], ICorrelationMarker)])

        MathUtils.initRandomStreamBase(1234)
        RandomStreamBase referenceStream = MathUtils.getRandomStream(MathUtils.getRandomStreamBase(), 0).clone()
        List<Double> randomNumbers = UniformDoubleList.getDoubles(1, false, referenceStream);
        randomNumbers[0..2] = [randomNumbers[0], randomNumbers[0], randomNumbers[0]]
        List<Number> probabilities = frechetUpperBoundStrategy.getRandomVector()

        assertEquals "probabilities associated to targets", true, randomNumbers.containsAll(probabilities)
        assertEquals "size probabilities", 3, probabilities.size()
    }

    void testNormalStrategy(){
   // todo(jwa): more complicated to test explicit values; need simulation toolkit from Markus
    }
}
