package org.pillarone.riskanalytics.domain.utils.validation;

import org.pillarone.riskanalytics.domain.utils.math.distribution.varyingparams.VaryingParametersDistributionType
import org.pillarone.riskanalytics.core.parameterization.validation.ValidationType
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionParams
import org.pillarone.riskanalytics.core.parameterization.validation.AbstractParameterValidationService

import org.pillarone.riskanalytics.core.parameterization.ConstrainedMultiDimensionalParameter
import org.pillarone.riskanalytics.core.RiskAnalyticsInconsistencyException
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionType
import org.pillarone.riskanalytics.domain.utils.math.distribution.FrequencyDistributionType;

/**
 * author simon.parten @ art-allianz . com
 */
public class DistributionConstraintLibrary {

    public static final String POISSON_NEGATIVE = "distribution.type.error.poisson.lambda.negative"
    public static final String PERIOD_ZERO_OR_LESS = "periods.must.be.one.or.more"
    public static final String PERIOD_GREATER_THAN_MAX = "periods.must.be.less.than.max.covered.period"
    public static final String LAST_COVERED_PERIOD = "lastCoveredPeriod"

    private DistributionConstraintLibrary() {
        /* Never instantiate this class*/
    }

    /**
     * Checks that poisson parameters are greater than 0
     * @param service the validation service which will display the contraints
     */
    public static void checkPoissonDistribution(AbstractParameterValidationService service) {
        service.register(VaryingParametersDistributionType.POISSON, checkPoissonGreaterThan0 )
        service.register(DistributionType.POISSON, checkPoissonGreaterThan0)
        service.register(FrequencyDistributionType.POISSON, checkPoissonGreaterThan0 )
    }

    public static void checkPeriodsInCoverPeriod( AbstractParameterValidationService service ) {
        for(VaryingParametersDistributionType aType in VaryingParametersDistributionType) {
            service.register(aType, checkPeriodWithinScope)
        }
    }

    private static Closure checkPoissonGreaterThan0 =
        {Map parameters, Map extraInfo ->
            def lamdaValues = parameters.get(DistributionParams.LAMBDA.toString())
            switch (lamdaValues.class) {

                case Double.class:
                    if (((Double) lamdaValues) >= 0) {return true} else
                        return [ValidationType.ERROR, POISSON_NEGATIVE, lamdaValues]

//                Often we'll have a table of values. Check all are greater than zero.
                case ConstrainedMultiDimensionalParameter.class:
                    List<Double> poissonValues = ((ConstrainedMultiDimensionalParameter) lamdaValues).getColumnByName(DistributionParams.LAMBDA.toString())
                    for (Double poissonValue in poissonValues) {
                        if (poissonValue < 0) {
                            return [ValidationType.ERROR, POISSON_NEGATIVE, poissonValue]
                        }
                    }
                    return true
                default: throw new RiskAnalyticsInconsistencyException("Unkown class recieved by validation of poisson parameter " + lamdaValues.class.toString())
            }
        }

    /**
     *
     */
    private static Closure checkPeriodWithinScope =
        {
            Map parameters, Map extraInfo ->
            if(!extraInfo) return
            int lastCoveredPeriod = extraInfo.get(LAST_COVERED_PERIOD)
            for( Object anObject in parameters.values() ) {
                if(anObject instanceof ConstrainedMultiDimensionalParameter) {
                    List<Integer> allPeriods = anObject.getColumnByName(DistributionParams.PERIOD.toString())
                    if(!allPeriods) return
                    for(Integer aPeriod in allPeriods ) {
                        if(aPeriod < 1 ) {
                            return [ ValidationType.ERROR, PERIOD_ZERO_OR_LESS, aPeriod ]
                        }
                        if(aPeriod > lastCoveredPeriod) {
                            return [ ValidationType.ERROR, PERIOD_GREATER_THAN_MAX, aPeriod, lastCoveredPeriod ]
                        }
                    }
                }
            }

        }

}
