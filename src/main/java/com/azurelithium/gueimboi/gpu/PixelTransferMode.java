package com.azurelithium.gueimboi.gpu;

import com.azurelithium.gueimboi.gpu.GPU.GPURegisters;
import com.azurelithium.gueimboi.gui.Display;

class PixelTransferMode extends GPUMode {

    private final int PIXELS_PER_LINE;

    private int linePixelsShitfed;

    private PixelFIFO pixelFIFO;
    private PixelFetcher pixelFetcher;
    private boolean pixelFetcherTickSwitch;

    PixelTransferMode(GPURegisters _gpuRegisters, Display display) {
        PIXELS_PER_LINE = display.getGameboyLCDWidth();
        pixelFIFO = new PixelFIFO(_gpuRegisters, display);
        pixelFetcher = new PixelFetcher(_gpuRegisters, pixelFIFO);
    }
    
    void tick() {  
        pixelFIFO.tick();
        linePixelsShitfed = pixelFIFO.getPixelOffset();
        if (pixelFetcherTickSwitch) {
            pixelFetcher.tick();   
        }
        pixelFetcherTickSwitch = !pixelFetcherTickSwitch;
        ticksInMode++;
    }

    boolean isFinished() {
        return linePixelsShitfed == PIXELS_PER_LINE;
    }

}