package ru.collapsedev.collapseapi.util;

import lombok.experimental.UtilityClass;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class RandomUtil {

    private final Random random = new Random();

    public double randomDouble(double bound) {
        if (bound <= 0) {
            return 0;
        }

        return random.nextDouble() * bound;
    }

    public double randomDouble(double bound1, double bound2) {
        if (bound1 <= 0 && bound2 <= 0) {
            return 0;
        }

        return bound1 + (bound2 - bound1) * random.nextDouble();
    }

    public double randomDoubleMulti(double bound) {
        if (bound <= 0) {
            return 0;
        }

        return -bound + 2 * bound * random.nextDouble();
    }

    public int randomInt(int bound) {
        if (bound <= 0) {
            return 0;
        }

        return random.nextInt(bound);
    }

    public int randomInt(int bound1, int bound2) {
        if (bound1 <= 0 && bound2 <= 0) {
            return 0;
        }

        return random.nextInt(bound2 - bound1) + bound1;
    }

    public int randomIntMulti(int bound) {
        if (bound <= 0) {
            return 0;
        }

        return random.nextInt(2 * bound) - bound;
    }

    public boolean randomBoolean() {
        return random.nextBoolean();
    }

    public boolean random100(int chance) {
        return RandomUtil.randomInt(100) < chance;
    }

    public boolean random100(double chance) {
        return RandomUtil.randomDouble(100) < chance;
    }
}
