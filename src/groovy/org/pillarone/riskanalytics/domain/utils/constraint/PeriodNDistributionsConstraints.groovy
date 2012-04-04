package org.pillarone.riskanalytics.domain.utils.constraint

import org.pillarone.riskanalytics.core.parameterization.IMultiDimensionalConstraints

/**
 * First two columns contain int, possibly following double values.
 *
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
class PeriodNDistributionsConstraints implements IMultiDimensionalConstraints {

    public static final String IDENTIFIER ="Period N Distributions"

    boolean matches(int row, int column, Object value) {
        column < 2 ? value instanceof Integer : value instanceof Number
    }

    String getName() {
        IDENTIFIER
    }

    Class getColumnType(int column) {
        column < 2 ? Integer : Double
    }

    Integer getColumnIndex(Class marker) {
        null
    }
}
