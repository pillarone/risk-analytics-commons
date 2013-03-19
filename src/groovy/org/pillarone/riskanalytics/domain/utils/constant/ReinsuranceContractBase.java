package org.pillarone.riskanalytics.domain.utils.constant;

import java.util.Map;

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
public enum ReinsuranceContractBase {
    CEDED, NET;

    public Object getConstructionString(Map parameters) {
        return getClass().getName() + "." + this;
    }
}
