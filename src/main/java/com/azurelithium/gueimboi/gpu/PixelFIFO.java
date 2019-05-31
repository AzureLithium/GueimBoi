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
    private GPURegisters gpuRegisters;

    PixelFIFO(GPURegisters _gpuRegisters, Display _display) {
        gpuRegisters = _gpuRegisters;
        pixelsToIgnore = gpuRegisters.getSCX();
        lineOffset = gpuRegisters.getLY();        
        display = _display;
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
        return pixelFIFO.size() <= Byte.SIZE;
    }

    void insertBlock(LinkedList<Integer> block) {
        pixelFIFO.addAll(block);
    }

    boolean canShift() {
        return pixelFIFO.size() > Byte.SIZE;
    }

    void shift() {
        int pixel = pixelFIFO.pop();
        if (pixelsToIgnore > 0) {
            pixelsToIgnore--;
        } else {
            display.setPixel(pixelOffset, lineOffset, gpuRegisters.isBGEnabled() ? pixel : 0);
            pixelOffset++;
        }
    }

    void tick() {
        if (canShift()) {
            shift();
        }
    }

}