package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public class PerilGumbelCopulaStrategy extends AbstractGumbelCopulaStrategy {

    static final PerilCopulaType type = PerilCopulaType.GUMBEL;

    public IParameterObjectClassifier getType() {
        return type;
    }
}