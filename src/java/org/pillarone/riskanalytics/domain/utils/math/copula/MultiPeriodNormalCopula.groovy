package org.pillarone.riskanalytics.domain.utils.math.copula

import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier
import org.pillarone.riskanalytics.core.parameterization.AbstractParameterObject
import org.pillarone.riskanalytics.domain.utils.math.dependance.DependancePacket
import org.pillarone.riskanalytics.core.components.Component
import org.pillarone.riskanalytics.core.parameterization.PeriodMatrixMultiDimensionalParameter

import org.pillarone.riskanalytics.core.simulation.SimulationException
import org.pillarone.riskanalytics.core.util.MathUtils
import org.pillarone.riskanalytics.domain.utils.math.dependance.GeneratorPeriod
import cern.colt.matrix.impl.DenseDoubleMatrix2D
import cern.colt.matrix.DoubleMatrix2D
import cern.colt.matrix.linalg.EigenvalueDecomposition
import cern.colt.matrix.DoubleMatrix1D
import umontreal.iro.lecuyer.randvarmulti.MultinormalCholeskyGen
import umontreal.iro.lecuyer.randvar.NormalGen
import umontreal.iro.lecuyer.probdist.NormalDist
import com.google.common.collect.Maps
/**
 *   author simon.parten @ art-allianz . com
 */
class MultiPeriodNormalCopula extends AbstractParameterObject implements ICopulaStrategy {

    static final CopulaType type = CopulaType.MULTI_PERIOD_NORMAL;

    private PeriodMatrixMultiDimensionalParameter dependencyMatrix

    public IParameterObjectClassifier getType() {
        return type;
    }

    public DependancePacket getDependanceAllPeriod(Integer finalModelPeriod) {
        DenseDoubleMatrix2D sigma = buildMatrix(dependencyMatrix)
        checkMatrix(sigma)
        MultinormalCholeskyGen generator = new MultinormalCholeskyGen(
                new NormalGen(MathUtils.getRandomStreamBase(), new NormalDist(0d, 1d)),
                new double[ getTargetComponents().size() * dependencyMatrix.getMaxPeriod() ],
                sigma
        )

        double[] correlatedResults = new double[getTargetComponents().size() * dependencyMatrix.getMaxPeriod()]
        generator.nextPoint(correlatedResults)

        Map<Integer, GeneratorPeriod> generatorsAndPeriods = buildHeader(dependencyMatrix.getMaxPeriod(), getTargetComponents() )
        if(generatorsAndPeriods.entrySet().size() != correlatedResults.size()) {
            throw new SimulationException("We're boned. Contact development")
        }

        DependancePacket dependancePacket = new DependancePacket(getTargetComponents())
        for(int index = 0; index < correlatedResults.size() ; index++) {
            double correlatedResult = correlatedResults[index]
            GeneratorPeriod generatorPeriod = generatorsAndPeriods.get(index)
            double marginalProbability =  NormalDist.cdf(0, 1d, correlatedResult)
            dependancePacket.addMarginal(generatorPeriod.getGeneratorName(), generatorPeriod.getPeriod(), marginalProbability)
        }
        return dependancePacket.immutable()
    }

    void checkMatrix(DenseDoubleMatrix2D sigma) {

        for (int i = 0; i < sigma.columns() ; i++) {
            if (!(sigma.getQuick(i,i).doubleValue() == 1)) {
                throw new SimulationException("MultiNormalPeriodCopula has values not equal to 1 on the diagonal");
            }
        }
        DoubleMatrix2D sigmaTranspose = sigma.viewDice();
        if (!sigmaTranspose.equals(sigma)) {
            throw new SimulationException("MultiPeriodCopula not symetric. Please check it and contact development");
        }
        EigenvalueDecomposition eigenvalueDecomp = new EigenvalueDecomposition(sigma);
        DoubleMatrix1D eigenvalues = eigenvalueDecomp.getRealEigenvalues();
        eigenvalues.viewSorted();
        if (eigenvalues.get(0) <= 0) {
            throw new SimulationException("Multi Period Copula not positive definite. Please check it. Eigenvalue of " + eigenvalues.get(0) + "detected.");
        }
    }

    DenseDoubleMatrix2D buildMatrix(PeriodMatrixMultiDimensionalParameter depMatrix) {
        List<Component> componentList = getTargetComponents()
        int maxModelPeriod = depMatrix.getMaxPeriod()
        Map<Integer, GeneratorPeriod> columnHeadings = buildHeader(maxModelPeriod, componentList)
        Map<Integer, GeneratorPeriod> rowHeadings = buildHeader(maxModelPeriod, componentList)

        Collection<PeriodMatrixMultiDimensionalParameter.CorrelationInfo> corrInfo = depMatrix.correlations
        DenseDoubleMatrix2D sigma = new DenseDoubleMatrix2D(componentList.size() * maxModelPeriod, componentList.size() * maxModelPeriod);
        int componentNumber = 0

        int columnNumber = 0
        int rowNumber = 0
        for (Map.Entry<Integer, GeneratorPeriod> column in columnHeadings.entrySet()) {
            for (Map.Entry<Integer, GeneratorPeriod> row in rowHeadings.entrySet()) {
//                PeriodMatrixMultiDimensionalParameter.CorrelationInfo searchInfo = new PeriodMatrixMultiDimensionalParameter.CorrelationInfo(component1: column.getValue().component, period1: column.getValue().getPeriod(), component2: row.getValue().component, period2: row.getValue().getPeriod())
                PeriodMatrixMultiDimensionalParameter.CorrelationInfo info = corrInfo.find {it ->
                    it.component1.getName().equals(column.getValue().component.getName()) &&
                    (it.period1 - 1).equals(column.getValue().getPeriod()) &&
                    it.component2.getName().equals(row.getValue().component.getName()) &&
                    (it.period2 - 1).equals(row.getValue().getPeriod())
                }
                sigma.set(rowNumber , columnNumber, info.getValue())
                rowNumber++
            }
            columnNumber++
            rowNumber = 0
        }
        return sigma
    }

    /**
     * It's very important that this function returns the header with a well defined and unique order.
     *
     * @param maxPeriod
     * @param componentList
     * @return
     */
    Map<Integer, GeneratorPeriod> buildHeader(int maxPeriod, List<Component> componentList ) {
        Map<Integer, GeneratorPeriod> rowHeadings = Maps.newHashMap()
        int i = 0
        for (Component comp in componentList) {
            for (int period = 0; period < maxPeriod; period++) {
                rowHeadings.put(i, new GeneratorPeriod(comp, period ))
                i++
            }
        }
        return rowHeadings
    }

    DependancePacket getDependance(Integer modelPeriod) {
        throw new SimulationException("Improper use of class; Multi period dependancy must be generated only once and referenced afterward.")
    }

    List<Component> getTargetComponents() {
        Collection<PeriodMatrixMultiDimensionalParameter.CorrelationInfo> correlationInfoList = dependencyMatrix.correlations
        Set<Component> componentSet = new HashSet<Component>(correlationInfoList.collectAll { it -> it.getComponent1() })
        return componentSet.toList().sort{ it.getName() }
    }

    List<String> getTargetNames() {
        throw new SimulationException("method not implemented")
    }

    List<Number> getRandomVector() {
        throw new SimulationException("method not implemented")
    }

    Map getParameters() {
        Map<String, Object> map = Maps.newHashMap()
        map.put(CopulaType.DEPENDANCY_MATRIX, dependencyMatrix)
        return map
    }

}

