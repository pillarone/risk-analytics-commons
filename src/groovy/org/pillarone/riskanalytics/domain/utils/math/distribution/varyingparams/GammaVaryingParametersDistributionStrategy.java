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
public class GammaVaryingParametersDistributionStrategy extends AbstractVaryingParameterDistributionStrategy {

    private ConstrainedMultiDimensionalParameter alphaAndLambda = new ConstrainedMultiDimensionalParameter(
            GroovyUtils.convertToListOfList(new Object[]{1, 2d, 2d}),
            Arrays.asList(DistributionParams.PERIOD, DistributionParams.ALPHA, DistributionParams.LAMBDA),
            ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER));

    public IParameterObjectClassifier getType() {
        return VaryingParametersDistributionType.GAMMA;
    }

    public Map getParameters() {
        Map<String, ConstrainedMultiDimensionalParameter> parameters = new HashMap<String, ConstrainedMultiDimensionalParameter>(1);
        parameters.put("alphaAndLambda", alphaAndLambda);
        return parameters;
    }

    protected TreeMap<Integer, RandomDistribution> initDistributions() {
        TreeMap<Integer, RandomDistribution> distributionPerPeriod = new TreeMap<Integer, RandomDistribution>();
        int alphaColumnIndex = alphaAndLambda.getColumnIndex(DistributionParams.ALPHA.toString());
        int lambaColumnIndex = alphaAndLambda.getColumnIndex(DistributionParams.LAMBDA.toString());

        for (int row = alphaAndLambda.getTitleRowCount(); row < alphaAndLambda.getRowCount(); row++) {
            int period = InputFormatConverter.getInt((alphaAndLambda.getValueAt(row, periodColumnIndex))) - 1;
            double alphaParam = InputFormatConverter.getDouble(alphaAndLambda.getValueAt(row, alphaColumnIndex));
            double lambdaParam = InputFormatConverter.getDouble(alphaAndLambda.getValueAt(row, lambaColumnIndex));
            distributionPerPeriod.put(period, DistributionType.getStrategy(
                    DistributionType.GAMMA, ArrayUtils.toMap(
                    new Object[][]{{DistributionParams.ALPHA, alphaParam}, {DistributionParams.LAMBDA, lambdaParam}})));
        }
        return distributionPerPeriod;
    }
}
