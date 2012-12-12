package org.pillarone.riskanalytics.domain.utils.constraint

import org.pillarone.riskanalytics.core.parameterization.IMultiDimensionalConstraints

/**
 * First column contains an int (the period), following double values.
 *
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
class PeriodDistributionsConstraints implements IMultiDimensionalConstraints {

    public static final String IDENTIFIER ="Period Distributions"

    boolean matches(int row, int column, Object value) {
        column == 0 ? value instanceof Integer : value instanceof Number
    }

    String getName() {
        IDENTIFIER
    }

    Class getColumnType(int column) {
        column == 0 ? Integer : Double
    }

    Integer getColumnIndex(Class marker) {
        null
    }

    boolean emptyComponentSelectionAllowed(int column) {
        return false
    }
}
