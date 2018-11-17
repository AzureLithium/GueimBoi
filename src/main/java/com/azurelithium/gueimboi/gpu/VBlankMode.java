package com.azurelithium.gueimboi.gpu;

class VBlankMode extends GPUMode {

    private final int DURATION_TICKS = 456;
    
    void tick() {
        ticksInMode++;
    }

    boolean isFinished() {
        return ticksInMode == DURATION_TICKS;
    }

}