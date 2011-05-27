package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.parameterization.AbstractParameterObjectClassifier
import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier
import org.pillarone.riskanalytics.core.parameterization.IParameterObject
import org.pillarone.riskanalytics.core.parameterization.ComboBoxMatrixMultiDimensionalParameter
import org.pillarone.riskanalytics.core.parameterization.AbstractMultiDimensionalParameter
import org.pillarone.riskanalytics.core.parameterization.ComboBoxTableMultiDimensionalParameter

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
class CopulaType extends AbstractParameterObjectClassifier {


    public static final CopulaType NORMAL = new CopulaType("normal", "NORMAL", ["dependencyMatrix": new ComboBoxMatrixMultiDimensionalParameter([[1d, 0d], [0d, 1d]], ["A", "B"], ICorrelationMarker)])
    public static final CopulaType INDEPENDENT = new CopulaType("independent", "INDEPENDENT", ["targets": new ComboBoxTableMultiDimensionalParameter(["A"], ['Targets'], ICorrelationMarker)])
    public static final CopulaType FRECHETUPPERBOUND = new CopulaType("frechet upper bound", "FRECHETUPPERBOUND", ["targets": new ComboBoxTableMultiDimensionalParameter(["A"], ['Targets'], ICorrelationMarker)])
    public static final CopulaType GUMBEL = new CopulaType("gumbel", "GUMBEL", ["lambda": 10, "dimension": 2, "targets": new ComboBoxTableMultiDimensionalParameter(["A"], ['Targets'], ICorrelationMarker)])
    public static final CopulaType T = new CopulaType("t", "T", ["dependencyMatrix": new ComboBoxMatrixMultiDimensionalParameter([[1d, 0d], [0d, 1d]], ["A", "B"], ICorrelationMarker), "degreesOfFreedom": 10])

    public static final all = [NORMAL, INDEPENDENT, FRECHETUPPERBOUND, GUMBEL]

    protected static Map types = [:]
    static {
        CopulaType.all.each {
            CopulaType.types[it.toString()] = it
        }
    }

    private CopulaType(String typeName, Map parameters) {
        super(typeName, parameters)
    }

    private CopulaType(String displayName, String typeName, Map parameters) {
        super(displayName, typeName, parameters)
    }

    public List<IParameterObjectClassifier> getClassifiers() {
        return all
    }

    public IParameterObject getParameterObject(Map parameters) {
        return CopulaType.getStrategy(this, parameters)
    }

    static ICopulaStrategy getDefault() {
        return new IndependentCopulaStrategy(
                targets: new ComboBoxTableMultiDimensionalParameter(["A"], ['targets'], ICorrelationMarker));
    }


    public static CopulaType valueOf(String type) {
        types[type]
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

    static ICopulaStrategy getStrategy(CopulaType type, Map parameters) {
        ICopulaStrategy copula
        switch (type) {
            case CopulaType.NORMAL:
                copula = new NormalCopulaStrategy(dependencyMatrix: (ComboBoxMatrixMultiDimensionalParameter) parameters["dependencyMatrix"])
                break
            case CopulaType.INDEPENDENT:
                copula = new IndependentCopulaStrategy(targets: (ComboBoxTableMultiDimensionalParameter) parameters["targets"])
                break
            case CopulaType.FRECHETUPPERBOUND:
                copula = new FrechetUpperBoundCopulaStrategy(targets: (ComboBoxTableMultiDimensionalParameter) parameters["targets"])
                break
            case CopulaType.T:
                copula = new TCopulaStrategy(dependencyMatrix: (ComboBoxMatrixMultiDimensionalParameter) parameters["dependencyMatrix"],
                        degreesOfFreedom: (int) parameters["degreesOfFreedom"])
                break
            case CopulaType.GUMBEL:
                copula = new GumbelCopulaStrategy(lambda: (double) parameters["lambda"], dimension: (double) parameters["dimension"],
                        targets: (ComboBoxTableMultiDimensionalParameter) parameters["targets"])
                break
        }
        return copula
    }
}
