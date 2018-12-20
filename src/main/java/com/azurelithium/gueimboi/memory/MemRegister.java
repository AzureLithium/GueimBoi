package com.azurelithium.gueimboi.memory;

import com.azurelithium.gueimboi.common.GameBoy;
import com.azurelithium.gueimboi.timer.Timer;

class MemRegister {

    protected Memory memory;
    protected int memRegisterAddress;

    protected MemRegister(Memory _memory, int _memRegisterAddress) {
        memory = _memory;
        memRegisterAddress = _memRegisterAddress;
    }

    int getAddress() {
        return memRegisterAddress;
    }

    int read() {
        return memory.readByte(memRegisterAddress) & 0xFF;
    }

    int controlledRead() {
        return read();
    }

    void write(int value) {
        memory.writeByte(memRegisterAddress, value & 0xFF);
    }

    void controlledWrite(int value) {
        write(value);
    }
}


class DIV extends MemRegister {

    private Timer timer;

    DIV(Memory _memory, int _memRegisterAddress, Timer _timer) {
        super(_memory, _memRegisterAddress);
        timer = _timer;
    }

    void controlledWrite(int value) {
        timer.checkTIMAUnexpectedIncrease();
        memory.writeByte(memRegisterAddress - 1, 0);
        memory.writeByte(memRegisterAddress, 0);
    }

}


class DIVLSB extends MemRegister {

    private Timer timer;

    DIVLSB(Memory _memory, int _memRegisterAddress, Timer _timer) {
        super(_memory, _memRegisterAddress);
        timer = _timer;
    }

    void controlledWrite(int value) {
        timer.checkTIMAUnexpectedIncrease();
        memory.writeByte(memRegisterAddress, 0);
        memory.writeByte(memRegisterAddress + 1, 0);
    }

}


class TIMA extends MemRegister {

    private Timer timer;

    TIMA(Memory _memory, int _memRegisterAddress, Timer _timer) {
        super(_memory, _memRegisterAddress);
        timer = _timer;
    }

    void controlledWrite(int value) {
        if (timer.getTicksSinceOverflow() < GameBoy.CYCLES_PER_CPU_TICK) {    // normal or in overflow
            write(value);
            timer.unsetOverflow();
        }
    }

}


class TMA extends MemRegister {

    private Timer timer;

    TMA(Memory _memory, int _memRegisterAddress, Timer _timer) {
        super(_memory, _memRegisterAddress);
        timer = _timer;
    }

    void controlledWrite(int value) {
        write(value);
        if (timer.getTicksSinceOverflow() >= GameBoy.CYCLES_PER_CPU_TICK) {   // just after overflow        
            timer.setTIMA(value);
        }
    }

}


class TAC extends MemRegister {

    private Timer timer;

    TAC(Memory _memory, int _memRegisterAddress, Timer _timer) {
        super(_memory, _memRegisterAddress);
        timer = _timer;
    }

    void controlledWrite(int value) {
        boolean oldTickRateBit = timer.isTimerEnabled() && timer.getTickRateBit();
        write(value);
        boolean newTickRateBit = timer.isTimerEnabled() && timer.getTickRateBit();
        if (oldTickRateBit && !newTickRateBit) {
            timer.checkTIMAUnexpectedIncrease();
        }
    }

}


class IF extends MemRegister {

    private Timer timer;

    IF(Memory _memory, int _memRegisterAddress, Timer _timer) {
        super(_memory, _memRegisterAddress);
        timer = _timer;
    }

    int read() {
        return super.read() | 0xE0;
    }

    void write(int value) {
        super.write(value | 0xE0);
    }

    void controlledWrite(int value) {
        write(value);
        if (timer.getTicksSinceOverflow() >= GameBoy.CYCLES_PER_CPU_TICK) {   // just after overflow
            timer.setIFOverride();
        }
    }

}


class STAT extends MemRegister {

    STAT(Memory _memory, int _memRegisterAddress) {
        super(_memory, _memRegisterAddress);
    }

    int read() {
        return super.read() | 0x80;
    }

    void write(int value) {
        super.write(value | 0x80);
    }

    void controlledWrite(int value) {
        int STAT = read();
        write((value & 0x78) | STAT);
    }

}


class ROM_DISABLE extends MemRegister {

    private MMU MMU;

    ROM_DISABLE(Memory memory, int memRegisterAddress, MMU _MMU) {
        super(memory, memRegisterAddress);
        MMU = _MMU;
    }

    void controlledWrite(int value) {
        memory.writeByte(memRegisterAddress, value);
        if (value == 0x01) { // bootrom disabling
            MMU.loadCartridge();
        }
    }

}


class IE extends MemRegister {

    IE(Memory _memory, int _memRegisterAddress) {
        super(_memory, _memRegisterAddress);
    }

    int read() {
        return super.read() | 0xE0;
    }

    void write(int value) {
        super.write(value | 0xE0) ;
    }

}
