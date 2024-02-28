package transformer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BinaryToFloatTest {

	Transformer t = new Transformer();

	@Test
	public void test1() {
		String result = t.binaryToFloat("00000000010000000000000000000000");
		assertEquals(String.valueOf(Math.pow(2, -127)), result);
	}

}
