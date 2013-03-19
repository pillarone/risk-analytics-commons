package org.pillarone.riskanalytics.domain.utils.math.dependance;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.pillarone.riskanalytics.core.simulation.SimulationException;

/**
 * Created with IntelliJ IDEA.
 * User: sparten
 * Date: 11.01.13
 * Time: 09:59
 * To change this template use File | Settings | File Templates.
 */
public class MarginalAndEvent {

    final Event event;
    final Double marginalProbability;
    final GeneratorPeriod generatorPeriod;

    public MarginalAndEvent(GeneratorPeriod generatorPeriod, Double marginalProbability, Event event) {
        this.generatorPeriod = generatorPeriod;
        if(marginalProbability < 0 || marginalProbability > 1 || marginalProbability.isNaN()) {
            throw new SimulationException("Probability must be between 1 and 0. Cannot be " + marginalProbability +  " . " + generatorPeriod.toString());
        }
        this.marginalProbability = marginalProbability;
        this.event = event;
    }

    public MarginalAndEvent(Double marginalProbability, GeneratorPeriod generatorPeriod) {
        this.marginalProbability = marginalProbability;
        this.generatorPeriod = generatorPeriod;
        this.event = null;
    }

    public Event getEventOrNull() {
        return event;
    }

    public Double getMarginalProbability() {
        return marginalProbability;
    }

    public GeneratorPeriod getGeneratorPeriod() {
        return generatorPeriod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarginalAndEvent that = (MarginalAndEvent) o;

        if (event != null ? !event.equals(that.event) : that.event != null) return false;
        if (generatorPeriod != null ? !generatorPeriod.equals(that.generatorPeriod) : that.generatorPeriod != null)
            return false;
        if (marginalProbability != null ? !marginalProbability.equals(that.marginalProbability) : that.marginalProbability != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = event != null ? event.hashCode() : 0;
        result = 31 * result + (marginalProbability != null ? marginalProbability.hashCode() : 0);
        result = 31 * result + (generatorPeriod != null ? generatorPeriod.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MarginalAndEvent{" +
                "event=" + event +
                ", marginalProbability=" + marginalProbability +
                ", generatorPeriod=" + generatorPeriod.toString() +
                '}';
    }
}
