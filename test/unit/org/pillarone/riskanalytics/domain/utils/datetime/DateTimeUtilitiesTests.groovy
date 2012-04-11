package org.pillarone.riskanalytics.domain.utils.datetime

import org.joda.time.DateTime

/**
 * @author simon.parten (at) art-allianz (dot) com
 */
class DateTimeUtilitiesTests extends GroovyTestCase {

    static final EPSILON = 1E-5

    void testDays360OneYear(){
        private DateTime testDate1 = new DateTime(2015, 1, 1, 0, 0, 0, 0);
        private DateTime testDate2 = new DateTime(2013, 1, 1, 0, 0, 0, 0);

        double testMe = DateTimeUtilities.days360(testDate2, testDate1 )

        assertEquals "Check two years" , testMe , 360 * 2, EPSILON
    }
    void testDays360OneDay(){
        private DateTime testDate1 = new DateTime(2013, 1, 2, 0, 0, 0, 0);
        private DateTime testDate2 = new DateTime(2013, 1, 1, 0, 0, 0, 0);

        double testMe = DateTimeUtilities.days360(testDate2, testDate1 )

        assertEquals "Check two years" , testMe , 1, EPSILON
    }

    void testDays360TwoMonths(){
        private DateTime testDate1 = new DateTime(2013, 3, 1, 0, 0, 0, 0);
        private DateTime testDate2 = new DateTime(2013, 1, 1, 0, 0, 0, 0);

        double testMe = DateTimeUtilities.days360(testDate2, testDate1 )

        assertEquals "Check two years" , testMe , 60, EPSILON
    }

    void testDays360MinusTwoMonths(){
        private DateTime testDate1 = new DateTime(2013, 3, 1, 0, 0, 0, 0);
        private DateTime testDate2 = new DateTime(2013, 1, 1, 0, 0, 0, 0);

        double testMe = DateTimeUtilities.days360(testDate1, testDate2 )

        assertEquals "Check two years" , testMe , -60, EPSILON
    }

    void testDays360LeapYearDays(){
        private DateTime testDate1 = new DateTime(2012, 2, 28, 0, 0, 0, 0);
        private DateTime testDate2 = new DateTime(2012, 3, 1, 0, 0, 0, 0);

        double testMe = DateTimeUtilities.days360(testDate1, testDate2 )

        assertEquals "Check two years" , testMe , 3, EPSILON
    }

    void testDays360LeapYearDay(){
        private DateTime testDate1 = new DateTime(2012, 2, 29, 0, 0, 0, 0);
        private DateTime testDate2 = new DateTime(2012, 3, 1, 0, 0, 0, 0);

        double testMe = DateTimeUtilities.days360(testDate1, testDate2 )

        assertEquals "Check two years" , testMe , 2, EPSILON
    }
}
