package org.pillarone.riskanalytics.domain.utils.datetime

import org.joda.time.DateTime

/**
 * @author simon.parten (at) art-allianz (dot) com
 */
class DateTimeUtilitiesTests extends GroovyTestCase {

    static final EPSILON = 1E-5

    void testDays360OneYear(){
        DateTime testDate1 = new DateTime(2015, 1, 1, 0, 0, 0, 0);
        DateTime testDate2 = new DateTime(2013, 1, 1, 0, 0, 0, 0);

        double testMeEU = DateTimeUtilities.Days360.EU.days360(testDate2, testDate1 )
        double testMeUS = DateTimeUtilities.Days360.US.days360(testDate2, testDate1 )

        assertEquals "Check two years" , testMeEU , 360 * 2, EPSILON
        assertEquals "Check two years" , testMeUS , 360 * 2, EPSILON
    }
    void testDays360OneDay(){
        DateTime testDate1 = new DateTime(2013, 1, 2, 0, 0, 0, 0);
        DateTime testDate2 = new DateTime(2013, 1, 1, 0, 0, 0, 0);

        double testMeEU = DateTimeUtilities.Days360.EU.days360(testDate2, testDate1 )
        double testMeUS = DateTimeUtilities.Days360.US.days360(testDate2, testDate1 )

        assertEquals "Check" , testMeEU , 1, EPSILON
        assertEquals "Check" , testMeUS , 1, EPSILON
    }

    void testDays360TwoMonths(){
        DateTime testDate1 = new DateTime(2013, 3, 1, 0, 0, 0, 0);
        DateTime testDate2 = new DateTime(2013, 1, 1, 0, 0, 0, 0);

        double testMeEU = DateTimeUtilities.Days360.EU.days360(testDate2, testDate1 )
        double testMeUS = DateTimeUtilities.Days360.US.days360(testDate2, testDate1 )

        assertEquals "Check two years" , testMeEU , 60, EPSILON
        assertEquals "Check two years" , testMeUS , 60, EPSILON
    }

    void testDays360MinusTwoMonths(){
        DateTime testDate1 = new DateTime(2013, 3, 1, 0, 0, 0, 0);
        DateTime testDate2 = new DateTime(2013, 1, 1, 0, 0, 0, 0);

        double testMeEU = DateTimeUtilities.Days360.EU.days360(testDate1, testDate2 )
        double testMeUS = DateTimeUtilities.Days360.US.days360(testDate1, testDate2 )

        assertEquals "Check two years" , testMeEU , -60, EPSILON
        assertEquals "Check two years" , testMeUS , -60, EPSILON
    }

    void testDays360LeapYearDays(){
        DateTime testDate1 = new DateTime(2012, 2, 28, 0, 0, 0, 0);
        DateTime testDate2 = new DateTime(2012, 3, 1, 0, 0, 0, 0);

        double testMeEU = DateTimeUtilities.Days360.EU.days360 (testDate1, testDate2 )
        double testMeUS = DateTimeUtilities.Days360.US.days360 (testDate1, testDate2 )

        assertEquals "Check two years" , testMeEU , 3, EPSILON
        assertEquals "Check two years" , testMeUS , 3, EPSILON
    }

    void testDays360LeapYearDay(){
        DateTime testDate1 = new DateTime(2012, 2, 29, 0, 0, 0, 0);
        DateTime testDate2 = new DateTime(2012, 3, 1, 0, 0, 0, 0);

        double testMeEU = DateTimeUtilities.Days360.EU.days360(testDate1, testDate2 )
        double testMeUS = DateTimeUtilities.Days360.US.days360(testDate1, testDate2 )

        assertEquals "Check two years" , testMeEU , 2, EPSILON
        assertEquals "Check two years" , testMeUS , 1, EPSILON
    }

    void testDays360TestEndDateLastDayOfMonth(){
        final DateTime dateTime1 = new DateTime(2011, 6, 30, 0, 0, 0, 0 )
        final DateTime dateTime2 = new DateTime(2011, 12, 31, 0, 0, 0, 0 )

        double testMeEU = DateTimeUtilities.Days360.EU.days360(dateTime1, dateTime2);
        double testMeUS = DateTimeUtilities.Days360.EU.days360(dateTime1, dateTime2);

        assertEquals("", 180d , testMeEU)
        assertEquals("", 180d , testMeUS)
    }

    void testDays360Months(){
        final DateTime dateTime1 = new DateTime(2011, 6, 30, 0, 0, 0, 0 )
        final DateTime dateTime2 = new DateTime(2011, 12, 31, 0, 0, 0, 0 )

        double testMe = DateTimeUtilities.months360 (dateTime1, dateTime2);

        assertEquals("", 6d , testMe)
    }



    void testTestDays360Proprtion(){
        DateTime testDate1 = new DateTime(2015, 1, 1, 0, 0, 0, 0);
        DateTime testDate2 = new DateTime(2013, 1, 1, 0, 0, 0, 0);

        DateTime halfPeriod = new DateTime(2014, 1, 1, 0, 0, 0, 0);
        double testMe = DateTimeUtilities.days360ProportionOfPeriod(testDate2, testDate1, halfPeriod, DateTimeUtilities.Days360.EU)
        assertEquals "Check half period" , testMe , 0.5d, EPSILON

        DateTime sameAsStart = new DateTime(2013, 1, 1, 0, 0, 0, 0);
        double shouldBeZero = DateTimeUtilities.days360ProportionOfPeriod(testDate2, testDate1, sameAsStart, DateTimeUtilities.Days360.EU)
        assertEquals "shouldBeZero" , shouldBeZero , 0d

        DateTime sameAsEnd = new DateTime(2015, 1, 1, 0, 0, 0, 0);
        double shouldBe1 = DateTimeUtilities.days360ProportionOfPeriod(testDate2, testDate1, sameAsEnd, DateTimeUtilities.Days360.EU)
        assertEquals "shouldBe1" , shouldBe1 , 1d

//        Check the runtimeChecks!
        shouldFail {
            DateTime beforeStart = new DateTime(2012, 1, 1, 0, 0, 0, 0);
            double shouldFail = DateTimeUtilities.days360ProportionOfPeriod(testDate2, testDate1, beforeStart, DateTimeUtilities.Days360.EU)
        }

        shouldFail {
            DateTime afterEnd = new DateTime(2016, 1, 1, 0, 0, 0, 0);
            double shouldFail = DateTimeUtilities.days360ProportionOfPeriod(testDate2, testDate1, afterEnd, DateTimeUtilities.Days360.EU)
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
