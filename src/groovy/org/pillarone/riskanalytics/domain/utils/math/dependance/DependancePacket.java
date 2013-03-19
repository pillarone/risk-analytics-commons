package org.pillarone.riskanalytics.domain.utils.math.dependance;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.pillarone.riskanalytics.core.components.Component;
import org.pillarone.riskanalytics.core.packets.Packet;
import org.pillarone.riskanalytics.core.simulation.SimulationException;
import org.pillarone.riskanalytics.core.simulation.engine.PeriodScope;
import org.pillarone.riskanalytics.domain.utils.marker.IPerilMarker;

import java.util.*;

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
    private final List<Component> dependantGenerators;

    public DependancePacket() {
        this.marginals = Maps.newHashMap();
        this.dependantGenerators = Lists.newArrayList();
    }

    public DependancePacket( List<Component> dependantGenerators ) {
        this.marginals = Maps.newHashMap();
        this.dependantGenerators = Collections.unmodifiableList( dependantGenerators );
    }

    /**
     * Ideally after initialisation, the
     * because otherwise someone changing that map would mess up the original object.
      * @param marginals
     */
    public DependancePacket (Map<GeneratorPeriod, MarginalAndEvent> marginals, List<Component> dependantGenerators ) {
        this.marginals = Collections.unmodifiableMap( marginals );
        this.dependantGenerators = Collections.unmodifiableList(dependantGenerators);
    }

    public void addMarginal ( String generatorName, Integer period, Double marginalValue ) {
        addMarginal(generatorName, period, null, marginalValue);
    }

    public boolean isDependantGenerator( IPerilMarker generator ) {
        return dependantGenerators.contains(generator);
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
        MarginalAndEvent marginalAndEvent = marginals.get(generatorPeriod);
        if(marginalAndEvent == null) {
            throw new SimulationException("Attempted to lookup a marginal distribution in period: " + period + " for generator" +
                    "" + generatorName + ". Have you initialised the dependancy structure correctly? Please contract development");
        }
        return marginalAndEvent;
    }

    /* We don't want people to be able to tamper with this outside of it's initialisation ! */
    public Map<GeneratorPeriod, MarginalAndEvent> getMarginals() {
        return Collections.unmodifiableMap(marginals);
    }

    public DependancePacket immutable() {
        return new DependancePacket(marginals, dependantGenerators);
    }

    public List<Component> getDependantGenerators() {
        return dependantGenerators;
    }
}
