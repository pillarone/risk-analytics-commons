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

    public static RandomDistribution getIdiosyncraticDistribution(RandomDistribution totalDistribution,
                                                                  RandomDistribution systematicDistribution) {
        if (systematicDistribution == null || systematicDistribution.getDistribution() == null) {
            return totalDistribution;
        }
        if (!systematicDistribution.getType().equals(totalDistribution.getType())) {
            throw new IllegalArgumentException("systematic and total distribution are not of same type!");
        }
        if (totalDistribution.getDistribution().getMean() < systematicDistribution.getDistribution().getMean()) {
            throw new IllegalArgumentException("mean of systematic frequencies is greater than mean of claims numbers!");
        }
        Map<String, Object> params = getIdiosyncraticParams(totalDistribution.getType(), totalDistribution.getParameters(),
                systematicDistribution.getParameters());
        return FrequencyDistributionType.getStrategy(totalDistribution.getType(), params);
    }

    public static Map<String, Object> getIdiosyncraticParams(DistributionType type, Map paramsTotal, Map paramsSystematic) {

        Map<String, Object> params = new HashMap<String, Object>();
        double controllParameter = 0;
        if (type.equals(FrequencyDistributionType.POISSON)) {
            params.put("lambda", (Double) paramsTotal.get("lambda") - (Double) paramsSystematic.get("lambda"));
        }
        else if (type.equals(FrequencyDistributionType.BINOMIALDIST)) {
            params.put("p", paramsTotal.get("p"));
            params.put("n", (Double) paramsTotal.get("n") - (Double) paramsSystematic.get("n"));
            controllParameter = (Double) paramsTotal.get("p") - (Double) paramsSystematic.get("p");
        }
        else if (type.equals(FrequencyDistributionType.NEGATIVEBINOMIAL)) {
            params.put("p", paramsTotal.get("p"));
            params.put("gamma", (Double) paramsTotal.get("gamma") - (Double) paramsSystematic.get("gamma"));
            controllParameter = (Double) paramsTotal.get("p") - (Double) paramsSystematic.get("p");
        }
        else if (type.equals(FrequencyDistributionType.CONSTANT)) {
            params.put("constant", (Double) paramsTotal.get("constant") - (Double) paramsSystematic.get("constant"));
        }
        else if (type.equals(FrequencyDistributionType.CONSTANTS)) {
            List<Double> constantsTotal = getDoubleList((ConstrainedMultiDimensionalParameter) paramsTotal.get("constants"), "constants");
            List<Double> constantsSystematic = getDoubleList((ConstrainedMultiDimensionalParameter) paramsSystematic.get("constants"), "constants");
            List<Double> constantsIdiosyncratic = new ArrayList<Double>();
            for (Double stateTotal : constantsTotal) {
                for (Double stateSystematic : constantsSystematic) {
                    constantsIdiosyncratic.add(stateTotal - stateSystematic);
                }
            }
            params.put("constants", new ConstrainedMultiDimensionalParameter(GroovyUtils.toList(constantsIdiosyncratic),
                    Arrays.asList("constants"), ConstraintsFactory.getConstraints(DoubleConstraints.IDENTIFIER)));
        }
        else if (type.equals(FrequencyDistributionType.DISCRETEEMPIRICAL)) {
            params = getParamsDiscreteEmpirical(paramsTotal, paramsSystematic);
        }
        else if (type.equals(FrequencyDistributionType.DISCRETEEMPIRICALCUMULATIVE)) {
            params = getParamsDiscreteEmpiricalCumulative(paramsTotal, paramsSystematic);
        }

        if (controllParameter != 0) {
            throw new IllegalArgumentException("event generator and claims generators: parameters for frequency distribution are not consistent");
        }
        return params;
    }

    public static Map<String, Object> getParamsDiscreteEmpirical(Map paramsTotal, Map paramsSystematic) {
        Map<String, Object> params = new HashMap<String, Object>();
        List<Double> observationsTotal = getDoubleList((ConstrainedMultiDimensionalParameter)
                paramsTotal.get("discreteEmpiricalValues"), "observations");
        List<Double> probabilitiesTotal = getDoubleList((ConstrainedMultiDimensionalParameter)
                paramsTotal.get("discreteEmpiricalValues"), "probabilities");
        List<Double> observationsSystematic = getDoubleList((ConstrainedMultiDimensionalParameter)
                paramsSystematic.get("discreteEmpiricalValues"), "observations");
        List<Double> probabilitiesSystematic = getDoubleList((ConstrainedMultiDimensionalParameter)
                paramsSystematic.get("discreteEmpiricalValues"), "probabilities");
        double probTotalSum = 0;
        for (Double probability : probabilitiesTotal) {
            probTotalSum += probability;
        }
        double probSystematicSum = 0;
        for (Double probability : probabilitiesSystematic) {
            probSystematicSum += probability;
        }
        List<Double> observationsIdiosyncratic = new ArrayList<Double>();
        List<Double> probabilitiesIdiosyncratic = new ArrayList<Double>();
        for (int i = 0; i < observationsTotal.size(); i++) {
            for (int j = 0; j < observationsSystematic.size(); j++) {
                observationsIdiosyncratic.add(observationsTotal.get(i) - observationsSystematic.get(j));
                probabilitiesIdiosyncratic.add(probabilitiesTotal.get(i) / probTotalSum * probabilitiesSystematic.get(j) / probSystematicSum);
            }
        }
        List<List> observationsAndProbabilitiesIdiosyncratic = new ArrayList<List>();
        observationsAndProbabilitiesIdiosyncratic.add(observationsIdiosyncratic);
        observationsAndProbabilitiesIdiosyncratic.add(probabilitiesIdiosyncratic);
        params.put("discreteEmpiricalValues", new ConstrainedMultiDimensionalParameter(GroovyUtils.toList(observationsAndProbabilitiesIdiosyncratic),
                Arrays.asList("observations", "probabilities"), ConstraintsFactory.getConstraints(DoubleConstraints.IDENTIFIER)));
        return params;
    }

    public static Map<String, Object> getParamsDiscreteEmpiricalCumulative(Map paramsTotal, Map paramsSystematic) {
        Map<String, Object> params = new HashMap<String, Object>();
        List<Double> observationsTotal = getDoubleList((ConstrainedMultiDimensionalParameter)
                paramsTotal.get("discreteEmpiricalCumulativeValues"), "observations");
        List<Double> probabilitiesTotal = getDoubleList((ConstrainedMultiDimensionalParameter)
                paramsTotal.get("discreteEmpiricalCumulativeValues"), "cumulative probabilities");
        List<Double> observationsSystematic = getDoubleList((ConstrainedMultiDimensionalParameter)
                paramsSystematic.get("discreteEmpiricalCumulativeValues"), "observations");
        List<Double> probabilitiesSystematic = getDoubleList((ConstrainedMultiDimensionalParameter)
                paramsSystematic.get("discreteEmpiricalCumulativeValues"), "cumulative probabilities");
        int size = probabilitiesTotal.size();
        for (int i = 1; i < size; i++) {
            probabilitiesTotal.set(size - i, probabilitiesTotal.get(size - i) - probabilitiesTotal.get(size - (i + 1)));
        }
        size = probabilitiesSystematic.size();
        for (int i = 1; i < size; i++) {
            probabilitiesSystematic.set(size - i, probabilitiesSystematic.get(size - i) - probabilitiesSystematic.get(size - (i + 1)));
        }
        double probTotalSum = 0;
        for (Double probability : probabilitiesTotal) {
            probTotalSum += probability;
        }
        double probSystematicSum = 0;
        for (Double probability : probabilitiesSystematic) {
            probSystematicSum += probability;
        }
        TreeMap<Double, Double> probabilitiesPerObservation = new TreeMap<Double, Double>();
        for (int i = 0; i < observationsTotal.size(); i++) {
            for (int j = 0; j < observationsSystematic.size(); j++) {
                probabilitiesPerObservation.put(observationsTotal.get(i) - observationsSystematic.get(j),
                        probabilitiesTotal.get(i) / probTotalSum * probabilitiesSystematic.get(j) / probSystematicSum);
            }
        }
        List<Double> observationsIdiosyncratic = new ArrayList<Double>();
        List<Double> probabilitiesIdiosyncratic = new ArrayList<Double>();
        for (SortedMap.Entry<Double, Double> obsAndProb : probabilitiesPerObservation.entrySet()) {
            observationsIdiosyncratic.add(obsAndProb.getKey());
            probabilitiesIdiosyncratic.add(obsAndProb.getValue());
        }
        for (int i = 1; i < probabilitiesIdiosyncratic.size(); i++) {
            probabilitiesIdiosyncratic.set(i, probabilitiesIdiosyncratic.get(i) + probabilitiesIdiosyncratic.get(i - 1));
        }
        List<List> observationsAndProbabilitiesIdiosyncratic = new ArrayList<List>();
        observationsAndProbabilitiesIdiosyncratic.add(observationsIdiosyncratic);
        observationsAndProbabilitiesIdiosyncratic.add(probabilitiesIdiosyncratic);
        params.put("discreteEmpiricalCumulativeValues", new ConstrainedMultiDimensionalParameter(GroovyUtils.toList(observationsAndProbabilitiesIdiosyncratic),
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
