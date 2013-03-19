package org.pillarone.riskanalytics.domain.utils.math.distribution.varyingparams;

import org.apache.commons.lang.ArrayUtils;
import org.pillarone.riskanalytics.core.parameterization.ConstrainedMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;
import org.pillarone.riskanalytics.core.util.GroovyUtils;
import org.pillarone.riskanalytics.domain.utils.InputFormatConverter;
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionParams;
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionType;
import org.pillarone.riskanalytics.domain.utils.math.distribution.PiecewiseLinearDistribution;
import org.pillarone.riskanalytics.domain.utils.math.distribution.RandomDistribution;

import java.util.*;

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
public class PiecewiseLinearVaryingParametersDistributionStrategy extends AbstractVaryingParameterDistributionStrategy {

    private ConstrainedMultiDimensionalParameter supportPoints;

    public IParameterObjectClassifier getType() {
        return VaryingParametersDistributionType.PIECEWISELINEAR;
    }

    public Map getParameters() {
        Map<String, ConstrainedMultiDimensionalParameter> parameters = new HashMap<String, ConstrainedMultiDimensionalParameter>(1);
        parameters.put("supportPoints", supportPoints);
        return parameters;
    }

    protected TreeMap<Integer, RandomDistribution> initDistributions() {
        TreeMap<Integer, RandomDistribution> distributionPerPeriod = new TreeMap<Integer, RandomDistribution>();
        for (Map.Entry<Integer, List<List<Double>>> entry : getListOfObservationsPerPeriod().entrySet()) {
            double[] values =  entry.getValue().isEmpty() ? new double[]{0d} : GroovyUtils.asDouble(entry.getValue().get(0));
            double[] cumulativeProbabilities =  entry.getValue().isEmpty() ? new double[]{0d} : GroovyUtils.asDouble(entry.getValue().get(1));
            RandomDistribution distribution = new RandomDistribution();
            distribution.setType(DistributionType.PIECEWISELINEAR);
            distribution.setParameters(ArrayUtils.toMap(new Object[][]{
                    {DistributionParams.VALUES, values},
                    {DistributionParams.CUMULATIVE_PROBABILITES, cumulativeProbabilities}}));
            distribution.setDistribution(new PiecewiseLinearDistribution(values, cumulativeProbabilities));
            distributionPerPeriod.put(entry.getKey(), distribution);
        }
        return distributionPerPeriod;
    }

    private SortedMap<Integer, List<List<Double>>> getListOfObservationsPerPeriod() {
        int firstValueRow = supportPoints.getTitleRowCount();
        int valuesColumnIndex = supportPoints.getColumnIndex(DistributionParams.VALUES.toString());
        int cumProbColumnIndex = supportPoints.getColumnIndex(DistributionParams.CUMULATIVE_PROBABILITES.toString());
        SortedMap<Integer, List<List<Double>>> listOfValuesPerPeriod = new TreeMap<Integer, List<List<Double>>>();
        for (int row = firstValueRow; row <= supportPoints.getColumn(0).size(); row++) {
            int period = InputFormatConverter.getInt(supportPoints.getValueAt(row, periodColumnIndex)) - 1;
            double value = InputFormatConverter.getDouble(supportPoints.getValueAt(row, valuesColumnIndex));
            double cumProbability = InputFormatConverter.getDouble(supportPoints.getValueAt(row, cumProbColumnIndex));
            List<Double> values = new ArrayList<Double>();
            List<Double> cumProbabilities = new ArrayList<Double>();
            if (listOfValuesPerPeriod.containsKey(period)) {
                values = listOfValuesPerPeriod.get(period).get(0);
                cumProbabilities = listOfValuesPerPeriod.get(period).get(1);
            }
            else {
                listOfValuesPerPeriod.put(period, Arrays.asList(values, cumProbabilities));
            }
            values.add(value);
            cumProbabilities.add(cumProbability);
        }
        return listOfValuesPerPeriod;
    }

}
