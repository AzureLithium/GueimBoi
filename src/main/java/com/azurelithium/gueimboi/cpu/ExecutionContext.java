package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.utils.StringUtils;

public class ExecutionContext {

    private long cycles;
    private int data;
    private int dataAddress;
    boolean executeNextStep;
    Registers registers;
    MMU MMU;
    ALU ALU;

    public long getCycles() {
        return cycles;
    }

    public void setCycles(long _cycles) {
        cycles = _cycles;
    }

    void addCycles() {
        cycles += 4;
    }

    int getData() {
        return data;
    }

    int getDataLSB() {
        return data & 0xFF;
    }

    int getDataMSB() {
        return (data >> Byte.SIZE) & 0xFF;
    }

    void setData(int _data) {
        data = _data;
    }

    int getDataAddress() {
        return dataAddress;
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
