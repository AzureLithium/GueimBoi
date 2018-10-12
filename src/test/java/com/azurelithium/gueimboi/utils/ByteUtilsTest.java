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

    @Before
    public void testInit() {
        mostNegativeByte = Byte.MIN_VALUE;
        mostNegativeWord = Short.MIN_VALUE;
        mostNegativeInt = Integer.MIN_VALUE;
        mostPositiveByte = Byte.MAX_VALUE;
        mostPositiveWord = Short.MAX_VALUE;
        mostPositiveInt = Integer.MAX_VALUE;
        allZeroInt = allZeroWord = allZeroByte = 0;
        allOneInt = allOneWord = allOneByte = -1;
    }

    /**
    * Calculation
    */

    @Test
    public void testIsNegative() {
        assertTrue(ByteUtils.isNegative(mostNegativeByte));        
        assertTrue(ByteUtils.isNegative(mostNegativeWord));
        assertTrue(ByteUtils.isNegative(mostNegativeInt));
        assertFalse(ByteUtils.isNegative(mostPositiveByte));
        assertFalse(ByteUtils.isNegative(mostPositiveWord));
        assertFalse(ByteUtils.isNegative(mostPositiveInt));
    }

    @Test
    public void testIsPositive() {
        assertFalse(ByteUtils.isPositive(mostNegativeByte));        
        assertFalse(ByteUtils.isPositive(mostNegativeWord));
        assertFalse(ByteUtils.isPositive(mostNegativeInt));
        assertTrue(ByteUtils.isPositive(mostPositiveByte));
        assertTrue(ByteUtils.isPositive(mostPositiveWord));
        assertTrue(ByteUtils.isPositive(mostPositiveInt));
    }

    @Test
    public void testAbs() {
        assertEquals(ByteUtils.abs(mostNegativeByte), mostPositiveByte + 1);        
        assertEquals(ByteUtils.abs(mostNegativeWord), mostPositiveWord + 1);
        assertEquals(ByteUtils.abs(mostPositiveByte), mostPositiveByte);
        assertEquals(ByteUtils.abs(mostPositiveWord), mostPositiveWord);
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
    public void testGetMSb() {
        assertTrue(ByteUtils.getMSb(mostNegativeByte));      
        assertTrue(ByteUtils.getMSb(mostNegativeWord));
        assertTrue(ByteUtils.getMSb(mostNegativeInt));
        assertFalse(ByteUtils.getMSb(mostPositiveByte));      
        assertFalse(ByteUtils.getMSb(mostPositiveWord));
        assertFalse(ByteUtils.getMSb(mostPositiveInt));
    }

    @Test
    public void testGetLSb() {
        assertFalse(ByteUtils.getLSb(mostNegativeByte));      
        assertFalse(ByteUtils.getLSb(mostNegativeWord));
        assertFalse(ByteUtils.getLSb(mostNegativeInt));
        assertTrue(ByteUtils.getLSb(mostPositiveByte));      
        assertTrue(ByteUtils.getLSb(mostPositiveWord));
        assertTrue(ByteUtils.getLSb(mostPositiveInt));
    }

    @Test
    public void testGetMSB() {     
        assertEquals(ByteUtils.getMSB(mostNegativeWord), mostNegativeByte);
        assertEquals(ByteUtils.getMSB(mostNegativeInt), mostNegativeByte);    
        assertEquals(ByteUtils.getMSB(mostPositiveWord), mostPositiveByte);
        assertEquals(ByteUtils.getMSB(mostPositiveInt), mostPositiveByte);
    }

    @Test
    public void testGetLSB() {
        assertEquals(ByteUtils.getLSB(mostNegativeWord), 0);
        assertEquals(ByteUtils.getLSB(mostNegativeInt), 0);    
        assertEquals(ByteUtils.getLSB(mostPositiveWord), -1);
        assertEquals(ByteUtils.getLSB(mostPositiveInt), -1);
    }

    @Test
    public void testGetMSW() {     
        assertEquals(ByteUtils.getMSW(mostNegativeInt), mostNegativeWord);
        assertEquals(ByteUtils.getMSW(mostPositiveInt), mostPositiveWord); 
    }

    @Test
    public void testGetLSW() {
        assertEquals(ByteUtils.getLSW(mostNegativeInt), 0);
        assertEquals(ByteUtils.getLSW(mostPositiveInt), -1);
    }

    @Test
    public void testToByte() {
        assertEquals(ByteUtils.toByte(mostNegativeByte), mostNegativeByte);
        assertEquals(ByteUtils.toByte(mostPositiveByte), mostPositiveByte); 
        assertEquals(ByteUtils.toByte(mostNegativeWord), allZeroByte);
        assertEquals(ByteUtils.toByte(mostPositiveWord), -1); 
        assertEquals(ByteUtils.toByte(mostNegativeInt), allZeroByte);
        assertEquals(ByteUtils.toByte(mostPositiveInt), -1);
    }

    @Test
    public void testToWordFromInt() {
        assertEquals(ByteUtils.toWord(mostNegativeByte), mostNegativeByte);
        assertEquals(ByteUtils.toWord(mostPositiveByte), mostPositiveByte); 
        assertEquals(ByteUtils.toWord(mostNegativeWord), mostNegativeWord);
        assertEquals(ByteUtils.toWord(mostPositiveWord), mostPositiveWord); 
        assertEquals(ByteUtils.toWord(mostNegativeInt), allZeroWord);
        assertEquals(ByteUtils.toWord(mostPositiveInt), -1);
    }

    /**
    * Composition
    */

    @Test
    public void testToWordFromBytes() {
        assertEquals(ByteUtils.toWord(mostPositiveByte, (byte)-1), Short.MAX_VALUE);
        assertEquals(ByteUtils.toWord((byte)0, Byte.MAX_VALUE), Byte.MAX_VALUE);
        assertEquals(ByteUtils.toWord(mostNegativeByte, Byte.MAX_VALUE), Short.MIN_VALUE + Byte.MAX_VALUE);
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
        assertEquals(ByteUtils.setBit(allZeroByte, 0), (byte)0b00000001);
        assertEquals(ByteUtils.setBit(allZeroByte, 1), (byte)0b00000010);
        assertEquals(ByteUtils.setBit(allZeroByte, 2), (byte)0b00000100);
        assertEquals(ByteUtils.setBit(allZeroByte, 3), (byte)0b00001000);
        assertEquals(ByteUtils.setBit(allZeroByte, 4), (byte)0b00010000);
        assertEquals(ByteUtils.setBit(allZeroByte, 5), (byte)0b00100000);
        assertEquals(ByteUtils.setBit(allZeroByte, 6), (byte)0b01000000);
        assertEquals(ByteUtils.setBit(allZeroByte, 7), (byte)0b10000000);
        assertEquals(ByteUtils.setBit(allZeroWord, 0), (short)0b0000000000000001);
        assertEquals(ByteUtils.setBit(allZeroWord, 1), (short)0b0000000000000010);
        assertEquals(ByteUtils.setBit(allZeroWord, 2), (short)0b0000000000000100);
        assertEquals(ByteUtils.setBit(allZeroWord, 3), (short)0b0000000000001000);
        assertEquals(ByteUtils.setBit(allZeroWord, 4), (short)0b0000000000010000);
        assertEquals(ByteUtils.setBit(allZeroWord, 5), (short)0b0000000000100000);
        assertEquals(ByteUtils.setBit(allZeroWord, 6), (short)0b0000000001000000);
        assertEquals(ByteUtils.setBit(allZeroWord, 7), (short)0b0000000010000000);
        assertEquals(ByteUtils.setBit(allZeroWord, 8), (short)0b0000000100000000);
        assertEquals(ByteUtils.setBit(allZeroWord, 9), (short)0b0000001000000000);
        assertEquals(ByteUtils.setBit(allZeroWord, 10), (short)0b0000010000000000);
        assertEquals(ByteUtils.setBit(allZeroWord, 11), (short)0b0000100000000000);
        assertEquals(ByteUtils.setBit(allZeroWord, 12), (short)0b0001000000000000);
        assertEquals(ByteUtils.setBit(allZeroWord, 13), (short)0b0010000000000000);
        assertEquals(ByteUtils.setBit(allZeroWord, 14), (short)0b0100000000000000);
        assertEquals(ByteUtils.setBit(allZeroWord, 15), (short)0b1000000000000000);
    }

    @Test
    public void testUnsetBit() {
        assertEquals(ByteUtils.unsetBit(allOneByte, 0), (byte)0b11111110);
        assertEquals(ByteUtils.unsetBit(allOneByte, 1), (byte)0b11111101);
        assertEquals(ByteUtils.unsetBit(allOneByte, 2), (byte)0b11111011);
        assertEquals(ByteUtils.unsetBit(allOneByte, 3), (byte)0b11110111);
        assertEquals(ByteUtils.unsetBit(allOneByte, 4), (byte)0b11101111);
        assertEquals(ByteUtils.unsetBit(allOneByte, 5), (byte)0b11011111);
        assertEquals(ByteUtils.unsetBit(allOneByte, 6), (byte)0b10111111);
        assertEquals(ByteUtils.unsetBit(allOneByte, 7), (byte)0b01111111);
        assertEquals(ByteUtils.unsetBit(allOneWord, 0), (short)0b1111111111111110);
        assertEquals(ByteUtils.unsetBit(allOneWord, 1), (short)0b1111111111111101);
        assertEquals(ByteUtils.unsetBit(allOneWord, 2), (short)0b1111111111111011);
        assertEquals(ByteUtils.unsetBit(allOneWord, 3), (short)0b1111111111110111);
        assertEquals(ByteUtils.unsetBit(allOneWord, 4), (short)0b1111111111101111);
        assertEquals(ByteUtils.unsetBit(allOneWord, 5), (short)0b1111111111011111);
        assertEquals(ByteUtils.unsetBit(allOneWord, 6), (short)0b1111111110111111);
        assertEquals(ByteUtils.unsetBit(allOneWord, 7), (short)0b1111111101111111);
        assertEquals(ByteUtils.unsetBit(allOneWord, 8), (short)0b1111111011111111);
        assertEquals(ByteUtils.unsetBit(allOneWord, 9), (short)0b1111110111111111);
        assertEquals(ByteUtils.unsetBit(allOneWord, 10), (short)0b1111101111111111);
        assertEquals(ByteUtils.unsetBit(allOneWord, 11), (short)0b1111011111111111);
        assertEquals(ByteUtils.unsetBit(allOneWord, 12), (short)0b1110111111111111);
        assertEquals(ByteUtils.unsetBit(allOneWord, 13), (short)0b1101111111111111);
        assertEquals(ByteUtils.unsetBit(allOneWord, 14), (short)0b1011111111111111);
        assertEquals(ByteUtils.unsetBit(allOneWord, 15), (short)0b0111111111111111);
    }

}
