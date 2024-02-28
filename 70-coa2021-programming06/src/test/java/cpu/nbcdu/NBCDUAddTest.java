package cpu.nbcdu;

import org.junit.Test;
import util.DataType;
import util.Transformer;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class NBCDUAddTest {

	private final NBCDU nbcdu = new NBCDU();
	private final Transformer transformer = new Transformer();
	private DataType src;
	private DataType dest;
	private DataType result;

	@Test
	public void AddTest1() {
		src = new DataType("11000000000000000000000010011000");
		dest = new DataType("11000000000000000000000001111001");
		result = nbcdu.add(src, dest);
		assertEquals("11000000000000000000000101110111", result.toString());
	}
	@Test
	public void AddTest3() {
		src = new DataType("11010000000000000000000100000000");
		dest = new DataType("11000000000000000000000100000001");
		result = nbcdu.add(src, dest);
		assertEquals("11000000000000000000000000000001", result.toString());
	}
	@Test
	public void AddTest2() {
		for (int i = -100; i < 100; i++) {
			for (int j = -100; j < 1000; j++) {
				src = new DataType(transformer.decimalToNBCD(Integer.toString(i)));
				dest = new DataType(transformer.decimalToNBCD(Integer.toString(j)));
				result = nbcdu.add(src, dest);
				System.out.println(src);
				System.out.println(dest);
				assertEquals(transformer.decimalToNBCD(Integer.toString(i + j)), result.toString());
			}
		}
	}

}
