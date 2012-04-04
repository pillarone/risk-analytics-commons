package org.pillarone.riskanalytics.domain.utils.math.distribution.varyingparams;

import org.apache.commons.lang.ArrayUtils;
import org.pillarone.riskanalytics.core.parameterization.ConstrainedMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.ConstraintsFactory;
import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;
import org.pillarone.riskanalytics.core.util.GroovyUtils;
import org.pillarone.riskanalytics.domain.utils.InputFormatConverter;
import org.pillarone.riskanalytics.domain.utils.constraint.PeriodNDistributionsConstraints;
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionParams;
import org.pillarone.riskanalytics.domain.utils.math.distribution.RandomDistribution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
public class ChiSquareVaryingParametersDistributionStrategy extends AbstractVaryingParameterDistributionStrategy {

    private ConstrainedMultiDimensionalParameter n = new ConstrainedMultiDimensionalParameter(
            GroovyUtils.convertToListOfList(new Object[]{1, 1}),
            Arrays.asList(DistributionParams.PERIOD, DistributionParams.N),
            ConstraintsFactory.getConstraints(PeriodNDistributionsConstraints.IDENTIFIER));

    public IParameterObjectClassifier getType() {
        return VaryingParametersDistributionType.CHISQUAREDIST;
    }

    public Map getParameters() {
        Map<String, ConstrainedMultiDimensionalParameter> parameters = new HashMap<String, ConstrainedMultiDimensionalParameter>(1);
        parameters.put("n", n);
        return parameters;
    }

    protected TreeMap<Integer, RandomDistribution> initDistributions() {
        TreeMap<Integer, RandomDistribution> distributionPerPeriod = new TreeMap<Integer, RandomDistribution>();
        int nColumnIndex = n.getColumnIndex(DistributionParams.N.toString());

        for (int row = n.getTitleRowCount(); row < n.getRowCount(); row++) {
            int period = InputFormatConverter.getInt((n.getValueAt(row, periodColumnIndex))) - 1;
            double nParam = InputFormatConverter.getDouble(n.getValueAt(row, nColumnIndex));
            distributionPerPeriod.put(period, (RandomDistribution) VaryingParametersDistributionType.getStrategy(
                    VaryingParametersDistributionType.CHISQUAREDIST, ArrayUtils.toMap(
                        new Object[][]{{DistributionParams.N, nParam}})));
        }
        return distributionPerPeriod;
    }
}
