package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.utils.StringUtils;

class ExecutionContext {

    int data;
    int dataAddress;
    Registers registers;
    MMU MMU;
    ALU ALU;

    String printData() {
        return StringUtils.toHex(data);
    }

    String printDataAddress() {
        return StringUtils.toHex(dataAddress);
    }

}