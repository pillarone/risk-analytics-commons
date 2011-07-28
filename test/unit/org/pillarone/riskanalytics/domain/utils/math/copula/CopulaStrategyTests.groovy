package org.pillarone.riskanalytics.domain.utils.math.copula

import org.pillarone.riskanalytics.core.util.MathUtils
import umontreal.iro.lecuyer.rng.RandomStreamBase
import org.pillarone.riskanalytics.domain.utils.math.randomnumber.UniformDoubleList
import org.pillarone.riskanalytics.core.parameterization.ComboBoxTableMultiDimensionalParameter
import org.pillarone.riskanalytics.domain.utils.marker.ICorrelationMarker
import org.pillarone.riskanalytics.core.parameterization.ComboBoxMatrixMultiDimensionalParameter
import umontreal.iro.lecuyer.probdist.NormalDist
import org.pillarone.riskanalytics.domain.utils.math.generator.IRandomNumberGenerator
import org.pillarone.riskanalytics.domain.utils.math.generator.RandomNumberGeneratorFactory
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionType
import umontreal.iro.lecuyer.probdist.ChiSquareDist
import umontreal.iro.lecuyer.probdist.StudentDist

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
class CopulaStrategyTests extends GroovyTestCase {

    ICopulaStrategy independentCopulaStrategy
    ICopulaStrategy frechetUpperBoundCopulaStrategy
    ICopulaStrategy normalCopulaStrategy
    ICopulaStrategy tCopulaStrategy

    void testIndependentCopulaStrategy() {
        independentCopulaStrategy = CopulaType.getStrategy(CopulaType.INDEPENDENT,
                ["targets": new ComboBoxTableMultiDimensionalParameter(["Fire", "Hull", "Legal"], ["Targets"], ICorrelationMarker)])

        MathUtils.initRandomStreamBase(1234)
        RandomStreamBase referenceStream = MathUtils.getRandomStream(MathUtils.getRandomStreamBase(), 0).clone()
        List<Double> randomNumbers = UniformDoubleList.getDoubles(3, false, referenceStream);

        List<Number> probabilities = independentCopulaStrategy.getRandomVector()

        assertEquals "probabilities associated to targets", true, randomNumbers.containsAll(probabilities)
        assertEquals "size probabilities", 3, probabilities.size()

    }

    void testFrechetUpperBoundCopulaStrategy() {
        frechetUpperBoundCopulaStrategy = CopulaType.getStrategy(CopulaType.FRECHETUPPERBOUND,
                ["targets": new ComboBoxTableMultiDimensionalParameter(["Fire", "Hull", "Legal"], ["Targets"], ICorrelationMarker)])

        MathUtils.initRandomStreamBase(1234)
        RandomStreamBase referenceStream = MathUtils.getRandomStream(MathUtils.getRandomStreamBase(), 0).clone()
        List<Double> randomNumbers = UniformDoubleList.getDoubles(1, false, referenceStream);
        randomNumbers[0..2] = [randomNumbers[0], randomNumbers[0], randomNumbers[0]]

        List<Number> probabilities = frechetUpperBoundCopulaStrategy.getRandomVector()

        assertEquals "probabilities associated to targets", true, randomNumbers.containsAll(probabilities)
        assertEquals "size probabilities", 3, probabilities.size()
    }

    void testNormalCopulaStrategy() {
        // for computation cf. document "Dependence Modelling via the Copula Method" by Helen Arnold --> Simulation Toolkit

        normalCopulaStrategy = CopulaType.getStrategy(CopulaType.NORMAL,
                ["dependencyMatrix": new ComboBoxMatrixMultiDimensionalParameter([[1d, 0d], [0d, 1d]],
                        ['fire', 'hull'], ICorrelationMarker)])

        MathUtils.initRandomStreamBase(1234)
        RandomStreamBase referenceStream = MathUtils.getRandomStream(MathUtils.getRandomStreamBase(), 0).clone()
        List<Double> randomNumbers = UniformDoubleList.getDoubles(6, false, referenceStream);

        List<Number> probabilities = normalCopulaStrategy.getRandomVector()

        assertEquals "size probabilities", 2, probabilities.size()
        assertEquals "probability one", randomNumbers[0], (Double) probabilities[0], 1E-14
        assertEquals "probability two", randomNumbers[1], (Double) probabilities[1], 1E-14

        normalCopulaStrategy = CopulaType.getStrategy(CopulaType.NORMAL,
                ["dependencyMatrix": new ComboBoxMatrixMultiDimensionalParameter([[1d, 1d], [1d, 1d]],
                        ['fire', 'hull'], ICorrelationMarker)])
        probabilities = normalCopulaStrategy.getRandomVector()

        assertEquals "size probabilities", 2, probabilities.size()
        assertEquals "probability one", randomNumbers[2], (Double) probabilities[0], 1E-14
        assertEquals "probability two", randomNumbers[2], (Double) probabilities[1], 1E-14

        normalCopulaStrategy = CopulaType.getStrategy(CopulaType.NORMAL,
                ["dependencyMatrix": new ComboBoxMatrixMultiDimensionalParameter([[1d, 0.5d], [0.5d, 1d]],
                        ['fire', 'hull'], ICorrelationMarker)])
        probabilities = normalCopulaStrategy.getRandomVector()

        double x2 = 0.5 * NormalDist.inverseF(0d, 1d, randomNumbers[4]) + Math.sqrt(1d - Math.pow(0.5, 2)) * NormalDist.inverseF(0d, 1d, randomNumbers[5])
        assertEquals "size probabilities", 2, probabilities.size()
        assertEquals "probability one", randomNumbers[4], (Double) probabilities[0], 1E-14
        assertEquals "probability two", NormalDist.cdf(0d, 1d, x2), (Double) probabilities[1], 1E-14
    }

    void testTCopulaStrategy() {
        // for computation cf. document "Dependence Modelling via the Copula Method" by Helen Arnold --> Simulation Toolkit

        int degreesOfFreedom = 10
        tCopulaStrategy = CopulaType.getStrategy(CopulaType.T,
                ["dependencyMatrix": new ComboBoxMatrixMultiDimensionalParameter([[1d, 0d], [0d, 1d]],
                        ['fire', 'hull'], ICorrelationMarker),"degreesOfFreedom": degreesOfFreedom])

        MathUtils.initRandomStreamBase(1234)
        RandomStreamBase referenceStream = MathUtils.getRandomStream(MathUtils.getRandomStreamBase(), 0).clone()
        List<Double> randomNumbers = UniformDoubleList.getDoubles(9, false, referenceStream);

        List<Number> probabilities = tCopulaStrategy.getRandomVector()

        double x1 = NormalDist.inverseF(0d,1d,randomNumbers[0])
        double x2 = NormalDist.inverseF(0d, 1d, randomNumbers[1])
        double factor = (double) degreesOfFreedom / ChiSquareDist.inverseF(degreesOfFreedom, randomNumbers[2]);
        x1 = Math.sqrt(factor)*x1
        x2 = Math.sqrt(factor)*x2
        assertEquals "size probabilities", 2, probabilities.size()
        assertEquals "probability one", StudentDist.cdf(degreesOfFreedom,x1), (Double) probabilities[0], 1E-14
        assertEquals "probability two", StudentDist.cdf(degreesOfFreedom,x2), (Double) probabilities[1], 1E-14

        degreesOfFreedom = 5
        tCopulaStrategy = CopulaType.getStrategy(CopulaType.T,
                ["dependencyMatrix": new ComboBoxMatrixMultiDimensionalParameter([[1d, 1d], [1d, 1d]],
                        ['fire', 'hull'], ICorrelationMarker),"degreesOfFreedom": degreesOfFreedom])
        probabilities = tCopulaStrategy.getRandomVector()

        x1 = NormalDist.inverseF(0d,1d,randomNumbers[3])
        x2 = x1
        factor = (double) degreesOfFreedom / ChiSquareDist.inverseF(degreesOfFreedom, randomNumbers[5]);
        x1 = Math.sqrt(factor)*x1
        x2 = Math.sqrt(factor)*x2
        assertEquals "size probabilities", 2, probabilities.size()
        assertEquals "probability one", StudentDist.cdf(degreesOfFreedom,x1), (Double) probabilities[0], 1E-14
        assertEquals "probability two", StudentDist.cdf(degreesOfFreedom,x2), (Double) probabilities[1], 1E-14

        tCopulaStrategy = CopulaType.getStrategy(CopulaType.T,
                ["dependencyMatrix": new ComboBoxMatrixMultiDimensionalParameter([[1d, 0.5d], [0.5d, 1d]],
                        ['fire', 'hull'], ICorrelationMarker),"degreesOfFreedom": degreesOfFreedom])
        probabilities = tCopulaStrategy.getRandomVector()

        x1 = NormalDist.inverseF(0d,1d,randomNumbers[6])
        x2 = 0.5 * NormalDist.inverseF(0d, 1d, randomNumbers[6]) + Math.sqrt(1d - Math.pow(0.5, 2)) * NormalDist.inverseF(0d, 1d, randomNumbers[7])
        factor = (double) degreesOfFreedom / ChiSquareDist.inverseF(degreesOfFreedom, randomNumbers[8]);
        x1 = Math.sqrt(factor)*x1
        x2 = Math.sqrt(factor)*x2
        assertEquals "size probabilities", 2, probabilities.size()
        assertEquals "probability one",StudentDist.cdf(degreesOfFreedom,x1), (Double) probabilities[0], 1E-14
        assertEquals "probability two", StudentDist.cdf(degreesOfFreedom,x2), (Double) probabilities[1], 1E-14
    }


}
