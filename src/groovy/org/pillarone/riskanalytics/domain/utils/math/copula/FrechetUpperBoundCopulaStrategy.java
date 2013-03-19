package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public class FrechetUpperBoundCopulaStrategy extends AbstractFrechetUpperBoundCopulaStrategy {

    static final CopulaType type = CopulaType.FRECHETUPPERBOUND;

    public IParameterObjectClassifier getType() {
        return type;
    }
}