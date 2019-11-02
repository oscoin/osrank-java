package io.oscoin.util;

public class MathUtils {

    public static double EPSILON_FOR_APPROXIMATE_EQUALITY = 0.000001d;

    public static boolean approxEqual(double first, double second) {
        if (Math.abs(first - second) < EPSILON_FOR_APPROXIMATE_EQUALITY) return true;
        return false;
    }

}
