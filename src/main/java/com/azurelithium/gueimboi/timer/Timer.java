package com.azurelithium.gueimboi.timer;

import com.azurelithium.gueimboi.common.Component;
import com.azurelithium.gueimboi.common.GameBoy;
import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.memory.MemRegisterEnum;

public class Timer extends Component {

    private final int TIMER_ENABLED_BIT = 2;
    private final int TIMER_INT_REQUEST_BIT = 2;

    private boolean lastTickRateBit;
    private boolean overflow = false;
    private int ticksSinceOverflow;
    private boolean IFOverride = false;

    private MMU MMU;

    public Timer(MMU _MMU) {
        MMU = _MMU;
    }

    private int getDIVLSB() {
        return MMU.getMemRegister(MemRegisterEnum.DIVLSB);
    }

    private int getDIV() {
        return MMU.getMemRegister(MemRegisterEnum.DIV);
    }    

    private int getTIMA() {
        return MMU.getMemRegister(MemRegisterEnum.TIMA);
    }

    private int getTMA() {
        return MMU.getMemRegister(MemRegisterEnum.TMA);
    }

    private int getTAC() {
        return MMU.getMemRegister(MemRegisterEnum.TAC);
    }

    private void setDIVLSB(int value) {
        MMU.setMemRegister(MemRegisterEnum.DIVLSB, value);
    }

    private void setDIV(int value) {
        MMU.setMemRegister(MemRegisterEnum.DIV, value);
    }

    public void setTIMA(int value) {
        MMU.setMemRegister(MemRegisterEnum.TIMA, value);
    }

    public void tick() {
        incInternalCounter();

        if (overflow) {
            ticksSinceOverflow++;
            if (ticksSinceOverflow == GameBoy.CYCLES_PER_CPU_TICK) {  
                setTIMA(getTMA());
                if (!IFOverride) {
                    requestTimerInterrupt();
                }
            }
            if (ticksSinceOverflow > GameBoy.CYCLES_PER_CPU_TICK) {
                unsetOverflow();
            }
        }
        else if (isTimerEnabled() && lastTickRateBit && !getTickRateBit()) {
            incTIMA();
        }

        lastTickRateBit = getTickRateBit(); 
    }

    private void incInternalCounter() {
        incDIVLSB();
        if (getDIVLSB() == 0) {
            incDIV();
        }
    }

    private void incDIVLSB() {
        int DIVLSB = getDIVLSB();
        DIVLSB = ++DIVLSB & 0xFF;
        setDIVLSB(DIVLSB);
    }

    private void incDIV() {
        int DIV = getDIV();
        DIV = ++DIV & 0xFF;
        setDIV(DIV);
    }

    private void requestTimerInterrupt() {
        int IF = MMU.getMemRegister(MemRegisterEnum.IF);
        IF |= 1 << TIMER_INT_REQUEST_BIT;
        MMU.setMemRegister(MemRegisterEnum.IF, IF);
    }

    public void unsetOverflow() {
        overflow = false;
        ticksSinceOverflow = 0;
        IFOverride = false;
    }

    public boolean isTimerEnabled() {
        int TAC = getTAC();
        return (TAC & 1 << TIMER_ENABLED_BIT) != 0;
    }  

    public boolean getTickRateBit() {
        return (getInternalCounter() & (1 << getTIMATickRateBitPosition())) != 0;
    }

    private int getInternalCounter() {
        return (getDIV() << Byte.SIZE) + getDIVLSB();
    }

    private int getTIMATickRateBitPosition() {
        int TIMATickRateBitPosition = 0;
        int TAC = getTAC();
        switch(TAC & 0x3) {
            case 0x0:
                TIMATickRateBitPosition = 9;
                break;
            case 0x1:
                TIMATickRateBitPosition = 3;
                break;
            case 0x2:
                TIMATickRateBitPosition = 5;
                break;
            case 0x3:
                TIMATickRateBitPosition = 7;
                break;
        }
        return TIMATickRateBitPosition;
    }
    
    private void incTIMA() {
        int TIMA = getTIMA();
        TIMA = ++TIMA & 0xFF;
        setTIMA(TIMA);
        overflow = TIMA == 0;
    }

    public int getTicksSinceOverflow() {
        return ticksSinceOverflow;
    }

    public void checkTIMAUnexpectedIncrease() {
        if (getTickRateBit()) {
            incTIMA();
            lastTickRateBit = false;
        }
    }

    public void setIFOverride() {
        IFOverride = true;
    }

}