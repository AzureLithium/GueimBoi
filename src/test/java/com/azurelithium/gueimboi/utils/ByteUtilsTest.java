package com.azurelithium.gueimboi.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test for ByteUtils class.
 */
public class ByteUtilsTest {

    byte mostNegativeByte, mostPositiveByte, allZeroByte, allOneByte;
    short mostNegativeWord, mostPositiveWord, allZeroWord, allOneWord;
    int mostNegativeInt, mostPositiveInt, allZeroInt, allOneInt;
    int allUnset, allSet;

    @Before
    public void testInit() {
        mostNegativeByte = Byte.MIN_VALUE;
        mostNegativeWord = Short.MIN_VALUE;
        mostNegativeInt = Integer.MIN_VALUE;
        mostPositiveByte = Byte.MAX_VALUE;
        mostPositiveWord = Short.MAX_VALUE;
        mostPositiveInt = Integer.MAX_VALUE;
        allUnset = 0x00000000;
        allSet = 0xFFFFFFFF;
    }

    /**
     * Extraction
     */

    @Test
    public void testGetBit() {
        assertFalse(ByteUtils.getBit(mostNegativeByte, 0));
        assertTrue(ByteUtils.getBit(mostNegativeByte, 7));
        assertTrue(ByteUtils.getBit(mostPositiveByte, 0));
        assertFalse(ByteUtils.getBit(mostPositiveByte, 7));
        assertFalse(ByteUtils.getBit(mostNegativeWord, 0));
        assertTrue(ByteUtils.getBit(mostNegativeWord, 15));
        assertTrue(ByteUtils.getBit(mostPositiveWord, 0));
        assertFalse(ByteUtils.getBit(mostPositiveWord, 15));
        assertFalse(ByteUtils.getBit(mostNegativeInt, 0));
        assertTrue(ByteUtils.getBit(mostNegativeInt, 31));
        assertTrue(ByteUtils.getBit(mostPositiveInt, 0));
        assertFalse(ByteUtils.getBit(mostPositiveInt, 31));
    }

    @Test
    public void testGetMSB() {
        assertEquals(ByteUtils.getMSB(mostNegativeWord), (int)mostPositiveByte + 1);
        assertEquals(ByteUtils.getMSB(mostNegativeInt), 0);
        assertEquals(ByteUtils.getMSB(mostPositiveWord), mostPositiveByte);
        assertEquals(ByteUtils.getMSB(mostPositiveInt), 0xFF);
    }

    @Test
    public void testGetLSB() {
        assertEquals(ByteUtils.getLSB(mostNegativeWord), 0);
        assertEquals(ByteUtils.getLSB(mostNegativeInt), 0);
        assertEquals(ByteUtils.getLSB(mostPositiveWord), 0xFF);
        assertEquals(ByteUtils.getLSB(mostPositiveInt), 0xFF);
    }

    /**
     * Composition
     */

    @Test
    public void testToWordFromBytes() {
        assertEquals(ByteUtils.toWord(mostPositiveByte, (byte) -1), Short.MAX_VALUE);
        assertEquals(ByteUtils.toWord((byte) 0, Byte.MAX_VALUE), Byte.MAX_VALUE);
        assertEquals(ByteUtils.toWord(mostNegativeByte, Byte.MAX_VALUE),
                Short.MIN_VALUE + Byte.MAX_VALUE);
        assertEquals(ByteUtils.toWord(allZeroByte, allZeroByte), allZeroWord);
        assertEquals(ByteUtils.toWord(allZeroByte, mostPositiveByte), mostPositiveByte);
        assertEquals(ByteUtils.toWord(allZeroByte, mostNegativeByte), Byte.MAX_VALUE + 1);
        assertEquals(ByteUtils.toWord(mostNegativeByte, allZeroByte), mostNegativeWord);
    }

    /**
     * Modification
     */

    @Test
    public void testSetBit() {
        for (int i=0 ; i<32; i++) {
            assertEquals(ByteUtils.setBit(allUnset, i), 1 << i);
        }
    }

    @Test
    public void testUnsetBit() {
        for (int i=0 ; i<32; i++) {
            assertEquals(ByteUtils.unsetBit(allSet, i), 0xFFFFFFFF ^ (1 << i));
        }
    }

}
