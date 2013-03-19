package org.pillarone.riskanalytics.domain.utils.constraint

import org.pillarone.riskanalytics.core.parameterization.IMultiDimensionalConstraints
import org.pillarone.riskanalytics.domain.utils.marker.IPerilMarker

/**
 * @author fouad.jaada@intuitive-collaboration.com
 */
class PerilPortion implements IMultiDimensionalConstraints {

    public static final String IDENTIFIER = "PERIL_PORTION"

    boolean matches(int row, int column, Object value) {
        if (column == 0) {
            return value instanceof String
        }
        else {
            return value instanceof Number
        }
    }

    String getName() {
        return IDENTIFIER
    }

    Class getColumnType(int column) {
        return column == 0 ? IPerilMarker : Double
    }

    Integer getColumnIndex(Class marker) {
        if (IPerilMarker.isAssignableFrom(marker)) {
            return 0
        }
        else if (Double.isAssignableFrom(marker)) {
            return 1
        }
        return null;
    }

    boolean emptyComponentSelectionAllowed(int column) {
        return false
    }
}
