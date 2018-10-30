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
    int allReset, allSet;

    @Before
    public void testInit() {
        mostNegativeByte = Byte.MIN_VALUE;
        mostNegativeWord = Short.MIN_VALUE;
        mostPositiveByte = Byte.MAX_VALUE;
        mostPositiveWord = Short.MAX_VALUE;
        allReset = 0x0000;
        allSet = 0xFFFF;
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
    }

    @Test
    public void testGetMSB() {
        assertEquals(ByteUtils.getMSB(mostNegativeWord), (int)mostPositiveByte + 1);
        assertEquals(ByteUtils.getMSB(mostPositiveWord), mostPositiveByte);
    }

    @Test
    public void testGetLSB() {
        assertEquals(ByteUtils.getLSB(mostNegativeWord), 0);
        assertEquals(ByteUtils.getLSB(mostPositiveWord), 0xFF);
    }

    /**
     * Composition
     */

    @Test
    public void testToWordFromBytes() {
        assertEquals(ByteUtils.toWord(mostPositiveByte, -1), Short.MAX_VALUE);
        assertEquals(ByteUtils.toWord(mostNegativeByte, allZeroByte), Short.MAX_VALUE + 1);
        assertEquals(ByteUtils.toWord(allZeroByte, allZeroByte), allZeroWord);
        assertEquals(ByteUtils.toWord(allZeroByte, mostPositiveByte), mostPositiveByte);
        assertEquals(ByteUtils.toWord(allZeroByte, mostNegativeByte), Byte.MAX_VALUE + 1);
    }

    /**
     * Modification
     */

    @Test
    public void testSetBit() {
        for (int i=0 ; i<Short.SIZE; i++) {
            assertEquals(ByteUtils.setBit(allReset, i), 1 << i);
        }
    }

    @Test
    public void testResetBit() {
        for (int i=0 ; i<Short.SIZE; i++) {
            assertEquals(ByteUtils.resetBit(allSet, i), allSet ^ (1 << i));
        }
    }

}
