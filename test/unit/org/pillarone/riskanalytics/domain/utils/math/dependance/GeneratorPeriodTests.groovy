package org.pillarone.riskanalytics.domain.utils.math.dependance

/**
*   author simon.parten @ art-allianz . com
 */
class GeneratorPeriodTests extends GroovyTestCase {

    GeneratorPeriod generatorPeriod = new GeneratorPeriod("gen1", 1)
    GeneratorPeriod generatorPeriod2 = new GeneratorPeriod("gen1", 1)


    void testEquality() {
        assert generatorPeriod.equals(generatorPeriod2)
    }

    void testHash(){
        assert generatorPeriod.hashCode().equals(generatorPeriod2.hashCode())
    }
}
