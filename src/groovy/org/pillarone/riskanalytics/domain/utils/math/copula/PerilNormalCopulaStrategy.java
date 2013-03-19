package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;

/**
 * @author ali.majidi (at) munichre (dot) com, stefan.kunz (at) intuitive-collaboration (dot) com, jessika.walter (at) intuitive-collaboration (dot) com
 */
class PerilNormalCopulaStrategy extends AbstractNormalCopulaStrategy {

    static final PerilCopulaType type = PerilCopulaType.NORMAL;

    public IParameterObjectClassifier getType() {
        return type;
    }

}
