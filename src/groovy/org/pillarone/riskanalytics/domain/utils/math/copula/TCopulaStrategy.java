package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public class TCopulaStrategy extends AbstractTCopulaStrategy {

    static final CopulaType type = CopulaType.T;

    public IParameterObjectClassifier getType() {
        return type;
    }
}