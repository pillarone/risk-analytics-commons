package org.pillarone.riskanalytics.domain.utils.math.distribution.varyingparams;

import org.apache.commons.lang.ArrayUtils;
import org.pillarone.riskanalytics.core.parameterization.ConstrainedMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.ConstraintsFactory;
import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;
import org.pillarone.riskanalytics.core.util.GroovyUtils;
import org.pillarone.riskanalytics.domain.utils.InputFormatConverter;
import org.pillarone.riskanalytics.domain.utils.constraint.PeriodNDistributionsConstraints;
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
public class BinomialVaryingParametersDistributionStrategy extends AbstractVaryingParameterDistributionStrategy {

    private ConstrainedMultiDimensionalParameter nAndP = new ConstrainedMultiDimensionalParameter(
            GroovyUtils.convertToListOfList(new Object[]{1, 1, 0d}),
            Arrays.asList(DistributionParams.PERIOD, DistributionParams.N, DistributionParams.P),
            ConstraintsFactory.getConstraints(PeriodNDistributionsConstraints.IDENTIFIER));

    public IParameterObjectClassifier getType() {
        return VaryingParametersDistributionType.BINOMIALDIST;
    }

    public Map getParameters() {
        Map<String, ConstrainedMultiDimensionalParameter> parameters = new HashMap<String, ConstrainedMultiDimensionalParameter>(1);
        parameters.put("nAndP", nAndP);
        return parameters;
    }

    protected TreeMap<Integer, RandomDistribution> initDistributions() {
        TreeMap<Integer, RandomDistribution> distributionPerPeriod = new TreeMap<Integer, RandomDistribution>();
        int nColumnIndex = nAndP.getColumnIndex(DistributionParams.N.toString());
        int pColumnIndex = nAndP.getColumnIndex(DistributionParams.P.toString());

        for (int row = nAndP.getTitleRowCount(); row < nAndP.getRowCount(); row++) {
            int period = InputFormatConverter.getInt((nAndP.getValueAt(row, periodColumnIndex))) - 1;
            double nParam = InputFormatConverter.getDouble(nAndP.getValueAt(row, nColumnIndex));
            double pParam = InputFormatConverter.getDouble(nAndP.getValueAt(row, pColumnIndex));
            distributionPerPeriod.put(period, DistributionType.getStrategy(
                    DistributionType.BINOMIALDIST, ArrayUtils.toMap(
                        new Object[][]{{DistributionParams.N, nParam}, {DistributionParams.P, pParam}})));
        }
        return distributionPerPeriod;
    }
}
