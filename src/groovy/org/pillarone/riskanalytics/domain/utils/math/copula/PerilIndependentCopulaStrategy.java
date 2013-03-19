package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public class PerilIndependentCopulaStrategy extends AbstractIndependentCopulaStrategy {

   static final PerilCopulaType type = PerilCopulaType.INDEPENDENT;

    public IParameterObjectClassifier getType() {
        return type;
    }

}