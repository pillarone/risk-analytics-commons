package org.pillarone.riskanalytics.domain.utils.math.distribution.varyingparams;

import org.apache.commons.lang.ArrayUtils;
import org.pillarone.riskanalytics.core.parameterization.ConstrainedMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.ConstraintsFactory;
import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;
import org.pillarone.riskanalytics.core.util.GroovyUtils;
import org.pillarone.riskanalytics.domain.utils.InputFormatConverter;
import org.pillarone.riskanalytics.domain.utils.constraint.PeriodDistributionsConstraints;
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionParams;
import org.pillarone.riskanalytics.domain.utils.math.distribution.RandomDistribution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
public class PoissonVaryingParametersDistributionStrategy extends AbstractVaryingParameterDistributionStrategy {

    private ConstrainedMultiDimensionalParameter lambda = new ConstrainedMultiDimensionalParameter(GroovyUtils.convertToListOfList(new Object[]{1, 0d}),
            Arrays.asList(DistributionParams.PERIOD, DistributionParams.LAMBDA), ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER));

    public IParameterObjectClassifier getType() {
        return VaryingParametersDistributionType.POISSON;
    }

    public Map getParameters() {
        Map<String, ConstrainedMultiDimensionalParameter> parameters = new HashMap<String, ConstrainedMultiDimensionalParameter>(1);
        parameters.put("lambda", lambda);
        return parameters;
    }

    protected TreeMap<Integer, RandomDistribution> initDistributions() {
        TreeMap<Integer, RandomDistribution> distributionPerPeriod = new TreeMap<Integer, RandomDistribution>();
        int lambaColumnIndex = lambda.getColumnIndex(DistributionParams.LAMBDA.toString());

        for (int row = lambda.getTitleRowCount(); row < lambda.getRowCount(); row++) {
            int period = InputFormatConverter.getInt((lambda.getValueAt(row, periodColumnIndex))) - 1;
            double lambdaParam = InputFormatConverter.getDouble(lambda.getValueAt(row, lambaColumnIndex));
            distributionPerPeriod.put(period, (RandomDistribution) VaryingParametersDistributionType.getStrategy(
                    VaryingParametersDistributionType.POISSON, ArrayUtils.toMap(
                        new Object[][]{{DistributionParams.LAMBDA, lambdaParam}})));
        }
        return distributionPerPeriod;
    }
}
