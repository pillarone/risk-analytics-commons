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
public class LogNormalMuSigmaVaryingParametersDistributionStrategy extends AbstractVaryingParameterDistributionStrategy {

    private ConstrainedMultiDimensionalParameter muAndSigma = new ConstrainedMultiDimensionalParameter(
            GroovyUtils.convertToListOfList(new Object[]{1, 1d, 1d}), Arrays.asList(PERIOD, DistributionParams.MU, DistributionParams.SIGMA),
            ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER));

    public IParameterObjectClassifier getType() {
        return VaryingParametersDistributionType.LOGNORMAL_MU_SIGMA;
    }

    public Map getParameters() {
        Map<String, ConstrainedMultiDimensionalParameter> parameters = new HashMap<String, ConstrainedMultiDimensionalParameter>(1);
        parameters.put("muAndSigma", muAndSigma);
        return parameters;
    }

    protected TreeMap<Integer, RandomDistribution> initDistributions() {
        TreeMap<Integer, RandomDistribution> distributionPerPeriod = new TreeMap<Integer, RandomDistribution>();
        int muColumnIndex = muAndSigma.getColumnIndex(DistributionParams.MU.toString());
        int sigmaColumnIndex = muAndSigma.getColumnIndex(DistributionParams.SIGMA.toString());

        for (int row = muAndSigma.getTitleRowCount(); row < muAndSigma.getRowCount(); row++) {
            int period = InputFormatConverter.getInt((muAndSigma.getValueAt(row, periodColumnIndex))) - 1;
            double mu = InputFormatConverter.getDouble(muAndSigma.getValueAt(row, muColumnIndex));
            double sigma = InputFormatConverter.getDouble(muAndSigma.getValueAt(row, sigmaColumnIndex));
            distributionPerPeriod.put(period, DistributionType.getStrategy(DistributionType.LOGNORMAL_MU_SIGMA, ArrayUtils.toMap(
                    new Object[][]{{DistributionParams.MEAN, mu}, {DistributionParams.SIGMA, sigma}})));
        }
        return distributionPerPeriod;
    }
}
