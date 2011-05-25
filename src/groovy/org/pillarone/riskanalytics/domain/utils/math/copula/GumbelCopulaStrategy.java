package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.parameterization.AbstractMultiDimensionalParameter;
import org.pillarone.riskanalytics.core.parameterization.IParameterObjectClassifier;
import org.pillarone.riskanalytics.domain.utils.math.distribution.DistributionType;
import org.pillarone.riskanalytics.domain.utils.math.distribution.RandomDistribution;
import org.pillarone.riskanalytics.domain.utils.math.generator.IRandomNumberGenerator;
import org.pillarone.riskanalytics.domain.utils.math.generator.RandomNumberGeneratorFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public class GumbelCopulaStrategy extends AbstractCopulaStrategy {

    RandomDistribution distribution;
    IRandomNumberGenerator uniformGenerator;

    AbstractMultiDimensionalParameter targets;
    double lambda;
    int dimension;

    static final CopulaType type = CopulaType.GUMBEL;

    public IParameterObjectClassifier getType() {
        return type;
    }

    public List<Number> getRandomVector() {
        List<Number> randomVector = new ArrayList<Number>();
        distribution = DistributionType.getUniformDistribution();
        uniformGenerator = RandomNumberGeneratorFactory.getGenerator(distribution);
        double s = (Double) uniformGenerator.nextValue();
        double q = (Double) uniformGenerator.nextValue();
        double t = newtonApproximation(q, lambda, 0.001, 10000);

        for (int j = 0; j < dimension - 1; j++) {
            randomVector.add(Math.exp(-Math.pow(s * Math.pow((-Math.log(t)), lambda), 1 / lambda)));
            if (j == dimension - 2) {
                randomVector.add(Math.exp(-Math.pow((double) (1 - s) * Math.pow((double) (-Math.log(t)), lambda), 1 / lambda)));
            }
            else {
                s = Math.exp(-Math.pow((double) (1 - s) * Math.pow((double) (-Math.log(t)), lambda), 1 / lambda));
            }
        }
        return randomVector;
    }

    public List<String> getTargetNames() {
        return targets.getValues();
    }

    public Map getParameters() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lambda", lambda);
        params.put("dimension", dimension);
        params.put("targets", targets);
        return params;
    }

    double newtonApproximation(double q, double theta, double epsilon, int maxSteps) {
        double xOld = q;
        double xNew = 1 - q;
        int step = 0;
        while (Math.abs(xOld - xNew) > epsilon && step < maxSteps) {
            xOld = xNew;
            xNew = xOld - (xOld - xOld / theta * Math.log(xOld) - q) / (1 - 1 / theta * (Math.log(xOld) + 1));

            ++step;
        }
        return xNew;
    }
}