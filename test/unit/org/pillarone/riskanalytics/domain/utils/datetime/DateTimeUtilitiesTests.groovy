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

    void testDays360TestEndDateLastDayOfMonth(){
        final DateTime dateTime1 = new DateTime(2011, 6, 30, 0, 0, 0, 0 )
        final DateTime dateTime2 = new DateTime(2011, 12, 31, 0, 0, 0, 0 )

        double testMe = DateTimeUtilities.days360(dateTime1, dateTime2);

        assertEquals("", 180d , testMe)
    }

    void testDays360Months(){
        final DateTime dateTime1 = new DateTime(2011, 6, 30, 0, 0, 0, 0 )
        final DateTime dateTime2 = new DateTime(2011, 12, 31, 0, 0, 0, 0 )

        double testMe = DateTimeUtilities.months360(dateTime1, dateTime2);

        assertEquals("", 6d , testMe)
    }



    void testTestDays360Proprtion(){
        private DateTime testDate1 = new DateTime(2015, 1, 1, 0, 0, 0, 0);
        private DateTime testDate2 = new DateTime(2013, 1, 1, 0, 0, 0, 0);

        private DateTime halfPeriod = new DateTime(2014, 1, 1, 0, 0, 0, 0);
        double testMe = DateTimeUtilities.days360ProportionOfPeriod(testDate2, testDate1, halfPeriod)
        assertEquals "Check half period" , testMe , 0.5d, EPSILON

        private DateTime sameAsStart = new DateTime(2013, 1, 1, 0, 0, 0, 0);
        double shouldBeZero = DateTimeUtilities.days360ProportionOfPeriod(testDate2, testDate1, sameAsStart)
        assertEquals "shouldBeZero" , shouldBeZero , 0d

        private DateTime sameAsEnd = new DateTime(2015, 1, 1, 0, 0, 0, 0);
        double shouldBe1 = DateTimeUtilities.days360ProportionOfPeriod(testDate2, testDate1, sameAsEnd)
        assertEquals "shouldBe1" , shouldBe1 , 1d

//        Check the runtimeChecks!
        shouldFail {
            private DateTime beforeStart = new DateTime(2012, 1, 1, 0, 0, 0, 0);
            double shouldFail = DateTimeUtilities.days360ProportionOfPeriod(testDate2, testDate1, beforeStart)
        }

        shouldFail {
            private DateTime afterEnd = new DateTime(2016, 1, 1, 0, 0, 0, 0);
            double shouldFail = DateTimeUtilities.days360ProportionOfPeriod(testDate2, testDate1, afterEnd)
        }
    }

    void testSumDateTimeDoubleMapByDateRange(){
        DateTime date1 =  new DateTime(2012, 2, 1, 0, 0, 0, 0);
        DateTime date2 = new DateTime(2013, 2, 1, 0, 0, 0, 0);
        DateTime date3 = new DateTime(2013, 6, 1, 0, 0, 0, 0);
        DateTime date4 = new DateTime(2016, 1, 1, 0, 0, 0, 0);
        Map<DateTime, Double> testMe = [:]

        testMe.put(date1, 10d)
        testMe.put(date2, 10d)
        testMe.put(date3, 10d)
        testMe.put(date4, 10d)



        DateTime startDate = new DateTime(2013, 1, 1, 0, 0, 0, 0);
        DateTime endDate   = new DateTime(2014, 1, 1, 0, 0, 0, 0);

        double sum = DateTimeUtilities.sumDateTimeDoubleMapByDateRange(testMe, startDate, endDate )

        assertEquals (" first and last date excluded, should be ", 20d, sum, EPSILON)
    }
}
