package com.azurelithium.gueimboi.gpu;

import java.util.LinkedList;
import java.util.List;

import com.azurelithium.gueimboi.gpu.GPU.GPURegisters;
import com.azurelithium.gueimboi.gui.Display;

class PixelFIFO {

    private final LinkedList<Pixel> pixelFIFO = new LinkedList<Pixel>();
    private final Display display;

    private boolean isBlocked;
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

    void block() {
        isBlocked = true;
    }

    void unblock() {
        isBlocked = false;
    }

    boolean isBlocked() {
        return isBlocked;
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

    void insertBlock(List<Pixel> block) {
        pixelFIFO.addAll(block);
    }

    void overlap(List<Pixel> block) {
        for (int i=0; i < block.size(); i++) {
            Pixel existingPixel = pixelFIFO.get(i);
            Pixel overlappingPixel = block.get(i);
            if (existingPixel.compareTo(overlappingPixel) < 0) {
                if (i < pixelFIFO.size()) {
                    pixelFIFO.set(i, overlappingPixel);
                } else {
                    pixelFIFO.add(overlappingPixel);
                }                
            }
        }
    }

    boolean canShift() {
        return !isBlocked && pixelFIFO.size() > 8;
    }

    void shift() {
        Pixel pixel = pixelFIFO.pop();        
        int[] BGP = gpuRegisters.getBGP();
        int[] OBP0 = gpuRegisters.getOBP0();
        int[] OBP1 = gpuRegisters.getOBP1();  
        if (pixelsToIgnore > 0) {
            pixelsToIgnore--;
        } else {
            int pixelColor = 0;
            switch (pixel.getPixelType()) {
                case BACKGROUND:
                    pixelColor = gpuRegisters.isBGEnabled() ? BGP[pixel.getValue()] : 0;
                    break;
                case SPRITE:
                    pixelColor = pixel.getPaletteNumber() ? OBP1[pixel.getValue()] : OBP0[pixel.getValue()];
                    break;
                case WINDOW:
            }
            display.setPixel(pixelOffset, lineOffset, pixelColor);
            pixelOffset++;
        }
    }

    void tick() {
        if (canShift()) {
            shift();
        }
    }

}