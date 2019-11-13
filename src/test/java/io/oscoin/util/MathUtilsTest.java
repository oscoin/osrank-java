package io.oscoin.util;

import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testApproxEqual() {

        assert(MathUtils.approxEqual(1d, 1.000000000000001d));

        assert(MathUtils.approxEqual(1d, 2d) == false);
    }
}
