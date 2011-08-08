package org.pillarone.riskanalytics.domain.utils.math.copula;


import org.pillarone.riskanalytics.core.parameterization.*

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
abstract class AbstractCopulaType extends AbstractParameterObjectClassifier {

    protected AbstractCopulaType(String typeName, Map parameters) {
        super(typeName, parameters)
    }

    protected AbstractCopulaType(String displayName, String typeName, Map parameters) {
        super(displayName, typeName, parameters)
    }
}
