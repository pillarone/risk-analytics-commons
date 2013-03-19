package org.pillarone.riskanalytics.domain.utils.math.dependance

/**
*   author simon.parten @ art-allianz . com
 */
class MarginalAndEventTests extends GroovyTestCase {

    void testEquals(){
        MarginalAndEvent event = new MarginalAndEvent(0.5, new GeneratorPeriod("LOB1", 1))
        MarginalAndEvent event2 = new MarginalAndEvent(0.5, new GeneratorPeriod("LOB1", 1))
        assert event.equals(event2)
    }

    void testCreation(){
        MarginalAndEvent event = new MarginalAndEvent(new GeneratorPeriod("LOB", 1), 0.5, null)
        shouldFail {
            MarginalAndEvent event1 = new MarginalAndEvent(new GeneratorPeriod("LOB", 1), -0.01, null)
        }

        shouldFail {
            MarginalAndEvent event2 = new MarginalAndEvent(new GeneratorPeriod("LOB", 1), 1.001, null)
        }
    }
}
