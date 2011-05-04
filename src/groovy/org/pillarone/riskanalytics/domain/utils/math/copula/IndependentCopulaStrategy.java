package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.components.Component;
import org.pillarone.riskanalytics.core.parameterization.AbstractMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.ComboBoxTableMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;
import org.pillarone.riskanalytics.domain.utils.math.generator.IRandomNumberGenerator;
import org.pillarone.riskanalytics.domain.utils.math.generator.RandomNumberGeneratorFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public class IndependentCopulaStrategy extends AbstractCopulaStrategy {

    ComboBoxTableMultiDimensionalParameter targets;

    public List<Number> getRandomVector() {
        IRandomNumberGenerator generator = RandomNumberGeneratorFactory.getUniformGenerator();
        List<Number> randomVector = new ArrayList<Number>();
        for (int i = 0; i < getTargetNames().size(); i++) {
            randomVector.add(generator.nextValue());
        }
        return randomVector;
    }

    static final CopulaType type = CopulaType.INDEPENDENT;

    public IParameterObjectClassifier getType() {
        return type;
    }

    public Map getParameters() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("targets", targets);
        return params;
    }

    public List<String> getTargetNames() {
        return targets.getValues();
    }

    public List<Component> getTargetComponents() {
        return targets.getValuesAsObjects();
    }
}
