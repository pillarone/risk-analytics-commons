package org.pillarone.riskanalytics.domain.utils.math.dependance

/**
*   author simon.parten @ art-allianz . com
 */
class DependancePacketTest extends GroovyTestCase {

    DependancePacket dependancePacket = new DependancePacket()

    void testImmutable(){
        DependancePacket aPacket = new DependancePacket()
        DependancePacket immutable = dependancePacket.immutable()
        shouldFail {
            Map<GeneratorPeriod, MarginalAndEvent> theMap = immutable.getMarginals()
            theMap.put(new GeneratorPeriod("nope", 1), new MarginalAndEvent(new GeneratorPeriod("nope", 1), 0.5, null ))
        }

        shouldFail {
            immutable.addMarginal("nope", 1, 0.5)
        }

    }

    void testAddMarginal(){
        final String addMe = "addMe"
        dependancePacket.addMarginal(addMe, 1, 0.5)
        dependancePacket.addMarginal(addMe, 2, 0.1)
        DependancePacket aPacket = dependancePacket.immutable()

        MarginalAndEvent getFirst = aPacket.getMarginal(addMe, 1)
        assert getFirst.marginalProbability == 0.5

        MarginalAndEvent getSecond = aPacket.getMarginal(addMe, 2)
        assert getSecond.marginalProbability == 0.1
    }

}
