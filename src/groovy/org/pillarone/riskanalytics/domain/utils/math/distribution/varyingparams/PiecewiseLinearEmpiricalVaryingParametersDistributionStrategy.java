package org.pillarone.riskanalytics.domain.utils.math.distribution.varyingparams;

import org.apache.commons.lang.ArrayUtils;
import org.pillarone.riskanalytics.core.parameterization.ConstrainedMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;
import org.pillarone.riskanalytics.core.util.GroovyUtils;
import org.pillarone.riskanalytics.domain.utils.InputFormatConverter;
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionParams;
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionType;
import org.pillarone.riskanalytics.domain.utils.math.distribution.RandomDistribution;
import umontreal.iro.lecuyer.probdist.PiecewiseLinearEmpiricalDist;

import java.util.*;

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
public class PiecewiseLinearEmpiricalVaryingParametersDistributionStrategy extends AbstractVaryingParameterDistributionStrategy {

    private ConstrainedMultiDimensionalParameter observations;

    public IParameterObjectClassifier getType() {
        return VaryingParametersDistributionType.PIECEWISELINEAREMPIRICAL;
    }

    public Map getParameters() {
        Map<String, ConstrainedMultiDimensionalParameter> parameters = new HashMap<String, ConstrainedMultiDimensionalParameter>(1);
        parameters.put("observations", observations);
        return parameters;
    }

    protected TreeMap<Integer, RandomDistribution> initDistributions() {
        TreeMap<Integer, RandomDistribution> distributionPerPeriod = new TreeMap<Integer, RandomDistribution>();
        for (Map.Entry<Integer, List<Double>> entry : getListOfObservationsPerPeriod().entrySet()) {
            double[] observations =  entry.getValue().isEmpty() ? new double[]{0d} : GroovyUtils.asDouble(entry.getValue());
            RandomDistribution distribution = new RandomDistribution();
            distribution.setType(DistributionType.PIECEWISELINEAREMPIRICAL);
            distribution.setParameters(ArrayUtils.toMap(new Object[][]{{DistributionParams.OBSERVATIONS, observations}}));
            distribution.setDistribution(new PiecewiseLinearEmpiricalDist(observations));
            distributionPerPeriod.put(entry.getKey(), distribution);
        }
        return distributionPerPeriod;
    }

    private SortedMap<Integer, List<Double>> getListOfObservationsPerPeriod() {
        int firstValueRow = observations.getTitleRowCount();
        int observationsColumnIndex = observations.getColumnIndex(DistributionParams.OBSERVATIONS.toString());
        SortedMap<Integer, List<Double>> observationsPerPeriod = new TreeMap<Integer, List<Double>>();
        for (int row = firstValueRow; row <= observations.getColumn(0).size(); row++) {
            int period = InputFormatConverter.getInt(observations.getValueAt(row, periodColumnIndex)) - 1;
            double observation = InputFormatConverter.getDouble(observations.getValueAt(row, observationsColumnIndex));
            List<Double> observations = observationsPerPeriod.get(period);
            if (observations == null) {
                observations = new ArrayList<Double>();
                observationsPerPeriod.put(period, observations);
            }
            observations.add(observation);
        }
        return observationsPerPeriod;
    }

}
