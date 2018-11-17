package com.azurelithium.gueimboi.gpu;

class OAMSearchMode extends GPUMode {

    private final int DURATION_TICKS = 80;
    
    void tick() {
        ticksInMode++;
    }

    boolean isFinished() {
        return ticksInMode == DURATION_TICKS;
    }

}