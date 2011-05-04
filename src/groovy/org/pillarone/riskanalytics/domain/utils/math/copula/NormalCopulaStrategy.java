package org.pillarone.riskanalytics.domain.utils.math.copula;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.EigenvalueDecomposition;
import org.pillarone.riskanalytics.core.parameterization.AbstractMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.ComboBoxMatrixMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;
import org.pillarone.riskanalytics.domain.utils.math.randomnumber.DependencyType;
import org.pillarone.riskanalytics.domain.utils.math.randomnumber.IMultiRandomGenerator;
import umontreal.iro.lecuyer.probdist.NormalDist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ali.majidi (at) munichre (dot) com, stefan.kunz (at) intuitive-collaboration (dot) com, jessika.walter (at) intuitive-collaboration (dot) com
 */
class NormalCopulaStrategy extends AbstractCopulaStrategy {

    IMultiRandomGenerator generator;
    ComboBoxMatrixMultiDimensionalParameter dependencyMatrix;

    public List<Number> getRandomVector() {
        checkDependencyMatrix();
        int size = dependencyMatrix.getValueRowCount();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("meanVector", new double[size]);
        params.put("sigmaMatrix", dependencyMatrix.getValues());
        generator = DependencyType.getStrategy(DependencyType.NORMAL, params);
        List<Number> randomVector = generator.nextVector();
        for (int j = 0; j < randomVector.size(); j++) {
            randomVector.set(j, NormalDist.cdf(0, 1d, (Double) randomVector.get(j)));
        }
        return randomVector;
    }

    public List<String> getTargetNames() {
        return dependencyMatrix.getRowNames();
    }

    public Map getParameters() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dependencyMatrix", dependencyMatrix);
        return params;
    }

    static final CopulaType type = CopulaType.NORMAL;

    public IParameterObjectClassifier getType() {
        return type;
    }

    public void checkDependencyMatrix() {
        List<List<Double>> values = dependencyMatrix.getValues();
        for (int i = 0; i < values.size(); i++) {
            if (!(values.get(i).get(i) == 1d)) {
                throw new IllegalArgumentException("NormalCopulaStratey.dependencyMatrixInvalidDiagonal");
            }
        }

        DenseDoubleMatrix2D sigma = new DenseDoubleMatrix2D(values.size(), values.size());
        for (int i = 0; i < values.size(); i++) {
            for (int j = 0; j < values.get(i).size(); j++) {
                sigma.set(i, j, values.get(i).get(j));
            }
        }
        DoubleMatrix2D sigmaTranspose = sigma.viewDice();
        if (!sigmaTranspose.equals(sigma)) {
            throw new IllegalArgumentException("NormalCopulaStratey.dependencyMatrixNonSymmetric");
        }
        EigenvalueDecomposition eigenvalueDecomp = new EigenvalueDecomposition(sigma);
        DoubleMatrix1D eigenvalues = eigenvalueDecomp.getRealEigenvalues();
        eigenvalues.viewSorted();
        if (eigenvalues.get(0) <= 0) {
            throw new IllegalArgumentException("NormalCopulaStratey.dependencyMatrixNonPosDef");
        }
    }
}
