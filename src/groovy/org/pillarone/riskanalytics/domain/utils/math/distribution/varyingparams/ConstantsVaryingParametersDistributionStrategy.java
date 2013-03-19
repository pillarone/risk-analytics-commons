package org.pillarone.riskanalytics.domain.utils.math.distribution.varyingparams;

import org.apache.commons.lang.ArrayUtils;
import org.pillarone.riskanalytics.core.parameterization.ConstrainedMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.ConstraintsFactory;
import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;
import org.pillarone.riskanalytics.core.util.GroovyUtils;
import org.pillarone.riskanalytics.domain.utils.InputFormatConverter;
import org.pillarone.riskanalytics.domain.utils.constraint.PeriodDistributionsConstraints;
import org.pillarone.riskanalytics.domain.utils.math.distribution.ConstantsDistribution;
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionParams;
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionType;
import org.pillarone.riskanalytics.domain.utils.math.distribution.RandomDistribution;

import java.util.*;

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
public class ConstantsVaryingParametersDistributionStrategy extends AbstractVaryingParameterDistributionStrategy {

    private ConstrainedMultiDimensionalParameter constants;

    public IParameterObjectClassifier getType() {
        return VaryingParametersDistributionType.CONSTANTS;
    }

    public Map getParameters() {
        Map<String, ConstrainedMultiDimensionalParameter> parameters = new HashMap<String, ConstrainedMultiDimensionalParameter>(1);
        parameters.put("constants", constants);
        return parameters;
    }

    protected TreeMap<Integer, RandomDistribution> initDistributions() {
        TreeMap<Integer, RandomDistribution> distributionPerPeriod = new TreeMap<Integer, RandomDistribution>();
        for (Map.Entry<Integer, List<Double>> entry : getListOfConstantsPerPeriod().entrySet()) {
            double[] values =  entry.getValue().isEmpty() ? new double[]{0d} : GroovyUtils.asDouble(entry.getValue());
            RandomDistribution distribution = new RandomDistribution();
            distribution.setType(DistributionType.CONSTANTS);
            distribution.setParameters(ArrayUtils.toMap(new Object[][]{{DistributionParams.CONSTANTS, values}}));
            distribution.setDistribution(new ConstantsDistribution(values));
            distributionPerPeriod.put(entry.getKey(), distribution);
        }
        return distributionPerPeriod;
    }

    private SortedMap<Integer, List<Double>> getListOfConstantsPerPeriod() {
        int firstValueRow = constants.getTitleRowCount();
        int constantColumnIndex = constants.getColumnIndex(DistributionParams.CONSTANTS.toString());
        SortedMap<Integer, List<Double>> constantsPerPeriod = new TreeMap<Integer, List<Double>>();
        for (int row = firstValueRow; row <= constants.getColumn(0).size(); row++) {
            int period = InputFormatConverter.getInt(constants.getValueAt(row, periodColumnIndex)) - 1;
            double constant = InputFormatConverter.getDouble(constants.getValueAt(row, constantColumnIndex));
            List<Double> constants = constantsPerPeriod.get(period);
            if (constants == null) {
                constants = new ArrayList<Double>();
                constantsPerPeriod.put(period, constants);
            }
            constants.add(constant);
        }
        return constantsPerPeriod;
    }

}
