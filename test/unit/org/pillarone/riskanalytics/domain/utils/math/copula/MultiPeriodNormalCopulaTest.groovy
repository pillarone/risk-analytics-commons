package org.pillarone.riskanalytics.domain.utils.math.copula

import org.pillarone.riskanalytics.core.components.Component
import org.pillarone.riskanalytics.core.example.component.TestComponent
import org.pillarone.riskanalytics.domain.utils.math.dependance.GeneratorPeriod
import cern.colt.matrix.impl.DenseDoubleMatrix2D
import org.pillarone.riskanalytics.core.parameterization.PeriodMatrixMultiDimensionalParameter
import org.pillarone.riskanalytics.domain.utils.marker.IPerilMarker
import org.pillarone.riskanalytics.domain.utils.math.dependance.DependancePacket

/**
 *   author simon.parten @ art-allianz . com
 */
class MultiPeriodNormalCopulaTest extends GroovyTestCase {


    void testBuildHeader() {
        MultiPeriodNormalCopula multiPeriodNormalCopula = new MultiPeriodNormalCopula()

        List<Component> compList = [new TestComponent(name: "LOB1"), new TestComponent(name: "LOB2")]
        int maxPeriodsEnteredByUser = 2
        Map<Integer, GeneratorPeriod> aMap = multiPeriodNormalCopula.buildHeader(maxPeriodsEnteredByUser, compList)
        assertEquals "check size", compList.size() * maxPeriodsEnteredByUser, aMap.size()
        assert "check order", aMap.get(1).generatorName.equals("LOB1")
        assert "check order", aMap.get(1).period.equals(1)
        assert "check order", aMap.get(0).period.equals(0)
        assert "check order", aMap.get(3).generatorName.equals("LOB2")
        assert "check order", aMap.get(3).period.equals(1)
        assert "check order", aMap.get(2).period.equals(0)
    }

    void testCheckMatrix() {
        double[][] aMatrix = new double[3][3]
        aMatrix[0][0] = 1d
        aMatrix[1][1] = 1d
        aMatrix[2][2] = 1d
        DenseDoubleMatrix2D sigma = new DenseDoubleMatrix2D(aMatrix);
        MultiPeriodNormalCopula multiPeriodNormalCopula = new MultiPeriodNormalCopula()
        multiPeriodNormalCopula.checkMatrix(sigma)

//        Not symettric

        aMatrix[2][0] = 1d
        DenseDoubleMatrix2D sigmaFail = new DenseDoubleMatrix2D(aMatrix);
        shouldFail {
            multiPeriodNormalCopula.checkMatrix(sigmaFail)
        }
        aMatrix[2][0] = 1d
        aMatrix[0][2] = 1d
        DenseDoubleMatrix2D sigmaFail1 = new DenseDoubleMatrix2D(aMatrix);
//        Not PSD
        shouldFail {
            multiPeriodNormalCopula.checkMatrix(sigmaFail1)
        }

        aMatrix[2][0] = 0.5d
        aMatrix[0][2] = 0.5d
        DenseDoubleMatrix2D sigmaWin = new DenseDoubleMatrix2D(aMatrix);
        multiPeriodNormalCopula.checkMatrix(sigmaWin)
    }

    void testBuildMatrix() {
        PeriodMatrixMultiDimensionalParameter testParameter = new PeriodMatrixMultiDimensionalParameter(new ArrayList(), new ArrayList(), IPerilMarker) {
            @Override
            int getMaxPeriod() {
                return 2
            }

            @Override
            List<PeriodMatrixMultiDimensionalParameter.CorrelationInfo> getCorrelations() {
                List<PeriodMatrixMultiDimensionalParameter.CorrelationInfo> info = new ArrayList<PeriodMatrixMultiDimensionalParameter.CorrelationInfo>()
                Component component1 = new TestComponent(name: "LOB1")
                Component component2 = new TestComponent(name: "LOB2")
//            Diagonal
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info00 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component1, period1: 1, component2: component1, period2: 1, value: 1)
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info11 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component1, period1: 2, component2: component1, period2: 2, value: 1)
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info22 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component2, period1: 1, component2: component2, period2: 1, value: 1)
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info33 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component2, period1: 2, component2: component2, period2: 2, value: 1)
                info << info00
                info << info11
                info << info22
                info << info33

//                Column L1, P0
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info01 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component1, period1: 1, component2: component1, period2: 2, value: 0.1)
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info02 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component1, period1: 1, component2: component2, period2: 1)
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info03 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component1, period1: 1, component2: component2, period2: 2)
                info << info01
                info << info02
                info << info03

//                Column L1, P1
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info10 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component1, period1: 2, component2: component1, period2: 1, value: 0.1)
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info12 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component1, period1: 2, component2: component2, period2: 1)
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info13 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component1, period1: 2, component2: component2, period2: 2)
                info << info10
                info << info12
                info << info13

//                Column L2, P0
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info20 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component2, period1: 1, component2: component1, period2: 1)
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info21 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component2, period1: 1, component2: component1, period2: 2)
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info23 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component2, period1: 1, component2: component2, period2: 2, value: 0.2)
                info << info20
                info << info21
                info << info23

//                Column L2, P1
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info30 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component2, period1: 2, component2: component1, period2: 1)
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info31 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component2, period1: 2, component2: component1, period2: 2)
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info32 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component2, period1: 2, component2: component2, period2: 1, value: 0.2)
                info << info30
                info << info31
                info << info32

                return info
            }
        }

        MultiPeriodNormalCopula multiPeriodNormalCopula = new MultiPeriodNormalCopula(dependencyMatrix: testParameter)
        DenseDoubleMatrix2D matrix2D = multiPeriodNormalCopula.buildMatrix(testParameter)
        assertEquals "check entries", 1d, matrix2D.getQuick(0, 0)
        assertEquals "check entries", 1d, matrix2D.getQuick(1, 1)
        assertEquals "check entries", 0.1d, matrix2D.getQuick(0, 1)
        assertEquals "check entries", 0.1d, matrix2D.getQuick(1, 0)
        assertEquals "check entries", 0.2d, matrix2D.getQuick(3, 2)
        assertEquals "check entries", 0.2d, matrix2D.getQuick(2, 3)
    }

    void testDependancy() {
        PeriodMatrixMultiDimensionalParameter testParameter = new PeriodMatrixMultiDimensionalParameter(new ArrayList(), new ArrayList(), IPerilMarker) {
            @Override
            int getMaxPeriod() {
                return 2
            }

            List<PeriodMatrixMultiDimensionalParameter.CorrelationInfo> getCorrelations() {
                List<PeriodMatrixMultiDimensionalParameter.CorrelationInfo> info = new ArrayList<PeriodMatrixMultiDimensionalParameter.CorrelationInfo>()
                Component component1 = new TestComponent(name: "LOB1")
//            Diagonal
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info00 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component1, period1: 1, component2: component1, period2: 1, value: 1)
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info11 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component1, period1: 2, component2: component1, period2: 2, value: 1)
                info << info00
                info << info11

//                Column L1, P0
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info01 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component1, period1: 1, component2: component1, period2: 2, value: 1)
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info02 = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: component1, period1: 2, component2: component1, period2: 1, value: 1)
                info << info01
                info << info02
                return info
            }
        }
        MultiPeriodNormalCopula multiPeriodNormalCopula = new MultiPeriodNormalCopula(dependencyMatrix: testParameter)
        DependancePacket dependancePacket = multiPeriodNormalCopula.getDependanceAllPeriod(2)
        assert dependancePacket.getMarginal("LOB1", 0).getMarginalProbability() == dependancePacket.getMarginal("LOB1", 1).getMarginalProbability()
    }

}
