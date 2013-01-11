package org.pillarone.riskanalytics.domain.utils.math.dependance;

import com.google.common.collect.Maps;
import org.pillarone.riskanalytics.core.packets.Packet;
import org.pillarone.riskanalytics.core.simulation.SimulationException;
import org.pillarone.riskanalytics.core.simulation.engine.PeriodScope;
import org.pillarone.riskanalytics.domain.utils.marker.IPerilMarker;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sparten
 * Date: 11.01.13
 * Time: 10:06
 * To change this template use File | Settings | File Templates.
 */

/**
 * Really we want this data structure to be immutable. Ideally after initialisation the user should call the second constructuor which
 * turns the map into something unmodifiable.
 */
public class DependancePacket extends Packet {

    private final Map<GeneratorPeriod, MarginalAndEvent> marginals;

    public DependancePacket() {
        marginals = new HashMap<GeneratorPeriod, MarginalAndEvent>();
    }

    /**
     * Ideally after initialisation, the
     * because otherwise someone changing that map would mess up the original object.
      * @param marginals
     */
    public DependancePacket (Map<GeneratorPeriod, MarginalAndEvent> marginals ) {
        this.marginals = Collections.unmodifiableMap( marginals );
    }

    public void addMarginal ( String generatorName, Integer period, Double marginalValue ) {
        addMarginal(generatorName, period, null, marginalValue);
    }

    public void addMarginal( String generatorName, Integer period, Event event, Double marginalValue ) {
        GeneratorPeriod generatorPeriod = new GeneratorPeriod(generatorName, period);
        MarginalAndEvent entry = marginals.get(generatorPeriod);
        if(entry != null) {
            throw new SimulationException("Attempted to overwrite the dependancy entry for " + generatorPeriod.toString() + ". This should never happen. Please contact development");
        }
        MarginalAndEvent marginalAndEvent = new MarginalAndEvent(generatorPeriod, marginalValue, event);
        marginals.put(generatorPeriod, marginalAndEvent);
    }

    public MarginalAndEvent getMarginal ( IPerilMarker claimsGenerator, PeriodScope periodScope) {
        return getMarginal(claimsGenerator.getName(), periodScope.getCurrentPeriod());
    }

    public MarginalAndEvent getMarginal (String generatorName, Integer period) {
        GeneratorPeriod generatorPeriod = new GeneratorPeriod(generatorName, period);
        return marginals.get(generatorPeriod);
    }

    /* We don't want people to be able to tamper with this outside of it's initialisation ! */
    public Map<GeneratorPeriod, MarginalAndEvent> getMarginals() {
        return Collections.unmodifiableMap(marginals);
    }

    public DependancePacket immutable() {
        return new DependancePacket(marginals);
    }
}
