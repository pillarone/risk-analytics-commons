package org.pillarone.riskanalytics.domain.utils.math.copula;

import org.pillarone.riskanalytics.core.components.Component;

import java.util.List;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public interface ICopulaStrategy {
    List<Number> getRandomVector();

    List<String> getTargetNames();

   // List<Component> getTargetComponents();
}
