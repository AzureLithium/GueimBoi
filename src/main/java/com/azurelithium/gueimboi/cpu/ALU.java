package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ALU {

    final Logger logger = LoggerFactory.getLogger(ALU.class);

    private ExecutionContext executionContext;

    ALU(ExecutionContext _executionContext) {
        executionContext = _executionContext;
    }

    int XOR() {
        int XORResult = executionContext.data ^ executionContext.registers.getA();
        executionContext.registers.setA(XORResult);
        Flags flags = executionContext.registers.getFlags();
        flags.setZ(XORResult == 0x0);
        flags.unsetN();
        flags.unsetH();
        flags.unsetC();
        logger.trace("XOR = {}. Flags = {} .", StringUtils.toHex(XORResult), flags);
        return XORResult;
    }

}
