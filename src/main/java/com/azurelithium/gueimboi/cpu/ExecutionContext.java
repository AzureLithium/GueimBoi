package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.utils.StringUtils;

public class ExecutionContext {

    Registers registers;
    MMU MMU;
    ALU ALU;

    private boolean IME;
    private boolean scheduleIME;
    boolean HALT;

    private int data;
    private int dataAddress;
    boolean executeNextStep;

    public boolean getScheduleIME() {
        return scheduleIME;
    }

    public void setScheduleIME(boolean _scheduleIME) {
        scheduleIME = _scheduleIME;
    }

    public boolean getIME() {
        return IME;
    }

    public void setIME(boolean _IME) {
        IME = _IME;
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
