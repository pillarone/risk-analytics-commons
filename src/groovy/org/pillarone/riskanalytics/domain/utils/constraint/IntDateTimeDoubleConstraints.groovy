package org.pillarone.riskanalytics.domain.utils.constraint

import org.pillarone.riskanalytics.core.parameterization.IMultiDimensionalConstraints
import org.joda.time.DateTime

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
class IntDateTimeDoubleConstraints implements IMultiDimensionalConstraints {

    public static final String IDENTIFIER = "INTDATETIMEDOUBLE"

    boolean matches(int row, int column, Object value) {
        if (column == 0) {
            return value instanceof Integer
        }
        else if (column == 1) {
            return value instanceof DateTime
        }
        else if (column == 2) {
            return value instanceof Double
        }
        return false
    }

    String getName() {
        IDENTIFIER
    }

    Class getColumnType(int column) {
        if (column == 0) {
            return Integer
        }
        else if (column == 1) {
            return DateTime
        }
        else if (column == 2) {
            return Double
        }
        throw new IllegalArgumentException("allowed column values [0..2], bus was $column")
    }

    Integer getColumnIndex(Class marker) {
        null
    }

    boolean emptyComponentSelectionAllowed(int column) {
        return false
    }
}
