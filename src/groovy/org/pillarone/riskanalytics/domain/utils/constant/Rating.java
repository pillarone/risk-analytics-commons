package org.pillarone.riskanalytics.domain.utils.constant;

import java.util.Map;

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
public enum Rating {
    AAA, AA, A, BBB, BB, B, CCC, CC, C, DEFAULT, NO_DEFAULT;

    public Object getConstructionString(Map parameters) {
        return getClass().getName() + "." + this;
    }
}
