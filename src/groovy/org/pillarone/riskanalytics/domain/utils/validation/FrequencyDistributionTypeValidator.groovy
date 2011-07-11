package org.pillarone.riskanalytics.domain.utils.validation

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier
import org.pillarone.riskanalytics.core.parameterization.validation.AbstractParameterValidationService
import org.pillarone.riskanalytics.core.parameterization.validation.IParameterizationValidator
import org.pillarone.riskanalytics.core.parameterization.validation.ParameterValidation
import org.pillarone.riskanalytics.core.parameterization.validation.ValidationType
import org.pillarone.riskanalytics.core.simulation.item.parameter.ParameterHolder
import org.pillarone.riskanalytics.core.simulation.item.parameter.ParameterObjectParameterHolder
import org.pillarone.riskanalytics.domain.utils.InputFormatConverter
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionType
import org.pillarone.riskanalytics.domain.utils.math.distribution.FrequencyDistributionType

class FrequencyDistributionTypeValidator implements IParameterizationValidator {

    private static Log LOG = LogFactory.getLog(FrequencyDistributionTypeValidator)
    private static final double EPSILON = 1E-6 // guard for "close-enough" checks instead of == for doubles

    private AbstractParameterValidationService validationService

    public FrequencyDistributionTypeValidator() {
        validationService = new ParameterValidationServiceImpl()
        registerConstraints()
    }

    List<ParameterValidation> validate(List<ParameterHolder> parameters) {

        List<ParameterValidation> errors = []

        for (ParameterHolder parameter in parameters) {
            if (parameter instanceof ParameterObjectParameterHolder) {
                IParameterObjectClassifier classifier = parameter.getClassifier()
                if (classifier instanceof FrequencyDistributionType) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug "validating ${parameter.path}"
                    }
                    try {
                        def currentErrors = validationService.validate(classifier, parameter.getParameterMap())
                        currentErrors*.path = parameter.path
                        errors.addAll(currentErrors)
                    }
                    catch (IllegalArgumentException ex) {
                        //https://issuetracking.intuitive-collaboration.com/jira/browse/PMO-1619
                        LOG.debug("call parameter.getBusinessObject() failed " + ex.toString())
                    }
                }
                errors.addAll(validate(parameter.classifierParameters.values().toList()))
            }
        }

        return errors
    }

    // note: same property file as for DistributionTypeValidator
    private void registerConstraints() {
        validationService.register(FrequencyDistributionType.POISSON) {Map type ->
            if (type.lambda >= 0) return true
            [ValidationType.ERROR, "distribution.type.error.poisson.lambda.negative", type.lambda]
        }

        validationService.register(FrequencyDistributionType.NEGATIVEBINOMIAL) {Map type ->
            if (type.gamma > 0) return true
            [ValidationType.ERROR, "distribution.tpye.error.negativebinomial.gamma.nonpositive", type.gamma]
        }
        validationService.register(FrequencyDistributionType.NEGATIVEBINOMIAL) {Map type ->
            if ((0.0..1.0).containsWithinBounds(type.p)) return true
            [ValidationType.ERROR, "distribution.tpye.error.negativebinomial.p.out.of.range", type.p]
        }

        validationService.register(FrequencyDistributionType.DISCRETEEMPIRICAL) {Map type ->
            double[] values = new double[type.discreteEmpiricalValues.getRowCount() - 1];
            int index = type.discreteEmpiricalValues.getColumnIndex('observations')
            for (int i = 1; i < type.discreteEmpiricalValues.getRowCount(); i++) {
                values[i - 1] = InputFormatConverter.getDouble(type.discreteEmpiricalValues.getValueAt(i, index))
            }
            if (!values) {
                return [ValidationType.ERROR, "distribution.type.error.discreteempirical.observations.empty"]
            }
            for (int i = 1; i < values.length; i++) {
                if (values[i - 1] >= values[i]) {
                    return [ValidationType.ERROR, "distribution.type.error.discreteempirical.observations.not.strictly.increasing", i, values[i - 1], values[i]]
                }
            }
            return true
        }

        validationService.register(FrequencyDistributionType.DISCRETEEMPIRICAL) {Map type ->
            double[] values = new double[type.discreteEmpiricalValues.getRowCount() - 1];
            int index = type.discreteEmpiricalValues.getColumnIndex('observations')
            for (int i = 1; i < type.discreteEmpiricalValues.getRowCount(); i++) {
                values[i - 1] = InputFormatConverter.getDouble(type.discreteEmpiricalValues.getValueAt(i, index))
            }
            for (int i = 0; i < values.length; i++) {
                if (values[i] < 0) {
                    return [ValidationType.ERROR, "distribution.type.error.discreteempirical.observations.negative", i + 1, values[i]]
                }
            }
            return true
        }

        validationService.register(FrequencyDistributionType.DISCRETEEMPIRICAL) {Map type ->
            double[] values = new double[type.discreteEmpiricalValues.getRowCount() - 1];
            int index = type.discreteEmpiricalValues.getColumnIndex('probabilities')
            for (int i = 1; i < type.discreteEmpiricalValues.getRowCount(); i++) {
                values[i - 1] = InputFormatConverter.getDouble(type.discreteEmpiricalValues.getValueAt(i, index))
            }
            if (!values) {
                return [ValidationType.ERROR, "distribution.type.error.discreteempirical.probabilities.empty"]
            }
            double sum = (Double) values.inject(0) {temp, it -> temp + it }
            if (isCloseEnough(sum, 0d)) {
                return [ValidationType.ERROR, "distribution.type.error.discreteempirical.probabilities.sum.zero", sum]
            }
            if (isCloseEnough(sum, 1d)) return true
            [ValidationType.WARNING, "distribution.type.error.discreteempirical.probabilities.sum.not.one", sum, values]
        }

        validationService.register(FrequencyDistributionType.DISCRETEEMPIRICAL) {Map type ->
            double[] values = new double[type.discreteEmpiricalValues.getRowCount() - 1];
            int index = type.discreteEmpiricalValues.getColumnIndex('probabilities')
            for (int i = 1; i < type.discreteEmpiricalValues.getRowCount(); i++) {
                values[i - 1] = InputFormatConverter.getDouble(type.discreteEmpiricalValues.getValueAt(i, index))
            }
            for (int i = 0; i < values.size(); i++) {
                if (values[i] < 0) {
                    return [ValidationType.ERROR, "distribution.type.error.discreteempirical.probabilities.negative", i + 1, values[i]]
                }
            }
            return true
        }

        validationService.register(FrequencyDistributionType.DISCRETEEMPIRICALCUMULATIVE) {Map type ->
            double[] values = new double[type.discreteEmpiricalCumulativeValues.getRowCount() - 1];
            int index = type.discreteEmpiricalCumulativeValues.getColumnIndex('observations')
            for (int i = 1; i < type.discreteEmpiricalCumulativeValues.getRowCount(); i++) {
                values[i - 1] = InputFormatConverter.getDouble(type.discreteEmpiricalCumulativeValues.getValueAt(i, index))
            }
            if (!values) {
                return [ValidationType.ERROR, "distribution.type.error.discreteempirical.cumulative.observations.empty"]
            }
            for (int i = 1; i < values.length; i++) {
                if (values[i - 1] >= values[i]) {
                    return [ValidationType.ERROR, "distribution.type.error.discreteempirical.cumulative.observations.not.strictly.increasing", i, values[i - 1], values[i]]
                }
            }
            return true
        }
        validationService.register(FrequencyDistributionType.DISCRETEEMPIRICALCUMULATIVE) {Map type ->
            double[] values = new double[type.discreteEmpiricalCumulativeValues.getRowCount() - 1];
            int index = type.discreteEmpiricalCumulativeValues.getColumnIndex('cumulative probabilities')
            for (int i = 1; i < type.discreteEmpiricalCumulativeValues.getRowCount(); i++) {
                values[i - 1] = InputFormatConverter.getDouble(type.discreteEmpiricalCumulativeValues.getValueAt(i, index))
            }
            if (!values) {
                return [ValidationType.ERROR, "distribution.type.error.discreteempirical.cumulative.probabilities.empty"]
            }

            if (values[0] < 0) {
                return [ValidationType.ERROR, "distribution.type.error.discreteempirical.cumulative.probabilities.negative", values[0]]
            }

            for (int i = 1; i < values.length; i++) {
                if (values[i - 1] > values[i]) {
                    return [ValidationType.ERROR, "distribution.type.error.discreteempirical.cumulative.probabilities.nonincreasing", i, values[i - 1], values[i]]
                }
            }
            if (values[-1] == 0) {
                return [ValidationType.ERROR, "distribution.type.error.discreteempirical.cumulative.probability.last.value.zero", values[values.length - 1]]
            }
            if (!isCloseEnough(values[-1], 1d)) {
                return [ValidationType.WARNING, "distribution.type.error.discreteempirical.cumulative.probability.last.value.not.1", values[values.length - 1]]
            }
            return true
        }

        validationService.register(FrequencyDistributionType.DISCRETEEMPIRICALCUMULATIVE) {Map type ->
            double[] values = new double[type.discreteEmpiricalCumulativeValues.getRowCount() - 1];
            int index = type.discreteEmpiricalCumulativeValues.getColumnIndex('observations')
            for (int i = 1; i < type.discreteEmpiricalCumulativeValues.getRowCount(); i++) {
                values[i - 1] = InputFormatConverter.getDouble(type.discreteEmpiricalCumulativeValues.getValueAt(i, index))
            }
            for (int i = 0; i < values.length; i++) {
                if (values[i] < 0) {
                    return [ValidationType.ERROR, "distribution.type.error.discreteempiricalcumulative.observations.negative", i + 1, values[i]]
                }
            }
            return true
        }

        validationService.register(FrequencyDistributionType.BINOMIALDIST) {Map type ->
            if ((0.0..1.0).containsWithinBounds(type.p)) return true
            [ValidationType.ERROR, "distribution.tpye.error.binomial.p.out.of.range", type.p]
        }
        validationService.register(FrequencyDistributionType.BINOMIALDIST) {Map type ->
            if (type.n > 0) return true
            [ValidationType.ERROR, "distribution.tpye.error.binomial.n.nonpositive", type.n]
        }

        validationService.register(FrequencyDistributionType.CONSTANTS) {Map type ->
            double[] values = new double[type.constants.getRowCount() - 1];
            int index = type.constants.getColumnIndex('constants')
            for (int i = 1; i < type.constants.getRowCount(); i++) {
                values[i - 1] = InputFormatConverter.getDouble(type.constants.getValueAt(i, index))
            }
            if (values && values.size() <= 0) {
                return [ValidationType.ERROR, "distribution.type.error.constants.empty", values]
            }

            for (int i = 0; i < values.length; i++) {
                if (values[i] < 0) {
                    return [ValidationType.ERROR, "distribution.type.error.constants.values.negative", i + 1, values[i]]
                }
            }
            return true

        }

        validationService.register(FrequencyDistributionType.CONSTANT) {Map type ->
            if (type.constant >= 0) return true
            [ValidationType.ERROR, "distribution.type.error.constant.value.negative", type.constant]
        }
    }

    private boolean isCloseEnough(double candidate, double compareAgainst) {
        (candidate - compareAgainst).abs() < EPSILON
    }
}