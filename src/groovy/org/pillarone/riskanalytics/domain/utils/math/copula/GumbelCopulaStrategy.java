package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public class GumbelCopulaStrategy extends AbstractGumbelCopulaStrategy {

    static final CopulaType type = CopulaType.GUMBEL;

    public IParameterObjectClassifier getType() {
        return type;
    }
}