package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.utils.ByteUtils;
import com.azurelithium.gueimboi.utils.StringUtils;
import java.math.BigInteger;

class ExecutionContext {

    private int data;
    private int dataAddress;
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
        for (int i = 0; i < bytes.length; i++) {
            dataBytes[i] = (int) bytes[i];
        }
        return dataBytes;
    }

    void setData(int _data) {
        data = _data;
    }

    int getDataAddress() {
        return dataAddress;
    }

    int[] getDataAddressBytes() {
        return new int[] {ByteUtils.getMSB(dataAddress), ByteUtils.getLSB(dataAddress)};
    }

    void setDataAddress(int _dataAddress) {
        dataAddress = _dataAddress;
    }

    String printData() {
        return StringUtils.toHex(data);
    }

    String printDataAddress() {
        return StringUtils.toHex(dataAddress);
    }

}
