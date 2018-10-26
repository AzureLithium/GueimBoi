package com.azurelithium.gueimboi.cpu;

import java.math.BigInteger;
import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.utils.StringUtils;

class ExecutionContext {

    private int primaryData;
    private int secondaryData;
    int dataAddress;
    boolean executeNextStep;
    Registers registers;
    MMU MMU;
    ALU ALU;

    int getPrimaryData() {
        return primaryData;
    }

    int[] getPrimaryDataBytes() {
        BigInteger bigInt = BigInteger.valueOf(primaryData);
        byte[] bytes = bigInt.toByteArray();
        int[] primaryDataBytes = new int[bytes.length];
        for (int i=0 ; i<bytes.length ; i++) {
            primaryDataBytes[i] = (int) bytes[i];
        }
        return primaryDataBytes;
    }

    void setPrimaryData(int data) {
        primaryData = data;
    }

    int getSecondaryData() {
        return secondaryData;
    }

    void setSecondaryData(int data) {
        secondaryData = data;
    }

    String printPrimaryData() {
        return StringUtils.toHex(primaryData);
    }

    String printSecondaryData() {
        return StringUtils.toHex(secondaryData);
    }

    String printDataAddress() {
        return StringUtils.toHex(dataAddress);
    }

}