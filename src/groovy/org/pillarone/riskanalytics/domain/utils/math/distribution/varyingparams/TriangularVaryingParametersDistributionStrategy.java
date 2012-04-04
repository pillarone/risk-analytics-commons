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
public class TriangularVaryingParametersDistributionStrategy extends AbstractVaryingParameterDistributionStrategy {

    private ConstrainedMultiDimensionalParameter supportPoints = new ConstrainedMultiDimensionalParameter(
            GroovyUtils.convertToListOfList(new Object[]{1, 1d, 1d, 0.01d}),
            Arrays.asList(DistributionParams.PERIOD, DistributionParams.A, DistributionParams.B, DistributionParams.M),
            ConstraintsFactory.getConstraints(PeriodDistributionsConstraints.IDENTIFIER));

    public IParameterObjectClassifier getType() {
        return VaryingParametersDistributionType.TRIANGULARDIST;
    }

    public Map getParameters() {
        Map<String, ConstrainedMultiDimensionalParameter> parameters = new HashMap<String, ConstrainedMultiDimensionalParameter>(1);
        parameters.put("supportPoints", supportPoints);
        return parameters;
    }

    protected TreeMap<Integer, RandomDistribution> initDistributions() {
        TreeMap<Integer, RandomDistribution> distributionPerPeriod = new TreeMap<Integer, RandomDistribution>();
        int aColumnIndex = supportPoints.getColumnIndex(DistributionParams.A.toString());
        int bColumnIndex = supportPoints.getColumnIndex(DistributionParams.B.toString());
        int mColumnIndex = supportPoints.getColumnIndex(DistributionParams.M.toString());

        for (int row = supportPoints.getTitleRowCount(); row < supportPoints.getRowCount(); row++) {
            int period = InputFormatConverter.getInt((supportPoints.getValueAt(row, periodColumnIndex))) - 1;
            double aParam = InputFormatConverter.getDouble(supportPoints.getValueAt(row, aColumnIndex));
            double bParam = InputFormatConverter.getDouble(supportPoints.getValueAt(row, bColumnIndex));
            double mParam = InputFormatConverter.getDouble(supportPoints.getValueAt(row, mColumnIndex));
            distributionPerPeriod.put(period, DistributionType.getStrategy(
                    DistributionType.TRIANGULARDIST, ArrayUtils.toMap(
                        new Object[][]{{DistributionParams.A, aParam},
                                {DistributionParams.B, bParam},
                                {DistributionParams.M, mParam}})));
        }
        return distributionPerPeriod;
    }
}
