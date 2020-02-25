package com.azurelithium.gueimboi.gpu;

import java.util.LinkedList;
import java.util.ListIterator;

import com.azurelithium.gueimboi.gpu.GPU.GPURegisters;
import com.azurelithium.gueimboi.gui.Display;

class PixelTransferMode extends GPUMode {

    private final int PIXELS_PER_LINE;
    private final int TICKS_PER_M_CYCLE = 2;

    private PixelFIFO pixelFIFO;
    private PixelFetcher pixelFetcher;
    private ListIterator<Sprite> spriteIterator;


    PixelTransferMode(GPURegisters _gpuRegisters, LinkedList<Sprite> sprites, Display display) {
        PIXELS_PER_LINE = display.getGameboyLCDWidth();
        pixelFIFO = new PixelFIFO(_gpuRegisters, display);
        pixelFetcher = new PixelFetcher(_gpuRegisters, pixelFIFO, PIXELS_PER_LINE);
        spriteIterator = sprites.listIterator();
        while (spriteIterator.hasNext()) {
            Sprite sprite = spriteIterator.next();
            if (sprite.getX() != 0) {
                spriteIterator.previous();
                break;
            }
        }
    }
    
    void tick() {
        if (pixelFIFO.size() != 0 && spriteIterator.hasNext()) {
            Sprite sprite = spriteIterator.next();
            if (sprite.getX() < 8 // be careful with partially visible sprites from the left side of the screen
                || sprite.getX() - 8 == pixelFIFO.getPixelOffset()) {                 
                pixelFIFO.block();
                pixelFetcher.fetchSprite(sprite);
            } else {
                spriteIterator.previous();
            }
        }

        pixelFIFO.tick();
        if (ticksInMode % TICKS_PER_M_CYCLE == 0) {
            pixelFetcher.tick();   
        }
        ticksInMode++;
    }

    boolean isFinished() {
        return pixelFIFO.getPixelOffset() == PIXELS_PER_LINE;
    }

}