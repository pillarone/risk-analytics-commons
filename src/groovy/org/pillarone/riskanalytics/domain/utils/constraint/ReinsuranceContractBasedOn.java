package org.pillarone.riskanalytics.domain.utils.constraint;

import org.pillarone.riskanalytics.core.parameterization.IMultiDimensionalConstraints;
import org.pillarone.riskanalytics.domain.utils.constant.ReinsuranceContractBase;
import org.pillarone.riskanalytics.domain.utils.marker.IReinsuranceContractMarker;

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
public class ReinsuranceContractBasedOn implements IMultiDimensionalConstraints {

    public static final String IDENTIFIER = "RI_CONTRACT_BASEDON";

    public static final String CONTRACT = "Covered Contracts";
    public static final String BASED_ON = "Based On";

    public static final int CONTRACT_COLUMN_INDEX = 0;
    public static final int BASED_ON_COLUMN_INDEX = 1;

    public boolean matches(int row, int column, Object value) {
        return value instanceof String;
    }

    public String getName() {
        return IDENTIFIER;
    }

    public Class getColumnType(int column) {
        return column == 0 ? IReinsuranceContractMarker.class : ReinsuranceContractBase.class;
    }

    public Integer getColumnIndex(Class marker) {
        if (IReinsuranceContractMarker.class.isAssignableFrom(marker)) {
            return 0;
        }
        return null;
    }
}
