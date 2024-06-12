package ru.collapsedev.collapseapi.util;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class RandomUtil {

    public double randomDouble(double bound) {
        if (bound <= 0) {
            return 0;
        }

        return ThreadLocalRandom.current().nextDouble(bound);
    }

    public double randomDoubleMulti(double bound) {
        if (bound <= 0) {
            return 0;
        }

        return ThreadLocalRandom.current().nextDouble(-bound, bound);
    }

    public int randomInt(int bound) {
        if (bound <= 0) {
            return 0;
        }

        return ThreadLocalRandom.current().nextInt(bound);
    }

    public int randomIntMulti(int bound) {
        if (bound <= 0) {
            return 0;
        }

        return ThreadLocalRandom.current().nextInt(-bound, bound);
    }

    public boolean randomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }
}
