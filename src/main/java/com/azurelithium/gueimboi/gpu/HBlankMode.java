package com.azurelithium.gueimboi.gpu;

class HBlankMode extends GPUMode {

    private final int MAXIMUM_DURATION_TICKS = 204;

    HBlankMode(int lookaheadTicks) {
        ticksInMode = lookaheadTicks;
    }
    
    void tick() {
        ticksInMode++;
    }

    boolean isFinished() {
        return ticksInMode == MAXIMUM_DURATION_TICKS;
    }

}