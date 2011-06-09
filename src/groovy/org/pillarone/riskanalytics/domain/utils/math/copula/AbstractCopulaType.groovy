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

    public String getConstructionString(Map parameters) {
        StringBuffer parameterString = new StringBuffer('[')
        parameters.each {k, v ->
            if (v.class.isEnum()) {
                parameterString << "\"$k\":${v.class.name}.$v,"
            }
            else {
                parameterString << "\"$k\":$v,"
            }
        }
        if (parameterString.size() == 1) {
            parameterString << ':'
        }
        parameterString << ']'
        "org.pillarone.riskanalytics.domain.pc.generators.copulas.CopulaStrategyFactory.getCopulaStrategy(${this.class.name}.${typeName.toUpperCase()}, ${parameterString})"
    }

}
