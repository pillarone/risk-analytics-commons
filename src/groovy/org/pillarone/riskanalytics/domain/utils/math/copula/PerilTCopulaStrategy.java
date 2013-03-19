package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public class PerilTCopulaStrategy extends AbstractTCopulaStrategy {

    static final PerilCopulaType type = PerilCopulaType.T;

    public IParameterObjectClassifier getType() {
        return type;
    }


}