package org.pillarone.riskanalytics.domain.utils.constant;

import java.util.Map;

/**
 * @author jessika.walter (at) intuitive-collaboration (dot) com
 */
public enum LogicArguments {
    AND, OR;

    public Object getConstructionString(Map parameters) {
        return getClass().getName() + "." + this;
    }
}
