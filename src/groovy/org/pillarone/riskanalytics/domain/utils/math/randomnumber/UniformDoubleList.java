package org.pillarone.riskanalytics.domain.utils.math.randomnumber;

import org.pillarone.riskanalytics.core.util.MathUtils;
import org.pillarone.riskanalytics.domain.utils.math.generator.IRandomNumberGenerator;
import org.pillarone.riskanalytics.domain.utils.math.generator.RandomNumberGeneratorFactory;
import umontreal.iro.lecuyer.rng.RandomStreamBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class UniformDoubleList {

    public static List<Double> getDoubles(int number, boolean sorted) {
        return getDoubles(number, sorted, MathUtils.getRandomStreamBase());
    }

    public static List<Double> getDoubles(int number, boolean sorted, RandomStreamBase stream) {
        IRandomNumberGenerator generator = RandomNumberGeneratorFactory.getUniformGenerator(stream);
        List<Double> doubleList = new ArrayList<Double>();
        for (int i = 0; i < number; i++) {
            doubleList.add((Double) generator.nextValue());
        }
        if (sorted) {
            Collections.sort(doubleList);
            return doubleList;
        }
        else {
            return doubleList;
        }
    }

    public static List<Double> getDoubles(int number) {
        return getDoubles(number, false);
    }

    public static List<Double> getDoubles(int number, double a, double b, RandomStreamBase stream) {
        IRandomNumberGenerator generator = RandomNumberGeneratorFactory.getUniformGenerator(a, b, stream);
        List<Double> doubleList = new ArrayList<Double>();
        for (int i = 0; i < number; i++) {
            doubleList.add((Double) generator.nextValue());
        }
        return doubleList;
    }

    public static List<Double> getDoubles(int number, double a, double b) {
        return getDoubles(number, a, b, MathUtils.getRandomStreamBase());
    }
}