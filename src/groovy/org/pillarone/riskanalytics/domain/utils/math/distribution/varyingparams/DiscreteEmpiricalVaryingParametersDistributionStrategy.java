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
public class DiscreteEmpiricalVaryingParametersDistributionStrategy extends AbstractVaryingParameterDistributionStrategy {

    private ConstrainedMultiDimensionalParameter discreteEmpiricalValues;

    public IParameterObjectClassifier getType() {
        return VaryingParametersDistributionType.DISCRETEEMPIRICAL;
    }

    public Map getParameters() {
        Map<String, ConstrainedMultiDimensionalParameter> parameters = new HashMap<String, ConstrainedMultiDimensionalParameter>(1);
        parameters.put("discreteEmpiricalValues", discreteEmpiricalValues);
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
                    {DistributionParams.PROBABILITIES, probabilities}}));
            distribution.setDistribution(DistributionType.getDiscreteEmpiricalDistribution(observations, probabilities));
            distributionPerPeriod.put(entry.getKey(), distribution);
        }
        return distributionPerPeriod;
    }

    private SortedMap<Integer, List<List<Double>>> getListOfObservationsPerPeriod() {
        int firstValueRow = discreteEmpiricalValues.getTitleRowCount();
        int observationsColumnIndex = discreteEmpiricalValues.getColumnIndex(DistributionParams.OBSERVATIONS.toString());
        int probabilitiesColumnIndex = discreteEmpiricalValues.getColumnIndex(DistributionParams.PROBABILITIES.toString());
        SortedMap<Integer, List<List<Double>>> listOfValuesPerPeriod = new TreeMap<Integer, List<List<Double>>>();
        for (int row = firstValueRow; row <= discreteEmpiricalValues.getColumn(0).size(); row++) {
            int period = InputFormatConverter.getInt(discreteEmpiricalValues.getValueAt(row, periodColumnIndex)) - 1;
            double value = InputFormatConverter.getDouble(discreteEmpiricalValues.getValueAt(row, observationsColumnIndex));
            double probability = InputFormatConverter.getDouble(discreteEmpiricalValues.getValueAt(row, probabilitiesColumnIndex));
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
