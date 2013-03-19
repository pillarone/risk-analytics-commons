package org.pillarone.riskanalytics.domain.utils.math.dependance;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.pillarone.riskanalytics.core.components.Component;

/**
 * Created with IntelliJ IDEA.
 * User: sparten
 * Date: 11.01.13
 * Time: 09:59
 * To change this template use File | Settings | File Templates.
 */
public class GeneratorPeriod {

    final String generatorName;
    final Integer period;
    final Component component;

    public GeneratorPeriod(String generatorName, Integer period) {
        this.generatorName = generatorName;
        this.period = period;
        this.component = null;
    }

    public GeneratorPeriod(Component generator, Integer period) {
        this.generatorName = generator.getName();
        this.period = period;
        this.component = generator;
    }

    public String getGeneratorName() {
        return generatorName;
    }

    public Integer getPeriod() {
        return period;
    }

    public Component getComponent() {
        return component;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneratorPeriod that = (GeneratorPeriod) o;

        return new EqualsBuilder().
                append(generatorName, that.generatorName).
                append(period, that.period).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(generatorName).append(period).toHashCode();
    }

    @Override
    public String toString() {
        return "GeneratorPeriod{" +
                "generatorName='" + generatorName + '\'' +
                ", period=" + period +
                '}';
    }
}
