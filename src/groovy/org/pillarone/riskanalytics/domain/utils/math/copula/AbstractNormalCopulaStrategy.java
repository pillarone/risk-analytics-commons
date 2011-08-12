package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.components.Component;
import org.pillarone.riskanalytics.core.parameterization.ComboBoxMatrixMultiDimensionalParameter;
import org.pillarone.riskanalytics.domain.utils.math.randomnumber.DependencyType;
import org.pillarone.riskanalytics.domain.utils.math.randomnumber.IMultiRandomGenerator;
import umontreal.iro.lecuyer.probdist.NormalDist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ali.majidi (at) munichre (dot) com, stefan.kunz (at) intuitive-collaboration (dot) com, jessika.walter (at) intuitive-collaboration (dot) com
 */
abstract class AbstractNormalCopulaStrategy extends AbstractCopulaStrategy {

    protected IMultiRandomGenerator generator;
    protected ComboBoxMatrixMultiDimensionalParameter dependencyMatrix;

    public List<Number> getRandomVector() {
        checkDependencyMatrix(dependencyMatrix);
        int size = dependencyMatrix.getValueRowCount();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("meanVector", new double[size]);
        params.put("sigmaMatrix", dependencyMatrix.getValues());
        generator = DependencyType.getStrategy(DependencyType.NORMAL, params);
        List<Number> randomVector = generator.nextVector();
        for (int j = 0; j < randomVector.size(); j++) {
            randomVector.set(j, NormalDist.cdf(0, 1d, (Double) randomVector.get(j)));
        }
        return randomVector;
    }

    public List<String> getTargetNames() {
        return dependencyMatrix.getRowNames();
    }

      public List<Component> getTargetComponents() {
           return dependencyMatrix.getHeadersAsObjects();
       }

    public Map getParameters() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dependencyMatrix", dependencyMatrix);
        return params;
    }
}
