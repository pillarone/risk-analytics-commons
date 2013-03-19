package org.pillarone.riskanalytics.domain.utils.math.copula;


import org.pillarone.riskanalytics.core.parameterization.*
import org.pillarone.riskanalytics.domain.utils.marker.IPerilMarker
import org.pillarone.riskanalytics.core.simulation.InvalidParameterException

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
class PerilCopulaType extends AbstractCopulaType {


    public static final PerilCopulaType NORMAL = new PerilCopulaType("normal", "NORMAL", ["dependencyMatrix": new ComboBoxMatrixMultiDimensionalParameter([[1d, 0d], [0d, 1d]], ["A", "B"], IPerilMarker)])
    public static final PerilCopulaType INDEPENDENT = new PerilCopulaType("independent", "INDEPENDENT", ["targets": new ComboBoxTableMultiDimensionalParameter(["A"], ['Targets'], IPerilMarker)])
    public static final PerilCopulaType FRECHETUPPERBOUND = new PerilCopulaType("frechet upper bound", "FRECHETUPPERBOUND", ["targets": new ComboBoxTableMultiDimensionalParameter(["A"], ['Targets'], IPerilMarker)])
    public static final PerilCopulaType GUMBEL = new PerilCopulaType("gumbel", "GUMBEL", ["lambda": 10, "dimension": 2, "targets": new ComboBoxTableMultiDimensionalParameter(["A"], ['Targets'], IPerilMarker)])
    public static final PerilCopulaType T = new PerilCopulaType("t", "T", ["dependencyMatrix": new ComboBoxMatrixMultiDimensionalParameter([[1d, 0d], [0d, 1d]], ["A", "B"], IPerilMarker), "degreesOfFreedom": 10])

    public static final all = [NORMAL, INDEPENDENT, FRECHETUPPERBOUND, GUMBEL, T]

    protected static Map types = [:]
    static {
        PerilCopulaType.all.each {
            PerilCopulaType.types[it.toString()] = it
        }
    }

    private PerilCopulaType(String typeName, Map parameters) {
        super(typeName, parameters)
    }

    private PerilCopulaType(String displayName, String typeName, Map parameters) {
        super(displayName, typeName, parameters)
    }

    public List<IParameterObjectClassifier> getClassifiers() {
        return all
    }

    public IParameterObject getParameterObject(Map parameters) {
        return PerilCopulaType.getStrategy(this, parameters)
    }

    static ICopulaStrategy getDefault() {
        return new IndependentCopulaStrategy(
                targets: new ComboBoxTableMultiDimensionalParameter(["A"], ['targets'], IPerilMarker));
    }


    public static PerilCopulaType valueOf(String type) {
        types[type]
    }

    static ICopulaStrategy getStrategy(PerilCopulaType type, Map parameters) {
        ICopulaStrategy copula
        switch (type) {
            case PerilCopulaType.NORMAL:
                copula = new PerilNormalCopulaStrategy(dependencyMatrix: (ComboBoxMatrixMultiDimensionalParameter) parameters["dependencyMatrix"])
                break
            case PerilCopulaType.INDEPENDENT:
                copula = new PerilIndependentCopulaStrategy(targets: (ComboBoxTableMultiDimensionalParameter) parameters["targets"])
                break
            case PerilCopulaType.FRECHETUPPERBOUND:
                copula = new PerilFrechetUpperBoundCopulaStrategy(targets: (ComboBoxTableMultiDimensionalParameter) parameters["targets"])
                break
            case PerilCopulaType.T:
                copula = new PerilTCopulaStrategy(dependencyMatrix: (ComboBoxMatrixMultiDimensionalParameter) parameters["dependencyMatrix"],
                        degreesOfFreedom: (int) parameters["degreesOfFreedom"])
                break
            case PerilCopulaType.GUMBEL:
                copula = new PerilGumbelCopulaStrategy(lambda: (double) parameters["lambda"], dimension: (double) parameters["dimension"],
                        targets: (ComboBoxTableMultiDimensionalParameter) parameters["targets"])
                break
            default:
                throw new InvalidParameterException("ICopulaStrategy $type not implemented")
        }
        return copula
    }
}
