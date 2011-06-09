package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.parameterization.ComboBoxMatrixMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionType;
import org.pillarone.riskanalytics.domain.utils.math.generator.IRandomNumberGenerator;
import org.pillarone.riskanalytics.domain.utils.math.generator.RandomNumberGeneratorFactory;
import org.pillarone.riskanalytics.domain.utils.math.randomnumber.DependencyType;
import org.pillarone.riskanalytics.domain.utils.math.randomnumber.IMultiRandomGenerator;
import umontreal.iro.lecuyer.probdist.StudentDist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public abstract class AbstractTCopulaStrategy extends AbstractCopulaStrategy {

    protected IMultiRandomGenerator generator;

    protected ComboBoxMatrixMultiDimensionalParameter dependencyMatrix;
    protected int degreesOfFreedom;
    protected Number chisquareRandomNumber;
    protected IRandomNumberGenerator generatorForChiSquare;

    public List<Number> getRandomVector() {
        checkDependencyMatrix(dependencyMatrix);

        int size = dependencyMatrix.getValueRowCount();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("meanVector", new double[size]);
        params.put("sigmaMatrix", dependencyMatrix.getValues());
        generator = DependencyType.getStrategy(DependencyType.NORMAL, params);
        Map<String, Integer> degrees = new HashMap<String, Integer>();
        degrees.put("n", degreesOfFreedom);
        generatorForChiSquare = RandomNumberGeneratorFactory.getGenerator(DistributionType.getStrategy(DistributionType.CHISQUAREDIST, degrees));

        List<Number> randomVector = generator.nextVector();
        double factor = (double) degreesOfFreedom / generatorForChiSquare.nextValue().doubleValue();
        factor = Math.sqrt(factor);
        for (int i = 0; i < randomVector.size(); ++i) {
            randomVector.set(i, StudentDist.cdf(degreesOfFreedom, (Double) randomVector.get(i) * factor));
        }
        return randomVector;
    }

    public List<String> getTargetNames() {
        return dependencyMatrix.getRowNames();
    }

    //  public List<Component> getTargetComponents() {
    //     return dependencyMatrix.getRowObjects();
    // }

    public Map getParameters() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dependencyMatrix", dependencyMatrix);
        params.put("degreesOfFreedom", degreesOfFreedom);
        return params;
    }
}