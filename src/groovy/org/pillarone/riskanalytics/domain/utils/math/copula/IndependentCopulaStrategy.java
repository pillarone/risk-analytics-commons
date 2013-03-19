package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public class IndependentCopulaStrategy extends AbstractIndependentCopulaStrategy {

    static final CopulaType type = CopulaType.INDEPENDENT;

    public IParameterObjectClassifier getType() {
        return type;
    }

}