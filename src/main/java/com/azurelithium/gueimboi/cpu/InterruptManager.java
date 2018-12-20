package com.azurelithium.gueimboi.cpu;

import java.util.ListIterator;

import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.memory.MemRegisterEnum;
import com.azurelithium.gueimboi.utils.ByteUtils;
import com.azurelithium.gueimboi.utils.StringUtils;

class InterruptManager {

    private static final int VBLANK_INT_REQUEST_BIT = 0;
    private static final int LCDSTAT_INT_REQUEST_BIT = 1;
    private static final int TIMER_INT_REQUEST_BIT = 2;
    private static final int SERIAL_INT_REQUEST_BIT = 3;
    private static final int JOYPAD_INT_REQUEST_BIT = 4;

    private static final int VBLANK_INT_VECTOR = 0x40;
    private static final int LCDSTAT_INT_VECTOR = 0x48;
    private static final int TIMER_INT_VECTOR = 0x50;
    private static final int SERIAL_INT_VECTOR = 0x58;
    private static final int JOYPAD_INT_VECTOR = 0x60;

    private ExecutionContext executionContext;
    private MMU MMU;
    
    InterruptManager(ExecutionContext _executionContext, MMU _MMU) {
        executionContext = _executionContext;
        MMU = _MMU;
    }

    boolean isAnyInterruptRequestedAndEnabled() {
        int IF = MMU.getMemRegister(MemRegisterEnum.IF);
        int IE = MMU.getMemRegister(MemRegisterEnum.IE);
        return (IF & IE & 0x1F) != 0;
    }

    ListIterator<InstructionStep> checkInterrupts() {
        if (executionContext.getIME()) {
            if (isVBlankInterruptEnabled() && isVBlankInterruptRequested()) {
                unrequestVBlankInterrupt();               
                return jumpToIntVector(VBLANK_INT_VECTOR);
            } else if (isLCDSTATInterruptEnabled() && isLCDSTATInterruptRequested()) {
                unrequestLCDSTATInterrupt();
                return jumpToIntVector(LCDSTAT_INT_VECTOR);
            } else if (isTimerInterruptEnabled() && isTimerInterruptRequested()) {
                unrequestTimerInterrupt();
                return jumpToIntVector(TIMER_INT_VECTOR);
            } else if (isSerialInterruptEnabled() && isSerialInterruptRequested()) {
                unrequestSerialInterrupt();
                return jumpToIntVector(SERIAL_INT_VECTOR);
            } else if (isJoypadInterruptEnabled() && isJoypadInterruptRequested()) {
                unrequestJoypadInterrupt();
                return jumpToIntVector(JOYPAD_INT_VECTOR);
            }
        }
        return null;
    }

    void checkScheduleIME() {
        if (executionContext.getScheduleIME()) {
            executionContext.setIME(true);
            executionContext.setScheduleIME(false);
        }
    }

    private boolean isVBlankInterruptEnabled() {
        int IE = MMU.getMemRegister(MemRegisterEnum.IE);
        return (IE & 1 << VBLANK_INT_REQUEST_BIT) != 0;
    }

    private boolean isLCDSTATInterruptEnabled() {
        int IE = MMU.getMemRegister(MemRegisterEnum.IE);
        return (IE & 1 << LCDSTAT_INT_REQUEST_BIT) != 0;
    }

    private boolean isTimerInterruptEnabled() {
        int IE = MMU.getMemRegister(MemRegisterEnum.IE);
        return (IE & 1 << TIMER_INT_REQUEST_BIT) != 0;
    }

    private boolean isSerialInterruptEnabled() {
        int IE = MMU.getMemRegister(MemRegisterEnum.IE);
        return (IE & 1 << SERIAL_INT_REQUEST_BIT) != 0;
    }

    private boolean isJoypadInterruptEnabled() {
        int IE = MMU.getMemRegister(MemRegisterEnum.IE);
        return (IE & 1 << JOYPAD_INT_REQUEST_BIT) != 0;
    }

    private boolean isVBlankInterruptRequested() {
        int IF = MMU.getMemRegister(MemRegisterEnum.IF);
        return (IF & 1 << VBLANK_INT_REQUEST_BIT) != 0;
    }

    private boolean isLCDSTATInterruptRequested() {
        int IF = MMU.getMemRegister(MemRegisterEnum.IF);
        return (IF & 1 << LCDSTAT_INT_REQUEST_BIT) != 0;
    }

    private boolean isTimerInterruptRequested() {
        int IF = MMU.getMemRegister(MemRegisterEnum.IF);
        return (IF & 1 << TIMER_INT_REQUEST_BIT) != 0;
    }

    private boolean isSerialInterruptRequested() {
        int IF = MMU.getMemRegister(MemRegisterEnum.IF);
        return (IF & 1 << SERIAL_INT_REQUEST_BIT) != 0;
    }

    private boolean isJoypadInterruptRequested() {
        int IF = MMU.getMemRegister(MemRegisterEnum.IF);
        return (IF & 1 << JOYPAD_INT_REQUEST_BIT) != 0;
    }

    private void unrequestVBlankInterrupt() {
        int IF = MMU.getMemRegister(MemRegisterEnum.IF);
        IF = ByteUtils.resetBit(IF, VBLANK_INT_REQUEST_BIT);
        MMU.setMemRegister(MemRegisterEnum.IF, IF);
    }

    private void unrequestLCDSTATInterrupt() {
        int IF = MMU.getMemRegister(MemRegisterEnum.IF);
        IF = ByteUtils.resetBit(IF, LCDSTAT_INT_REQUEST_BIT);
        MMU.setMemRegister(MemRegisterEnum.IF, IF);
    }

    private void unrequestTimerInterrupt() {
        int IF = MMU.getMemRegister(MemRegisterEnum.IF);
        IF = ByteUtils.resetBit(IF, TIMER_INT_REQUEST_BIT);
        MMU.setMemRegister(MemRegisterEnum.IF, IF);
    }

    private void unrequestSerialInterrupt() {
        int IF = MMU.getMemRegister(MemRegisterEnum.IF);
        IF = ByteUtils.resetBit(IF, SERIAL_INT_REQUEST_BIT);
        MMU.setMemRegister(MemRegisterEnum.IF, IF);
    }

    private void unrequestJoypadInterrupt() {
        int IF = MMU.getMemRegister(MemRegisterEnum.IF);
        IF = ByteUtils.resetBit(IF, JOYPAD_INT_REQUEST_BIT);
        MMU.setMemRegister(MemRegisterEnum.IF, IF);
    }

    private ListIterator<InstructionStep> jumpToIntVector(int vector) {
        executionContext.setIME(false);

        Instruction jumpToIntVector = new Instruction(-1, "INT " + StringUtils.toHex(vector));
        PC PC = new PC();

        jumpToIntVector.addStep(new InternalDelay());
        jumpToIntVector.addStep(new InternalDelay());
        jumpToIntVector.addStep(new PushMSB(PC));
        jumpToIntVector.addStep(new PushLSB(PC));
        jumpToIntVector.addStep(new INT(vector));
        jumpToIntVector.addStep(new InternalDelay());

        return jumpToIntVector.getInstructionStepIterator();
    }

}