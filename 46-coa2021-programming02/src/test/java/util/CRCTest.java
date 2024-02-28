package util;

import org.junit.*;

import static org.junit.Assert.*;

public class CRCTest {

    @Test
    public void CRCTrueTest1() {
        char[] data = "100011".toCharArray();
        String p = "1001";
        char[] originCRC = CRC.Calculate(data, p);
        //assertArrayEquals(originCRC, "111".toCharArray());
        assertArrayEquals(CRC.Check(data, p, originCRC), "000".toCharArray());
    }

}
