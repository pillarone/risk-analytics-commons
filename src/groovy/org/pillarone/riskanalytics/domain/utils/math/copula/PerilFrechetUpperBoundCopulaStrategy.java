package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public class PerilFrechetUpperBoundCopulaStrategy extends AbstractFrechetUpperBoundCopulaStrategy {

    static final PerilCopulaType type = PerilCopulaType.FRECHETUPPERBOUND;

    public IParameterObjectClassifier getType() {
        return type;
    }

}