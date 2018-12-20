package com.azurelithium.gueimboi.gpu;

import java.util.LinkedList;
import com.azurelithium.gueimboi.gpu.GPU.GPURegisters;
import com.azurelithium.gueimboi.gui.Display;

class PixelFIFO {

    private final LinkedList<Integer> pixelFIFO = new LinkedList<Integer>();
    private final Display display;

    private int lineOffset;
    private int pixelOffset;
    private int pixelsToIgnore;
    private boolean LCDEnabled;

    PixelFIFO(GPURegisters gpuRegisters, Display _display) {
        pixelsToIgnore = gpuRegisters.getSCX();
        display = _display;
        lineOffset = gpuRegisters.getLY();
        LCDEnabled = gpuRegisters.isLCDEnabled();
    }

    int size() {
        return pixelFIFO.size();
    }

    int getLineOffset() {
        return lineOffset;
    }

    int getPixelOffset() {
        return pixelOffset;
    }

    boolean canInsertBlock() {
        return pixelFIFO.size() <= 8;
    }

    void insertBlock(LinkedList<Integer> block) {
        pixelFIFO.addAll(block);
    }

    boolean canShift() {
        return pixelFIFO.size() > 8;
    }

    void shift() {
        int pixel = pixelFIFO.pop();
        if (pixelsToIgnore > 0) {
            pixelsToIgnore--;
        } else {
            display.setPixel(pixelOffset, lineOffset, LCDEnabled ? pixel : 0);
            pixelOffset++;
        }
    }

    void tick() {
        if (canShift()) {
            shift();
        }
    }

}