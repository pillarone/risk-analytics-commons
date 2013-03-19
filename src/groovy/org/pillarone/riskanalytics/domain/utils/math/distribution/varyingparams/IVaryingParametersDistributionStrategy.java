package org.pillarone.riskanalytics.domain.utils.math.distribution.varyingparams;

import org.pillarone.riskanalytics.core.parameterization.IParameterObject;
import org.pillarone.riskanalytics.domain.utils.math.distribution.RandomDistribution;

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
public interface IVaryingParametersDistributionStrategy extends IParameterObject {

    RandomDistribution getDistribution(int period);

}
