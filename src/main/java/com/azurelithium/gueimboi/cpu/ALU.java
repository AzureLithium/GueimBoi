package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.ByteUtils;
import com.azurelithium.gueimboi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ALU {

    final Logger logger = LoggerFactory.getLogger(getClass());

    void XOR(ExecutionContext executionContext) {
        int XORResult = executionContext.getPrimaryData() ^ executionContext.registers.getA();
        executionContext.registers.setA(XORResult);
        Flags flags = executionContext.registers.getFlags();
        flags.setZ(XORResult == 0x0);
        flags.unsetN();
        flags.unsetH();
        flags.unsetC();
        logger.trace("XOR = {}. Flags = {} .", StringUtils.toHex(XORResult), flags);
    }

    void testBit(ExecutionContext executionContext, int bit) {
        boolean testBit = ByteUtils.getBit(executionContext.getPrimaryData(), bit);
        Flags flags = executionContext.registers.getFlags();
        flags.setZ(!testBit);
        flags.unsetN();
        flags.setH();
        logger.trace("BIT {} = {}. Flags = {} .", bit, testBit, flags);
    }

}
