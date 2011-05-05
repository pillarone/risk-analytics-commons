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
public class UniformIntList {

    public static List<Integer> getIntegers(int number, boolean sorted, int i, int j) {
        return getIntegers(number, sorted, MathUtils.getRandomStreamBase(), i, j);
    }

    public static List<Integer> getIntegers(int number, boolean sorted, RandomStreamBase stream, int i, int j) {
        IRandomNumberGenerator generator = RandomNumberGeneratorFactory.getUniformIntGenerator(stream, i, j);
        List<Integer> integerList = new ArrayList<Integer>();
        for (int k = 0; k < number; k++) {
            integerList.add((Integer) generator.nextValue());
        }
        if (sorted) {
            Collections.sort(integerList);
            return integerList;
        }
        else {
            return integerList;
        }
    }

    public static List<Integer> getIntegers(int number, int i, int j) {
        return getIntegers(number, false, i, j);
    }

}