package org.pillarone.riskanalytics.domain.utils.math.distribution.varyingparams;

import org.apache.commons.lang.ArrayUtils;
import org.pillarone.riskanalytics.core.parameterization.ConstrainedMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;
import org.pillarone.riskanalytics.core.util.GroovyUtils;
import org.pillarone.riskanalytics.domain.utils.InputFormatConverter;
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionParams;
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionType;
import org.pillarone.riskanalytics.domain.utils.math.distribution.RandomDistribution;

import java.util.*;

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
public class DiscreteEmpiricalCumulativeVaryingParametersDistributionStrategy extends AbstractVaryingParameterDistributionStrategy {

    private ConstrainedMultiDimensionalParameter discreteEmpiricalCumulativeValues;

    public IParameterObjectClassifier getType() {
        return VaryingParametersDistributionType.DISCRETEEMPIRICALCUMULATIVE;
    }

    public Map getParameters() {
        Map<String, ConstrainedMultiDimensionalParameter> parameters = new HashMap<String, ConstrainedMultiDimensionalParameter>(1);
        parameters.put("discreteEmpiricalCumulativeValues", discreteEmpiricalCumulativeValues);
        return parameters;
    }

    protected TreeMap<Integer, RandomDistribution> initDistributions() {
        TreeMap<Integer, RandomDistribution> distributionPerPeriod = new TreeMap<Integer, RandomDistribution>();
        for (Map.Entry<Integer, List<List<Double>>> entry : getListOfObservationsPerPeriod().entrySet()) {
            double[] observations =  entry.getValue().isEmpty() ? new double[]{0d} : GroovyUtils.asDouble(entry.getValue().get(0));
            double[] probabilities =  entry.getValue().isEmpty() ? new double[]{0d} : GroovyUtils.asDouble(entry.getValue().get(1));
            RandomDistribution distribution = new RandomDistribution();
            distribution.setType(DistributionType.DISCRETEEMPIRICAL);
            distribution.setParameters(ArrayUtils.toMap(new Object[][]{
                    {DistributionParams.OBSERVATIONS, observations},
                    {DistributionParams.CUMULATIVE_PROBABILITES, probabilities}}));
            distribution.setDistribution(DistributionType.getDiscreteEmpiricalCumulativeDistribution(observations, probabilities));
            distributionPerPeriod.put(entry.getKey(), distribution);
        }
        return distributionPerPeriod;
    }

    private SortedMap<Integer, List<List<Double>>> getListOfObservationsPerPeriod() {
        int firstValueRow = discreteEmpiricalCumulativeValues.getTitleRowCount();
        int observationsColumnIndex = discreteEmpiricalCumulativeValues.getColumnIndex(DistributionParams.OBSERVATIONS.toString());
        int probabilitiesColumnIndex = discreteEmpiricalCumulativeValues.getColumnIndex(DistributionParams.CUMULATIVE_PROBABILITES.toString());
        SortedMap<Integer, List<List<Double>>> listOfValuesPerPeriod = new TreeMap<Integer, List<List<Double>>>();
        for (int row = firstValueRow; row <= discreteEmpiricalCumulativeValues.getColumn(0).size(); row++) {
            int period = InputFormatConverter.getInt(discreteEmpiricalCumulativeValues.getValueAt(row, periodColumnIndex)) - 1;
            double value = InputFormatConverter.getDouble(discreteEmpiricalCumulativeValues.getValueAt(row, observationsColumnIndex));
            double probability = InputFormatConverter.getDouble(discreteEmpiricalCumulativeValues.getValueAt(row, probabilitiesColumnIndex));
            List<Double> values = new ArrayList<Double>();
            List<Double> probabilities = new ArrayList<Double>();
            if (listOfValuesPerPeriod.containsKey(period)) {
                values = listOfValuesPerPeriod.get(period).get(0);
                probabilities = listOfValuesPerPeriod.get(period).get(1);
            }
            else {
                listOfValuesPerPeriod.put(period, Arrays.asList(values, probabilities));
            }
            values.add(value);
            probabilities.add(probability);
        }
        return listOfValuesPerPeriod;
    }

}
