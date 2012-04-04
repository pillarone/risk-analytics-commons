package org.pillarone.riskanalytics.domain.utils.math.distribution.varyingparams;

import org.apache.commons.lang.ArrayUtils;
import org.pillarone.riskanalytics.core.parameterization.ConstrainedMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.ConstraintsFactory;
import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;
import org.pillarone.riskanalytics.core.util.GroovyUtils;
import org.pillarone.riskanalytics.domain.utils.InputFormatConverter;
import org.pillarone.riskanalytics.domain.utils.constraint.PeriodDistributionsConstraints;
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionParams;
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionType;
import org.pillarone.riskanalytics.domain.utils.math.distribution.RandomDistribution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
public class ConstantVaryingParametersDistributionStrategy extends AbstractVaryingParameterDistributionStrategy {

    private ConstrainedMultiDimensionalParameter constant = new ConstrainedMultiDimensionalParameter(
            GroovyUtils.convertToListOfList(new Object[]{1, 0d}),
            Arrays.asList(PERIOD, DistributionParams.CONSTANT.toString()),
            ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER));

    public IParameterObjectClassifier getType() {
        return VaryingParametersDistributionType.CONSTANT;
    }

    public Map getParameters() {
        Map<String, ConstrainedMultiDimensionalParameter> parameters = new HashMap<String, ConstrainedMultiDimensionalParameter>(1);
        parameters.put("constant", constant);
        return parameters;
    }

    protected TreeMap<Integer, RandomDistribution> initDistributions() {
        TreeMap<Integer, RandomDistribution> distributionPerPeriod = new TreeMap<Integer, RandomDistribution>();
        int constantColumnIndex = constant.getColumnIndex(DistributionParams.CONSTANT.toString());

        for (int row = constant.getTitleRowCount(); row < constant.getRowCount(); row++) {
            int period = InputFormatConverter.getInt((constant.getValueAt(row, periodColumnIndex))) - 1;
            double constantParam = InputFormatConverter.getDouble(constant.getValueAt(row, constantColumnIndex));
            distributionPerPeriod.put(period, (RandomDistribution) VaryingParametersDistributionType.getStrategy(
                    VaryingParametersDistributionType.CONSTANT, ArrayUtils.toMap(
                    new Object[][]{{DistributionParams.CONSTANT.toString(), constantParam}})));
        }
        return distributionPerPeriod;
    }
}
