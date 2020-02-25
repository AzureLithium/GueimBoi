package com.azurelithium.gueimboi.gpu;

class HBlankMode extends GPUMode {

    private final int DURATION_TICKS;

    HBlankMode(int durationTicks) {
        /*if (lookaheadTicks == 0) {
            lookaheadTicks = 0;
        } else if (MAXIMUM_DURATION_TICKS < lookaheadTicks) {
            lookaheadTicks = MAXIMUM_DURATION_TICKS;
        } else {
            ticksInMode = lookaheadTicks;
        }*/
        DURATION_TICKS = durationTicks;
    }
    
    void tick() {
        ticksInMode++;
    }

    boolean isFinished() {
        return ticksInMode == DURATION_TICKS;
    }

}