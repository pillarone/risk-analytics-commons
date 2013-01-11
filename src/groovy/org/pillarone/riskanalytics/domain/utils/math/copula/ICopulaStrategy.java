package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.components.Component;
import org.pillarone.riskanalytics.core.parameterization.IParameterObject;
import org.pillarone.riskanalytics.domain.utils.math.dependance.DependancePacket;

import java.util.List;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public interface ICopulaStrategy extends IParameterObject {

    @Deprecated
    List<Number> getRandomVector();
    @Deprecated
    List<String> getTargetNames();
    @Deprecated
    List<Component> getTargetComponents();

    DependancePacket getDependance(Integer modelPeriod);
}
