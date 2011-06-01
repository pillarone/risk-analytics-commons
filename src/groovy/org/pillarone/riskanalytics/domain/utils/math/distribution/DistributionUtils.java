package org.pillarone.riskanalytics.domain.utils.math.distribution;

import org.pillarone.riskanalytics.core.parameterization.ConstrainedMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.ConstraintsFactory;
import org.pillarone.riskanalytics.core.parameterization.TableMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.util.GroovyUtils;
import org.pillarone.riskanalytics.domain.utils.InputFormatConverter;
import org.pillarone.riskanalytics.domain.utils.constraint.DoubleConstraints;

import java.util.*;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public class DistributionUtils {

    public static RandomDistribution getSumOfDistributions(RandomDistribution summand1, RandomDistribution summand2) {
        if (summand1 == null && summand2 == null) return null;
        if (summand1 == null) return summand2;
        if (summand2 == null) return summand1;

        if (!summand1.getType().equals(summand2.getType())) {
            throw new IllegalArgumentException("distributions are not of same type!");
        }
        Map<String, Object> params = getParamsOfSum(summand1.getType(), summand1.getParameters(),
                summand2.getParameters());
        return FrequencyDistributionType.getStrategy(summand1.getType(), params);
    }

    public static RandomDistribution getDifferenceOfDistributions(RandomDistribution minuend, RandomDistribution subtrahend) {
        if (minuend == null) return null;
        if (subtrahend == null || subtrahend.getDistribution() == null) {
            return minuend;
        }
        if (!subtrahend.getType().equals(minuend.getType())) {
            throw new IllegalArgumentException("distributions are not of same type!");
        }
        if (minuend.getDistribution().getMean() < subtrahend.getDistribution().getMean()) {
            throw new IllegalArgumentException("mean of subtrahend is greater than mean of minuend!");
        }
        Map<String, Object> params = getParamsOfDifference(minuend.getType(), minuend.getParameters(),
                subtrahend.getParameters());
        return FrequencyDistributionType.getStrategy(minuend.getType(), params);
    }


    public static RandomDistribution getIdiosyncraticDistribution(RandomDistribution totalDistribution,
                                                                  RandomDistribution systematicDistribution) {
        if (totalDistribution == null) return null;
        if (systematicDistribution == null || systematicDistribution.getDistribution() == null) {
            return totalDistribution;
        }
        if (!systematicDistribution.getType().equals(totalDistribution.getType())) {
            throw new IllegalArgumentException("systematic and total distribution are not of same type!");
        }
        if (totalDistribution.getDistribution().getMean() < systematicDistribution.getDistribution().getMean()) {
            throw new IllegalArgumentException("mean of systematic frequencies is greater than mean of claims numbers!");
        }
        Map<String, Object> params = getParamsOfDifference(totalDistribution.getType(), totalDistribution.getParameters(),
                systematicDistribution.getParameters());
        return FrequencyDistributionType.getStrategy(totalDistribution.getType(), params);
    }

    public static Map<String, Object> getParamsOfSum(DistributionType type, Map paramsSummand1, Map paramsSummand2) {

        Map<String, Object> params = new HashMap<String, Object>();
        double controllParameter = 0;
        if (type.equals(DistributionType.POISSON)) {
            params.put("lambda", (Double) paramsSummand1.get("lambda") + (Double) paramsSummand2.get("lambda"));
        }
        else if (type.equals(DistributionType.BINOMIALDIST)) {
            params.put("p", paramsSummand1.get("p"));
            params.put("n", (Integer) paramsSummand1.get("n") + (Integer) paramsSummand2.get("n"));
            controllParameter = (Double) paramsSummand1.get("p") - (Double) paramsSummand2.get("p");
        }
        else if (type.equals(DistributionType.NEGATIVEBINOMIAL)) {
            params.put("p", paramsSummand1.get("p"));
            params.put("gamma", (Double) paramsSummand1.get("gamma") + (Double) paramsSummand2.get("gamma"));
            controllParameter = (Double) paramsSummand1.get("p") - (Double) paramsSummand2.get("p");
        }
        else if (type.equals(DistributionType.CONSTANT)) {
            params.put("constant", (Double) paramsSummand1.get("constant") + (Double) paramsSummand2.get("constant"));
        }
        else if (type.equals(DistributionType.CONSTANTS)) {
            List<Double> constantsSummand1 = getDoubleList((ConstrainedMultiDimensionalParameter) paramsSummand1.get("constants"), "constants");
            List<Double> constantsSummand2 = getDoubleList((ConstrainedMultiDimensionalParameter) paramsSummand2.get("constants"), "constants");
            List<Double> constantsSum = new ArrayList<Double>();
            for (Double stateSummand1 : constantsSummand1) {
                for (Double stateSummand2 : constantsSummand2) {
                    constantsSum.add(stateSummand1 + stateSummand2);
                }
            }
            params.put("constants", new ConstrainedMultiDimensionalParameter(constantsSum, Arrays.asList("constants"),
                    ConstraintsFactory.getConstraints(DoubleConstraints.IDENTIFIER)));
        }
        else if (type.equals(DistributionType.DISCRETEEMPIRICAL)) {
            params = getParamsDiscreteEmpirical(paramsSummand1, paramsSummand2, true);
        }
        else if (type.equals(DistributionType.DISCRETEEMPIRICALCUMULATIVE)) {
            params = getParamsDiscreteEmpiricalCumulative(paramsSummand1, paramsSummand2, true);
        }

        else {
            throw new IllegalArgumentException("sum of distributions only implemented for discrete distributions");
        }
        if (controllParameter != 0) {
            throw new IllegalArgumentException("parameters for distributions sum are not consistent");
        }
        return params;
    }


    public static Map<String, Object> getParamsOfDifference(DistributionType type, Map paramsMinuend, Map paramsSubtrahend) {

        Map<String, Object> params = new HashMap<String, Object>();
        double controllParameter = 0;
        if (type.equals(DistributionType.POISSON)) {
            params.put("lambda", (Double) paramsMinuend.get("lambda") - (Double) paramsSubtrahend.get("lambda"));
        }
        else if (type.equals(DistributionType.BINOMIALDIST)) {
            params.put("p", paramsMinuend.get("p"));
            params.put("n", (Integer) paramsMinuend.get("n") - (Integer) paramsSubtrahend.get("n"));
            controllParameter = (Double) paramsMinuend.get("p") - (Double) paramsSubtrahend.get("p");
        }
        else if (type.equals(DistributionType.NEGATIVEBINOMIAL)) {
            params.put("p", paramsMinuend.get("p"));
            params.put("gamma", (Double) paramsMinuend.get("gamma") - (Double) paramsSubtrahend.get("gamma"));
            controllParameter = (Double) paramsMinuend.get("p") - (Double) paramsSubtrahend.get("p");
        }
        else if (type.equals(DistributionType.CONSTANT)) {
            params.put("constant", (Double) paramsMinuend.get("constant") - (Double) paramsSubtrahend.get("constant"));
        }
        else if (type.equals(DistributionType.CONSTANTS)) {
            List<Double> constantsMinuend = getDoubleList((ConstrainedMultiDimensionalParameter) paramsMinuend.get("constants"), "constants");
            List<Double> constantsSubtrahend = getDoubleList((ConstrainedMultiDimensionalParameter) paramsSubtrahend.get("constants"), "constants");
            List<Double> constantsDifference = new ArrayList<Double>();
            for (Double stateMinuend : constantsMinuend) {
                for (Double stateSubtrahend : constantsSubtrahend) {
                    constantsDifference.add(stateMinuend - stateSubtrahend);
                }
            }
            params.put("constants", new ConstrainedMultiDimensionalParameter(constantsDifference,
                    Arrays.asList("constants"), ConstraintsFactory.getConstraints(DoubleConstraints.IDENTIFIER)));
        }
        else if (type.equals(DistributionType.DISCRETEEMPIRICAL)) {
            params = getParamsDiscreteEmpirical(paramsMinuend, paramsSubtrahend, false);
        }
        else if (type.equals(DistributionType.DISCRETEEMPIRICALCUMULATIVE)) {
            params = getParamsDiscreteEmpiricalCumulative(paramsMinuend, paramsSubtrahend, false);
        }

        else {
            throw new IllegalArgumentException("sum of distributions only implemented for discrete distributions");
        }
        if (controllParameter != 0) {
            throw new IllegalArgumentException("parameters for distributions sum are not consistent");
        }
        return params;
    }

    public static Map<String, Object> getParamsDiscreteEmpirical(Map paramsSummand1, Map paramsSummand2, Boolean getSumAndNotDifference) {
        Map<String, Object> params = new HashMap<String, Object>();
        List<Double> observationsSummand1 = getDoubleList((ConstrainedMultiDimensionalParameter)
                paramsSummand1.get("discreteEmpiricalValues"), "observations");
        List<Double> probabilitiesSummand1 = getDoubleList((ConstrainedMultiDimensionalParameter)
                paramsSummand1.get("discreteEmpiricalValues"), "probabilities");
        List<Double> observationsSummand2 = getDoubleList((ConstrainedMultiDimensionalParameter)
                paramsSummand2.get("discreteEmpiricalValues"), "observations");
        List<Double> probabilitiesSummand2 = getDoubleList((ConstrainedMultiDimensionalParameter)
                paramsSummand2.get("discreteEmpiricalValues"), "probabilities");
        double normalizationProbSummand1 = 0;
        for (Double probability : probabilitiesSummand1) {
            normalizationProbSummand1 += probability;
        }

        double normalizationProbSummand2 = 0;
        for (Double probability : probabilitiesSummand2) {
            normalizationProbSummand2 += probability;
        }
        List<Double> observations = new ArrayList<Double>();
        List<Double> probabilities = new ArrayList<Double>();
        for (int i = 0; i < observationsSummand1.size(); i++) {
            for (int j = 0; j < observationsSummand2.size(); j++) {
                if (getSumAndNotDifference) {
                    observations.add(observationsSummand1.get(i) + observationsSummand2.get(j));
                }
                else {
                    observations.add(observationsSummand1.get(i) - observationsSummand2.get(j));
                }
                probabilities.add(probabilitiesSummand1.get(i) / normalizationProbSummand1 * probabilitiesSummand2.get(j) / normalizationProbSummand2);
            }
        }
        List<List> observationsAndProbabilities = new ArrayList<List>();
        observationsAndProbabilities.add(observations);
        observationsAndProbabilities.add(probabilities);
        params.put("discreteEmpiricalValues", new ConstrainedMultiDimensionalParameter(GroovyUtils.toList(observationsAndProbabilities),
                Arrays.asList("observations", "probabilities"), ConstraintsFactory.getConstraints(DoubleConstraints.IDENTIFIER)));
        return params;
    }

    public static Map<String, Object> getParamsDiscreteEmpiricalCumulative(Map paramsSummand1, Map paramsSummand2, Boolean getSumAndNotDifference) {
        Map<String, Object> params = new HashMap<String, Object>();
        List<Double> observationsSummand1 = getDoubleList((ConstrainedMultiDimensionalParameter)
                paramsSummand1.get("discreteEmpiricalCumulativeValues"), "observations");
        List<Double> probabilitiesSummand1 = getDoubleList((ConstrainedMultiDimensionalParameter)
                paramsSummand1.get("discreteEmpiricalCumulativeValues"), "cumulative probabilities");
        List<Double> observationsSummand2 = getDoubleList((ConstrainedMultiDimensionalParameter)
                paramsSummand2.get("discreteEmpiricalCumulativeValues"), "observations");
        List<Double> probabilitiesSummand2 = getDoubleList((ConstrainedMultiDimensionalParameter)
                paramsSummand2.get("discreteEmpiricalCumulativeValues"), "cumulative probabilities");
        int size = probabilitiesSummand1.size();
        for (int i = 1; i < size; i++) {
            probabilitiesSummand1.set(size - i, probabilitiesSummand1.get(size - i) - probabilitiesSummand1.get(size - (i + 1)));
        }
        size = probabilitiesSummand2.size();
        for (int i = 1; i < size; i++) {
            probabilitiesSummand2.set(size - i, probabilitiesSummand2.get(size - i) - probabilitiesSummand2.get(size - (i + 1)));
        }
        double normalizationProbSummand1 = 0;
        for (Double probability : probabilitiesSummand1) {
            normalizationProbSummand1 += probability;
        }
        double normalizationProbSummand2 = 0;
        for (Double probability : probabilitiesSummand2) {
            normalizationProbSummand2 += probability;
        }
        TreeMap<Double, Double> probabilitiesPerObservation = new TreeMap<Double, Double>();
        for (int i = 0; i < observationsSummand1.size(); i++) {
            for (int j = 0; j < observationsSummand2.size(); j++) {
                if (getSumAndNotDifference) {
                    probabilitiesPerObservation.put(observationsSummand1.get(i) + observationsSummand2.get(j),
                            probabilitiesSummand1.get(i) / normalizationProbSummand1 * probabilitiesSummand2.get(j) / normalizationProbSummand2);
                }
                else {
                    probabilitiesPerObservation.put(observationsSummand1.get(i) - observationsSummand2.get(j),
                            probabilitiesSummand1.get(i) / normalizationProbSummand1 * probabilitiesSummand2.get(j) / normalizationProbSummand2);
                }
            }
        }
        List<Double> observations = new ArrayList<Double>();
        List<Double> probabilities = new ArrayList<Double>();
        for (SortedMap.Entry<Double, Double> obsAndProb : probabilitiesPerObservation.entrySet()) {
            observations.add(obsAndProb.getKey());
            probabilities.add(obsAndProb.getValue());
        }
        for (int i = 1; i < probabilities.size(); i++) {
            probabilities.set(i, probabilities.get(i) + probabilities.get(i - 1));
        }
        List<List> observationsAndProbabilities = new ArrayList<List>();
        observationsAndProbabilities.add(observations);
        observationsAndProbabilities.add(probabilities);
        params.put("discreteEmpiricalCumulativeValues", new ConstrainedMultiDimensionalParameter(GroovyUtils.toList(observationsAndProbabilities),
                Arrays.asList("observations", "cumulative probabilities"), ConstraintsFactory.getConstraints(DoubleConstraints.IDENTIFIER)));
        return params;
    }

    public static List<Double> getDoubleList(TableMultiDimensionalParameter table, String title) {
        int columnIndex = table.getColumnIndex(title);
        List<Double> list = new ArrayList<Double>();
        for (int i = 1; i <= table.getValueRowCount(); i++) {
            list.add(InputFormatConverter.getDouble(table.getValueAt(i, columnIndex)));
        }
        return list;
    }

}
