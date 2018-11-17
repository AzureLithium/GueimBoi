package com.azurelithium.gueimboi.gpu;

abstract class GPUMode {

    protected int ticksInMode;

    abstract void tick();
    abstract boolean isFinished();
}