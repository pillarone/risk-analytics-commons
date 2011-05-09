package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.parameterization.AbstractMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.ComboBoxTableMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;
import org.pillarone.riskanalytics.core.parameterization.SimpleMultiDimensionalParameter;
import org.pillarone.riskanalytics.domain.utils.math.generator.IRandomNumberGenerator;
import org.pillarone.riskanalytics.domain.utils.math.generator.RandomNumberGeneratorFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public class FrechetUpperBoundCopulaStrategy extends AbstractCopulaStrategy {

    ComboBoxTableMultiDimensionalParameter targets;

    public List<Number> getRandomVector() {
        IRandomNumberGenerator generator = RandomNumberGeneratorFactory.getUniformGenerator();
        double severity = generator.nextValue().doubleValue();
        List<Number> severities = new ArrayList<Number>(getTargetNames().size());
        for (int i=0; i<getTargetNames().size(); i++){
            severities.add(severity);
        }
        return severities;
    }

    public Map getParameters() {
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("targets", targets);
        return params;
    }

    static final CopulaType type = CopulaType.FRECHETUPPERBOUND;

    public IParameterObjectClassifier getType() {
        return type;
    }

    public List<String> getTargetNames() {
        return targets.getValues();
    }
}