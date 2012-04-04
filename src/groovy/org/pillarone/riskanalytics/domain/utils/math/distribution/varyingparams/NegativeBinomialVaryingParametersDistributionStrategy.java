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
public class NegativeBinomialVaryingParametersDistributionStrategy extends AbstractVaryingParameterDistributionStrategy {

    private ConstrainedMultiDimensionalParameter gammaAndP = new ConstrainedMultiDimensionalParameter(
            GroovyUtils.convertToListOfList(new Object[]{1, 1d, 1d}),
            Arrays.asList(DistributionParams.PERIOD, DistributionParams.GAMMA, DistributionParams.P),
            ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER));

    public IParameterObjectClassifier getType() {
        return VaryingParametersDistributionType.NEGATIVEBINOMIAL;
    }

    public Map getParameters() {
        Map<String, ConstrainedMultiDimensionalParameter> parameters = new HashMap<String, ConstrainedMultiDimensionalParameter>(1);
        parameters.put("gammaAndP", gammaAndP);
        return parameters;
    }

    protected TreeMap<Integer, RandomDistribution> initDistributions() {
        TreeMap<Integer, RandomDistribution> distributionPerPeriod = new TreeMap<Integer, RandomDistribution>();
        int gammaColumnIndex = gammaAndP.getColumnIndex(DistributionParams.GAMMA.toString());
        int pColumnIndex = gammaAndP.getColumnIndex(DistributionParams.P.toString());

        for (int row = gammaAndP.getTitleRowCount(); row < gammaAndP.getRowCount(); row++) {
            int period = InputFormatConverter.getInt((gammaAndP.getValueAt(row, periodColumnIndex))) - 1;
            double gammaParam = InputFormatConverter.getDouble(gammaAndP.getValueAt(row, gammaColumnIndex));
            double pParam = InputFormatConverter.getDouble(gammaAndP.getValueAt(row, pColumnIndex));
            distributionPerPeriod.put(period, DistributionType.getStrategy(
                    DistributionType.NEGATIVEBINOMIAL, ArrayUtils.toMap(
                        new Object[][]{{DistributionParams.GAMMA, gammaParam}, {DistributionParams.P, pParam}})));
        }
        return distributionPerPeriod;
    }
}
