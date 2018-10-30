package com.azurelithium.gueimboi.cpu;

import java.math.BigInteger;
import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.utils.StringUtils;

class ExecutionContext {

    private int data;
    int dataAddress;
    boolean executeNextStep;
    Registers registers;
    MMU MMU;
    ALU ALU;

    int getData() {
        return data;
    }

    int[] getDataBytes() {
        BigInteger dataBig = BigInteger.valueOf(data);
        byte[] bytes = dataBig.toByteArray();
        int[] dataBytes = new int[bytes.length];
        for (int i=0 ; i<bytes.length ; i++) {
            dataBytes[i] = (int) bytes[i];
        }
        return dataBytes;
    }

    void setData(int _data) {
        data = _data;
    }

    String printData() {
        return StringUtils.toHex(data);
    }

    String printDataAddress() {
        return StringUtils.toHex(dataAddress);
    }

}