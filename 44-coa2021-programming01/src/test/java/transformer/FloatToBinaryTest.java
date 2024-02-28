package transformer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FloatToBinaryTest {

    private Transformer t = new Transformer();

    @Test
    public void test1() {
        String result = t.floatToBinary(String.valueOf(Math.pow(2, -127)));
        assertEquals("00000000010000000000000000000000", result);
    }

    @Test
    public void test8() {
        String result = t.floatToBinary("" + Double.MAX_VALUE);
        assertEquals("+Inf", result);
    }

}
