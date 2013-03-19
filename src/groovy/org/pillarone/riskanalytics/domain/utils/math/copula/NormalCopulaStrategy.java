package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;

/**
 * @author ali.majidi (at) munichre (dot) com, stefan.kunz (at) intuitive-collaboration (dot) com, jessika.walter (at) intuitive-collaboration (dot) com
 */
class NormalCopulaStrategy extends AbstractNormalCopulaStrategy {

    static final CopulaType type = CopulaType.NORMAL;

    public IParameterObjectClassifier getType() {
        return type;
    }

}
